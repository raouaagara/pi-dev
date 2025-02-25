package services;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;
import models.Equipement;
import java.io.FileNotFoundException;


public class PDFGenerator {
    public void handleExportPDF(Stage stage, List<Equipement> equipements) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                PdfWriter writer = new PdfWriter(file.getAbsolutePath());
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                // Titre du PDF
                document.add(new Paragraph("Liste des Équipements").setBold().setFontSize(16));

                // Création du tableau avec une colonne pour le prix
                float[] columnWidths = {50f, 200f, 100f, 80f}; // Ajustement des tailles
                Table table = new Table(columnWidths);

                // En-tête du tableau
                table.addCell(new Cell().add(new Paragraph("ID")));
                table.addCell(new Cell().add(new Paragraph("Nom")));
                table.addCell(new Cell().add(new Paragraph("Type")));
                table.addCell(new Cell().add(new Paragraph("Prix")));

                // Ajout des données
                for (Equipement equip : equipements) {
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(equip.getId()))));
                    table.addCell(new Cell().add(new Paragraph(equip.getName())));
                    table.addCell(new Cell().add(new Paragraph(equip.getCategory().getName())));
                    table.addCell(new Cell().add(new Paragraph(String.format("%.2f TND", equip.getPrice()))));
                }

                document.add(table);
                document.close();

                System.out.println("PDF exporté avec succès !");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
