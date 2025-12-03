package com.miapp.compra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.miapp.compra.model.Categoria;
import com.miapp.compra.repository.CategoriaRepository;

@Controller
@RequestMapping("/categories")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "categories/list";
    }

    @GetMapping("/new")
    public String nuevo(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categories/new";
    }

    @PostMapping
    public String guardar(@ModelAttribute Categoria c) {
        categoriaRepository.save(c);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("categoria", categoriaRepository.findById(id).orElse(null));
        return "categories/edit";
    }

    @GetMapping("/remove/{id}")
    public String eliminar(@PathVariable Long id) {
        categoriaRepository.deleteById(id);
        return "redirect:/categories";
    }

    @GetMapping("/view/{id}")
    public String ver(@PathVariable Long id, Model model) {
        model.addAttribute("categoria", categoriaRepository.findById(id).orElse(null));
        return "categories/view";
    }

}
