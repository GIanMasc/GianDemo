package com.gian.demo.web.rest;

import static com.gian.demo.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gian.demo.IntegrationTest;
import com.gian.demo.domain.Productos;
import com.gian.demo.repository.ProductosRepository;
import com.gian.demo.service.dto.ProductosDTO;
import com.gian.demo.service.mapper.ProductosMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProductosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductosResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRECIO = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRECIO = new BigDecimal(2);

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;

    private static final String ENTITY_API_URL = "/api/productos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductosRepository productosRepository;

    @Autowired
    private ProductosMapper productosMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductosMockMvc;

    private Productos productos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Productos createEntity(EntityManager em) {
        Productos productos = new Productos()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .precio(DEFAULT_PRECIO)
            .cantidad(DEFAULT_CANTIDAD);
        return productos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Productos createUpdatedEntity(EntityManager em) {
        Productos productos = new Productos()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precio(UPDATED_PRECIO)
            .cantidad(UPDATED_CANTIDAD);
        return productos;
    }

    @BeforeEach
    public void initTest() {
        productos = createEntity(em);
    }

    @Test
    @Transactional
    void createProductos() throws Exception {
        int databaseSizeBeforeCreate = productosRepository.findAll().size();
        // Create the Productos
        ProductosDTO productosDTO = productosMapper.toDto(productos);
        restProductosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productosDTO)))
            .andExpect(status().isCreated());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeCreate + 1);
        Productos testProductos = productosList.get(productosList.size() - 1);
        assertThat(testProductos.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testProductos.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testProductos.getPrecio()).isEqualByComparingTo(DEFAULT_PRECIO);
        assertThat(testProductos.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
    }

    @Test
    @Transactional
    void createProductosWithExistingId() throws Exception {
        // Create the Productos with an existing ID
        productos.setId(1L);
        ProductosDTO productosDTO = productosMapper.toDto(productos);

        int databaseSizeBeforeCreate = productosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productosDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductos() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get all the productosList
        restProductosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productos.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(sameNumber(DEFAULT_PRECIO))))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)));
    }

    @Test
    @Transactional
    void getProductos() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        // Get the productos
        restProductosMockMvc
            .perform(get(ENTITY_API_URL_ID, productos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productos.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.precio").value(sameNumber(DEFAULT_PRECIO)))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD));
    }

    @Test
    @Transactional
    void getNonExistingProductos() throws Exception {
        // Get the productos
        restProductosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductos() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        int databaseSizeBeforeUpdate = productosRepository.findAll().size();

        // Update the productos
        Productos updatedProductos = productosRepository.findById(productos.getId()).get();
        // Disconnect from session so that the updates on updatedProductos are not directly saved in db
        em.detach(updatedProductos);
        updatedProductos.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).precio(UPDATED_PRECIO).cantidad(UPDATED_CANTIDAD);
        ProductosDTO productosDTO = productosMapper.toDto(updatedProductos);

        restProductosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productosDTO))
            )
            .andExpect(status().isOk());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
        Productos testProductos = productosList.get(productosList.size() - 1);
        assertThat(testProductos.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testProductos.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testProductos.getPrecio()).isEqualByComparingTo(UPDATED_PRECIO);
        assertThat(testProductos.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void putNonExistingProductos() throws Exception {
        int databaseSizeBeforeUpdate = productosRepository.findAll().size();
        productos.setId(count.incrementAndGet());

        // Create the Productos
        ProductosDTO productosDTO = productosMapper.toDto(productos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductos() throws Exception {
        int databaseSizeBeforeUpdate = productosRepository.findAll().size();
        productos.setId(count.incrementAndGet());

        // Create the Productos
        ProductosDTO productosDTO = productosMapper.toDto(productos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductos() throws Exception {
        int databaseSizeBeforeUpdate = productosRepository.findAll().size();
        productos.setId(count.incrementAndGet());

        // Create the Productos
        ProductosDTO productosDTO = productosMapper.toDto(productos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productosDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductosWithPatch() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        int databaseSizeBeforeUpdate = productosRepository.findAll().size();

        // Update the productos using partial update
        Productos partialUpdatedProductos = new Productos();
        partialUpdatedProductos.setId(productos.getId());

        partialUpdatedProductos.nombre(UPDATED_NOMBRE).cantidad(UPDATED_CANTIDAD);

        restProductosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductos))
            )
            .andExpect(status().isOk());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
        Productos testProductos = productosList.get(productosList.size() - 1);
        assertThat(testProductos.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testProductos.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testProductos.getPrecio()).isEqualByComparingTo(DEFAULT_PRECIO);
        assertThat(testProductos.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void fullUpdateProductosWithPatch() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        int databaseSizeBeforeUpdate = productosRepository.findAll().size();

        // Update the productos using partial update
        Productos partialUpdatedProductos = new Productos();
        partialUpdatedProductos.setId(productos.getId());

        partialUpdatedProductos.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).precio(UPDATED_PRECIO).cantidad(UPDATED_CANTIDAD);

        restProductosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductos))
            )
            .andExpect(status().isOk());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
        Productos testProductos = productosList.get(productosList.size() - 1);
        assertThat(testProductos.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testProductos.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testProductos.getPrecio()).isEqualByComparingTo(UPDATED_PRECIO);
        assertThat(testProductos.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void patchNonExistingProductos() throws Exception {
        int databaseSizeBeforeUpdate = productosRepository.findAll().size();
        productos.setId(count.incrementAndGet());

        // Create the Productos
        ProductosDTO productosDTO = productosMapper.toDto(productos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productosDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductos() throws Exception {
        int databaseSizeBeforeUpdate = productosRepository.findAll().size();
        productos.setId(count.incrementAndGet());

        // Create the Productos
        ProductosDTO productosDTO = productosMapper.toDto(productos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductos() throws Exception {
        int databaseSizeBeforeUpdate = productosRepository.findAll().size();
        productos.setId(count.incrementAndGet());

        // Create the Productos
        ProductosDTO productosDTO = productosMapper.toDto(productos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductosMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productosDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Productos in the database
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductos() throws Exception {
        // Initialize the database
        productosRepository.saveAndFlush(productos);

        int databaseSizeBeforeDelete = productosRepository.findAll().size();

        // Delete the productos
        restProductosMockMvc
            .perform(delete(ENTITY_API_URL_ID, productos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Productos> productosList = productosRepository.findAll();
        assertThat(productosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
