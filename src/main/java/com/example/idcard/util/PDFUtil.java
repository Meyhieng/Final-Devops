package com.example.idcard.util;

import com.example.idcard.model.Profile;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

public class PDFUtil {

    public static byte[] generate(Profile profile) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        generateToStream(profile, out);
        return out.toByteArray();
    }

    public static void generateBatch(List<Profile> profiles, OutputStream out) throws Exception {
        Document document = new Document(PageSize.A5);
        PdfWriter.getInstance(document, out);
        document.open();

        for (int i = 0; i < profiles.size(); i++) {
            addProfilePage(document, profiles.get(i));
            if (i < profiles.size() - 1) {
                document.newPage();
            }
        }

        document.close();
    }

    private static void generateToStream(Profile profile, OutputStream out) throws Exception {
        Document document = new Document(PageSize.A5);
        PdfWriter.getInstance(document, out);
        document.open();
        addProfilePage(document, profile);
        document.close();
    }

    private static void addProfilePage(Document document, Profile profile) throws Exception {

        Font headerFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.WHITE);
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);
        PdfPCell headerCell = new PdfPCell(new Phrase("ID CARD", headerFont));
        headerCell.setBackgroundColor(new BaseColor(29, 78, 216)); // #1d4ed8
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setPadding(10);
        headerCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(headerCell);
        document.add(headerTable);

        document.add(Chunk.NEWLINE);

        Font labelFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        Font valueFont = new Font(Font.FontFamily.HELVETICA, 10);

        addRow(document, "Name:", profile.getFullName(), labelFont, valueFont);
        addRow(document, "ID:", profile.getRegistrationNumber(), labelFont, valueFont);
        addRow(document, "Type:", profile.getType() != null ? profile.getType().name() : "", labelFont, valueFont);
        addRow(document, "Department:", profile.getDepartment(), labelFont, valueFont);
        addRow(document, "Title:", profile.getTitle(), labelFont, valueFont);
        addRow(document, "Email:", profile.getEmail(), labelFont, valueFont);
        addRow(document, "Phone:", profile.getPhone(), labelFont, valueFont);
        addRow(document, "Blood Group:", profile.getBloodGroup(), labelFont, valueFont);

        document.add(Chunk.NEWLINE);

        if (profile.getUuid() != null) {
            byte[] qrBytes = QRCodeUtil.generateQR(profile.getUuid(), 100, 100);
            Image qrImage = Image.getInstance(qrBytes);
            qrImage.setAlignment(Element.ALIGN_CENTER);
            document.add(qrImage);
        }
    }

    private static void addRow(Document doc, String label, String value,
                                Font labelFont, Font valueFont) throws Exception {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 2});

        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(4);

        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "-", valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(4);

        table.addCell(labelCell);
        table.addCell(valueCell);
        doc.add(table);
    }
}