package com.miapp.compra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miapp.compra.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /*
     * List<Categoria> findByNombreContainingAndDescripcionContaining(String nombre,
     * String descripcion);
     * 
     * List<Categoria> findByDescripcionContainingIgnoreCase(String descripcion);
     * 
     * List<Categoria> findAllByOrderByNombreAsc();
     * Long countByNombreContaining(String texto);
     * 
     * Categoria findTopByOrderByIdCategoriaDesc();
     * 
     * @Query("SELECT c FROM Categoria c WHERE SIZE(c.productos) > :minimo")
     * 
     * List<Categoria> findByMinimoProductos(int minimo);
     */

}
