package com.gian.demo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gian.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductosDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductosDTO.class);
        ProductosDTO productosDTO1 = new ProductosDTO();
        productosDTO1.setId(1L);
        ProductosDTO productosDTO2 = new ProductosDTO();
        assertThat(productosDTO1).isNotEqualTo(productosDTO2);
        productosDTO2.setId(productosDTO1.getId());
        assertThat(productosDTO1).isEqualTo(productosDTO2);
        productosDTO2.setId(2L);
        assertThat(productosDTO1).isNotEqualTo(productosDTO2);
        productosDTO1.setId(null);
        assertThat(productosDTO1).isNotEqualTo(productosDTO2);
    }
}
