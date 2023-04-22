package com.gian.demo.service.mapper;

import com.gian.demo.domain.Productos;
import com.gian.demo.service.dto.ProductosDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Productos} and its DTO {@link ProductosDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductosMapper extends EntityMapper<ProductosDTO, Productos> {}
