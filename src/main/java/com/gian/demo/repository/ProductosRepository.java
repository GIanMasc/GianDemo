package com.gian.demo.repository;

import com.gian.demo.domain.Productos;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Productos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductosRepository extends JpaRepository<Productos, Long> {}
