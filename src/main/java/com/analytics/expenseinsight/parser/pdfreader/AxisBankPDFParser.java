package com.analytics.expenseinsight.parser.pdfreader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AxisBankPDFParser {

    // Transaction DTO
    static class Transaction {
        String date;
        String description;
        String debit;
        String credit;
        String balance;

        public Transaction(String date, String description, String debit, String credit, String balance) {
            this.date = date;
            this.description = description;
            this.debit = debit;
            this.credit = credit;
            this.balance = balance;
        }
    }

    // Step 1: Extract text using OCR
    public static String extractTextWithOCR(String filePath) {
        StringBuilder sb = new StringBuilder();
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            ITesseract tesseract = new Tesseract();
            tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata"); // adjust path
            tesseract.setLanguage("eng");

            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300);
                String text = tesseract.doOCR(bim);
                sb.append(text).append("\n");
            }
        } catch (IOException | TesseractException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    // Step 2: Parse text into transactions
    public static List<Transaction> parseTransactions(String text) {
        List<Transaction> transactions = new ArrayList<>();

        // Example Axis Bank line pattern:  "01-Apr-25   UPI/...   500.00    10,500.00"
        Pattern pattern = Pattern.compile(
                "(\\d{2}-[A-Za-z]{3}-\\d{2})\\s+(.+?)\\s+(\\d{1,10}\\.\\d{2})?\\s*(\\d{1,10}\\.\\d{2})?\\s+(\\d{1,10}\\.\\d{2})"
        );

        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String date = matcher.group(1);
            String description = matcher.group(2).trim();
            String debit = matcher.group(3) != null ? matcher.group(3) : "";
            String credit = matcher.group(4) != null ? matcher.group(4) : "";
            String balance = matcher.group(5);

            transactions.add(new Transaction(date, description, debit, credit, balance));
        }
        return transactions;
    }

    // Step 3: Convert to JSON
    public static String toJson(List<Transaction> transactions) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(transactions);
    }

    public static void main(String[] args) {
        String filePath = "C:/Users/elatchireddi/Downloads/BankSheetFirstPage.pdf"; // <-- replace with your local path

        // Extract text using OCR
        String text = extractTextWithOCR(filePath);
        System.out.println("Extracted Text:\n" + text);

        // Parse into transactions
        List<Transaction> transactions = parseTransactions(text);

        // Convert to JSON
        String jsonOutput = toJson(transactions);
        System.out.println("\nStructured JSON Transactions:\n" + jsonOutput);
    }
}
