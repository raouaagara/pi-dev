<?php

namespace App\Command;

use Symfony\Component\Console\Attribute\AsCommand;
use Symfony\Component\Console\Command\Command;
use Symfony\Component\Console\Input\InputInterface;
use Symfony\Component\Console\Output\OutputInterface;
use Symfony\Component\Console\Style\SymfonyStyle;
use Symfony\Component\Process\Process;

#[AsCommand(
    name: 'app:generate:entities',
    description: 'Generate entity classes from existing database',
)]
class GenerateEntitiesCommand extends Command
{
    protected function execute(InputInterface $input, OutputInterface $output): int
    {
        $io = new SymfonyStyle($input, $output);

        $io->title('Generating Entity Classes from Database...');
        $io->newLine();

        // 🛠️ Commande doctrine pour générer les entités à partir de la DB
        $process = new Process(['php', 'bin/console', 'doctrine:mapping:import', 'App\\Entity', 'annotation', '--path=src/Entity']);
        $process->run(function ($type, $buffer) use ($output) {
            $output->write($buffer);
        });

        // 🛠️ Générer les getters/setters
        $process = new Process(['php', 'bin/console', 'doctrine:generate:entities', 'App']);
        $process->run(function ($type, $buffer) use ($output) {
            $output->write($buffer);
        });

        $io->success('Entities successfully generated in src/Entity/');

        return Command::SUCCESS;
    }
}
