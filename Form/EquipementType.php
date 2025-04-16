<?php

namespace App\Form;

use App\Entity\category;
use App\Entity\Equipement;
use App\Entity\user;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class EquipementType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('name')
            ->add('description')
            ->add('price')
            ->add('image')
            ->add('availabitily')
            ->add('dateAdded', null, [
                'widget' => 'single_text',
            ])
            ->add('category', EntityType::class, [
                'class' => category::class,
                'choice_label' => 'id',
            ])
            ->add('partner', EntityType::class, [
                'class' => user::class,
                'choice_label' => 'id',
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
