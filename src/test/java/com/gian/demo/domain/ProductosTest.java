package com.gian.demo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gian.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Productos.class);
        Productos productos1 = new Productos();
        productos1.setId(1L);
        Productos productos2 = new Productos();
        productos2.setId(productos1.getId());
        assertThat(productos1).isEqualTo(productos2);
        productos2.setId(2L);
        assertThat(productos1).isNotEqualTo(productos2);
        productos1.setId(null);
        assertThat(productos1).isNotEqualTo(productos2);
    }
}
