package com.miapp.compra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miapp.compra.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
