<?php

namespace App\Controller;

use App\Entity\Equipement;
use App\Form\Equipement1Type;
use App\Repository\EquipementRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Validator\Validator\ValidatorInterface;
use Dompdf\Dompdf;
use Dompdf\Options;

#[Route('/admin_equipement')]

final class EquipementAdminController extends AbstractController
{
    #[Route('/', name: 'app_equipement_index_admin', methods: ['GET', 'POST'])]
    public function index(Request $request, EquipementRepository $equipementRepository, EntityManagerInterface $entityManager, ValidatorInterface $validator): Response
    {
        $equipement = new Equipement();
        $form = $this->createForm(Equipement1Type::class, $equipement, [
            'attr' => ['novalidate' => 'novalidate']
        ]);
        $form->handleRequest($request);

        if ($form->isSubmitted()) {
            $errors = $validator->validate($equipement);

            if (count($errors) > 0) {
                foreach ($errors as $error) {
                    $this->addFlash('error', $error->getMessage());
                }
            } else {
                $entityManager->persist($equipement);
                $entityManager->flush();
                $this->addFlash('success', 'Équipement ajouté avec succès.');
                return $this->redirectToRoute('app_equipement_index_admin');
            }
        }

        return $this->render('equipement/index.html.twig', [
            'equipements' => $equipementRepository->findAll(),
            'form' => $form->createView(),
        ]);
    }

    #[Route('/{id}', name: 'app_equipement_show_admin', methods: ['GET'])]
    public function show($id, EquipementRepository $equipementRepository): Response
    {
        $equipement = $equipementRepository->find($id);

        if (!$equipement) {
            throw $this->createNotFoundException("L'équipement demandé est introuvable.");
        }

        return $this->render('admin/show.html.twig', [
            'equipement' => $equipement,
        ]);
    }

    #[Route('/{id}/edit', name: 'app_equipement_edit_admin', methods: ['GET', 'POST'])]
    public function edit(Request $request, Equipement $equipement, EntityManagerInterface $entityManager, ValidatorInterface $validator): Response
    {
        $form = $this->createForm(Equipement1Type::class, $equipement, [
            'attr' => ['novalidate' => 'novalidate']
        ]);
        $form->handleRequest($request);

        if ($form->isSubmitted()) {
            $errors = $validator->validate($equipement);

            if (count($errors) > 0) {
                foreach ($errors as $error) {
                    $this->addFlash('error', $error->getMessage());
                }
            } else {
                $entityManager->flush();
                $this->addFlash('success', 'Équipement modifié avec succès.');
                return $this->redirectToRoute('app_equipement_index_admin');
            }
        }

        return $this->render('admin/edit.html.twig', [
            'equipement' => $equipement,
            'form' => $form->createView(),
        ]);
    }

    #[Route('/{id}', name: 'app_equipement_delete_admin', methods: ['POST'])]
    public function delete(Request $request, Equipement $equipement, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete' . $equipement->getId(), $request->get('_token'))) {
            $entityManager->remove($equipement);
            $entityManager->flush();
            $this->addFlash('success', 'Équipement supprimé avec succès.');
        }

        return $this->redirectToRoute('app_equipement_index_admin');
    }

    #[Route('/pdf', name: 'app_equipement_pdf_admin', methods: ['GET'])]
    public function generatePdf(EquipementRepository $equipementRepository): Response
    {
        $equipements = $equipementRepository->findAll();

        $html = $this->renderView('admin/pdf.html.twig', [
            'equipements' => $equipements,
        ]);

        $options = new Options();
        $options->set('defaultFont', 'Arial');

        $dompdf = new Dompdf($options);
        $dompdf->loadHtml($html);
        $dompdf->setPaper('A4', 'portrait');
        $dompdf->render();

        return new Response($dompdf->stream("equipements.pdf", [
            "Attachment" => true
        ]));
    }
}
