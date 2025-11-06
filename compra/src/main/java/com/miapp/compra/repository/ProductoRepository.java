package com.miapp.compra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miapp.compra.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /*
     * void deleteByStockLessThan(int cantidad);
     * 
     * void deleteByCategoriaIdCategoria(Long idCategoria);
     * List<Producto> findByPrecioBetween(double min, double max);
     * 
     * @Query("SELECT p FROM Producto p WHERE p.categoria.idCategoria = :idCategoria AND p.stock > 0"
     * )
     * List<Producto> findDisponiblesPorCategoria(Long idCategoria);
     */

}
