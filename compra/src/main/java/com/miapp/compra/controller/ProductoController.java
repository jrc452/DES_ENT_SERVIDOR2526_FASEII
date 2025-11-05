package com.miapp.compra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.miapp.compra.model.Producto;
import com.miapp.compra.repository.CategoriaRepository;
import com.miapp.compra.repository.ProductoRepository;

@Controller
@RequestMapping("/products")
public class ProductoController {
    @Autowired
    private ProductoRepository productoRepository;
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productoRepository.findAll());
        return "products/list";
    }

    @GetMapping("/new")
    public String nuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "products/new";
    }

    @PostMapping
    public String guardar(@ModelAttribute Producto producto) {
        productoRepository.save(producto);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String ediitar(@PathVariable Long id, Model model) {
        model.addAttribute("producto", productoRepository.findById(id).orElse(null));
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "products/edit";
    }

    @GetMapping("/remove/{id}")
    public String eliminar(@PathVariable Long id) {
        productoRepository.deleteById(id);
        return "redirect:/products";
    }

    @GetMapping("/view/{id}")
    public String ver(@PathVariable Long id, Model model) {
        model.addAttribute("producto", productoRepository.findById(id).orElse(null));
        return "products/view";
    }

}