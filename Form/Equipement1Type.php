<?php

namespace App\Form;

use App\Entity\Category;
use App\Entity\Equipement;
use App\Entity\User;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class Equipement1Type extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('name')
            ->add('description')
            ->add('price')
            ->add('image')
            ->add('availability')
            ->add('dateAdded', null, [
                'widget' => 'single_text',
            ])
            ->add('category', EntityType::class, [
                'class' => Category::class,
                'choice_label' => 'name', // Afficher le nom de la catégorie
            ])
            ->add('partner', EntityType::class, [
                'class' => User::class,
                'choice_label' => 'firstname', // Afficher le prénom de l'utilisateur
            ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Equipement::class,
        ]);
    }
}

