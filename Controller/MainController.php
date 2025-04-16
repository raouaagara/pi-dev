<?php
namespace App\Controller;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
class MainController extends AbstractController
{
    #[Route('/', name: 'accueil')]
    public function accueil(): Response
    {
        return $this->render('main/accueil.html.twig');
    }

    #[Route('/evenements', name: 'evenements')]
    public function evenements(): Response
    {
        return $this->render('main/evenements.html.twig');
    }

    #[Route('/equipement', name: 'equipement')]
    public function equipements(): Response
    {
        return $this->render('equipement/index.html.twig');
    }

    #[Route('/reclamations', name: 'reclamations')]
    public function reclamations(): Response
    {
        return $this->render('reclamation/index.html.twig');
    }
    #[Route('/category', name: 'category')]
    public function category(): Response
    {
        return $this->render('category/show.html.twig');
    }
}
