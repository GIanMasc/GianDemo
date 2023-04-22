package com.gian.demo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductosMapperTest {

    private ProductosMapper productosMapper;

    @BeforeEach
    public void setUp() {
        productosMapper = new ProductosMapperImpl();
    }
}
