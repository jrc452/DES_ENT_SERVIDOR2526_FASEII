package com.miapp.compra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.miapp.compra.repository.CategoriaRepository;
import com.miapp.compra.repository.ProductoRepository;
import com.miapp.compra.service.ExportService;

@Controller
public class ExportController {
    @Autowired
    private ExportService exportService;
    @Autowired
    private CategoriaRepository categoriaRepo;
    @Autowired
    private ProductoRepository productoRepo;

    // CATEGORÍAS
    @GetMapping("categories/export/csv")
    public ResponseEntity<byte[]> exportCategoriasCsv() {
        byte[] data = exportService.exportCategoriasCsv(categoriaRepo.findAll());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=categorias.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(data);
    }

    @GetMapping("categories/export/pdf")
    public ResponseEntity<byte[]> exportCategoriasPdf() {
        byte[] data = exportService.exportCategoriasPdf(categoriaRepo.findAll());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=categorias.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(data);
    }

    // PRODUCTOS
    @GetMapping("products/export/csv")
    public ResponseEntity<byte[]> exportProductosCsv() {
        byte[] data = exportService.exportProductosCsv(productoRepo.findAll());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=productos.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(data);
    }

    @GetMapping("products/export/pdf")
    public ResponseEntity<byte[]> exportProductosPdf() {
        byte[] data = exportService.exportProductosPdf(productoRepo.findAll());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=productos.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(data);
    }
}