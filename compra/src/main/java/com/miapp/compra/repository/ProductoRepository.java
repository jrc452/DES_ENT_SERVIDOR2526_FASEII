package com.miapp.compra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miapp.compra.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

}
