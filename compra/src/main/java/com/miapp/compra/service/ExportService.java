package com.miapp.compra.service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.miapp.compra.model.Categoria;
import com.miapp.compra.model.Producto;

@Service
public class ExportService {
    // CATEGORÍAS
    public byte[] exportCategoriasCsv(List<Categoria> datos) {
        StringBuilder sb = new StringBuilder("id;nombre;descripcion\n");
        for (Categoria c : datos) {
            sb.append(c.getId()).append(";")
                    .append(nullSafe(c.getNombre())).append(";")
                    .append(nullSafe(c.getDescripcion())).append(";");
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    public byte[] exportCategoriasPdf(List<Categoria> datos) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, baos);
            doc.open();
            doc.add(new Paragraph("Listado de Categorías"));
            doc.add(new Paragraph(" "));
            PdfPTable table = new PdfPTable(5);
            table.addCell("ID");
            table.addCell("Nombre");
            table.addCell("Descripción");
            for (Categoria c : datos) {
                table.addCell(String.valueOf(c.getId()));
                table.addCell(nullSafe(c.getNombre()));
                table.addCell(nullSafe(c.getDescripcion()));
            }
            doc.add(table);
            doc.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error creando PDF", e);
        }
    }

    // PRODUCTOS
    public byte[] exportProductosCsv(List<Producto> datos) {
        StringBuilder sb = new StringBuilder("id;nombre;descripcion\n");
        for (Producto p : datos) {
            sb.append(p.getId()).append(";")
                    .append(nullSafe(p.getNombre())).append(";")
                    .append(nullSafe(p.getDescripcion())).append(";");
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    public byte[] exportProductosPdf(List<Producto> datos) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, baos);
            doc.open();
            doc.add(new Paragraph("Listado de Productos"));
            doc.add(new Paragraph(" "));
            PdfPTable table = new PdfPTable(5);
            table.addCell("ID");
            table.addCell("Nombre");
            table.addCell("Descripción");
            for (Producto p : datos) {
                table.addCell(String.valueOf(p.getId()));
                table.addCell(nullSafe(p.getNombre()));
                table.addCell(nullSafe(p.getDescripcion()));
            }
            doc.add(table);
            doc.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error creando PDF", e);
        }
    }

    private String nullSafe(String s) {
        return s == null ? "" : s;
    }
}
