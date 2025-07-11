package app;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PdfReportExporter {

    public static boolean exportReportToPdf(java.io.File file, List<ProductSummary> data, LocalDate fromDate, LocalDate toDate) {
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 54, 36);

        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // ✅ Загружаем кастомный шрифт из ресурсов
            BaseFont baseFont;
            try (InputStream fontStream = PdfReportExporter.class.getResourceAsStream("/fonts/RobotoSlab.ttf")) {
                if (fontStream == null) {
                    throw new RuntimeException("Font file not found in resources/fonts/");
                }
                baseFont = BaseFont.createFont("RobotoSlab.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, fontStream.readAllBytes(), null);
            }

            // 🎨 Настраиваем шрифты на базе RobotoSlab
            Font titleFont = new Font(baseFont, 16, Font.BOLD);
            Font headerFont = new Font(baseFont, 12, Font.BOLD);
            Font cellFont = new Font(baseFont, 11);

            // Заголовок
            Paragraph title = new Paragraph(
                    "Darea de seamă pentru produse pe perioada din " + fromDate + " până la " + toDate,
                    titleFont
            );
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            // Таблица
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4, 2, 2, 2, 2});

            addCell(table, "Produs", headerFont, Element.ALIGN_LEFT); // названия — слева
            addCell(table, "Sold inițial", headerFont, Element.ALIGN_CENTER);
            addCell(table, "Venituri", headerFont, Element.ALIGN_CENTER);
            addCell(table, "Cheltuieli", headerFont, Element.ALIGN_CENTER);
            addCell(table, "Sold final", headerFont, Element.ALIGN_CENTER);

            for (ProductSummary ps : data) {
                addCell(table, ps.getDenumirea(), cellFont, Element.ALIGN_LEFT); // названия — слева
                addCell(table, Integer.toString(ps.getSoldInitial()), cellFont, Element.ALIGN_CENTER);
                addCell(table, Integer.toString(ps.getVenituri()), cellFont, Element.ALIGN_CENTER);
                addCell(table, Integer.toString(ps.getCheltuieli()), cellFont, Element.ALIGN_CENTER);
                addCell(table, Integer.toString(ps.getSoldFinal()), cellFont, Element.ALIGN_CENTER);
            }

            document.add(table);
            document.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void addCell(PdfPTable table, String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(5f);
        table.addCell(cell);
    }
}
