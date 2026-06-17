package com.ai.resumeanalyzer.service;

import com.ai.resumeanalyzer.entity.AnalysisHistory;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public ByteArrayInputStream generatePdf(AnalysisHistory history) {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, out);

            document.open();

            document.add(new Paragraph("AI Resume Analyzer Report"));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("File Name : " + history.getFileName()));
            document.add(new Paragraph("ATS Score : " + history.getAtsScore()));
            document.add(new Paragraph("Uploaded At : " + history.getUploadedAt()));

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}