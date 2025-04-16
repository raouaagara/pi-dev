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

#[Route('/equipement')]
final class EquipementController extends AbstractController
{
    private ValidatorInterface $validator;

    public function __construct(ValidatorInterface $validator)
    {
        $this->validator = $validator;
    }

    #[Route('/', name: 'app_equipement_index', methods: ['GET', 'POST'])]
    public function index(Request $request, EquipementRepository $equipementRepository, EntityManagerInterface $entityManager): Response
    {
        $equipement = new Equipement();

        // Création du formulaire avec validation HTML5 désactivée
        $form = $this->createForm(Equipement1Type::class, $equipement, [
            'attr' => ['novalidate' => 'novalidate']
        ]);

        $form->handleRequest($request);

        if ($form->isSubmitted()) {
            // Validation manuelle
            $errors = $this->validator->validate($equipement);

            if (count($errors) > 0) {
                foreach ($errors as $error) {
                    $this->addFlash('error', $error->getMessage());
                }
            } else {
                $entityManager->persist($equipement);
                $entityManager->flush();
                $this->addFlash('success', 'Équipement ajouté avec succès.');

                // Redirection pour éviter la résubmission et réinitialiser le formulaire
                return $this->redirectToRoute('app_equipement_index');
            }
        }

        return $this->render('equipement/index.html.twig', [
            'equipements' => $equipementRepository->findAll(),
            'form' => $form->createView(),
        ]);
    }

    #[Route('/{id}', name: 'app_equipement_show', methods: ['GET'])]
    public function show($id, EquipementRepository $equipementRepository): Response
    {
        $equipement = $equipementRepository->find($id);

        if (!$equipement) {
            throw $this->createNotFoundException("L'équipement demandé est introuvable.");
        }

        return $this->render('equipement/show.html.twig', [
            'equipement' => $equipement,
        ]);
    }

    #[Route('/{id}/edit', name: 'app_equipement_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Equipement $equipement, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(Equipement1Type::class, $equipement, [
            'attr' => ['novalidate' => 'novalidate']
        ]);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $errors = $this->validator->validate($equipement);

            if (count($errors) > 0) {
                foreach ($errors as $error) {
                    $this->addFlash('error', $error->getMessage());
                }
            } else {
                $entityManager->flush();
                $this->addFlash('success', 'Équipement modifié avec succès.');
                return $this->redirectToRoute('app_equipement_index');
            }
        }

        return $this->render('equipement/edit.html.twig', [
            'equipement' => $equipement,
            'form' => $form->createView(),
        ]);
    }

    #[Route('/{id}', name: 'app_equipement_delete', methods: ['POST'])]
    public function delete(Request $request, Equipement $equipement, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete' . $equipement->getId(), $request->get('_token'))) {
            $entityManager->remove($equipement);
            $entityManager->flush();
            $this->addFlash('success', 'Équipement supprimé avec succès.');
        }

        return $this->redirectToRoute('app_equipement_index');
    }
    #[Route('/pdf', name: 'app_equipement_pdf', methods: ['GET'])]
public function generatePdf(EquipementRepository $equipementRepository): Response
{
    $equipements = $equipementRepository->findAll();

    $html = $this->renderView('equipement/pdf.html.twig', [
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
}}
