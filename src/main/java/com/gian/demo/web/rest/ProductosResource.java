package com.gian.demo.web.rest;

import com.gian.demo.repository.ProductosRepository;
import com.gian.demo.service.ProductosService;
import com.gian.demo.service.dto.ProductosDTO;
import com.gian.demo.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.gian.demo.domain.Productos}.
 */
@RestController
@RequestMapping("/api")
public class ProductosResource {

    private final Logger log = LoggerFactory.getLogger(ProductosResource.class);

    private static final String ENTITY_NAME = "productos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductosService productosService;

    private final ProductosRepository productosRepository;

    public ProductosResource(ProductosService productosService, ProductosRepository productosRepository) {
        this.productosService = productosService;
        this.productosRepository = productosRepository;
    }

    /**
     * {@code POST  /productos} : Create a new productos.
     *
     * @param productosDTO the productosDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productosDTO, or with status {@code 400 (Bad Request)} if the productos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/productos")
    public ResponseEntity<ProductosDTO> createProductos(@RequestBody ProductosDTO productosDTO) throws URISyntaxException {
        log.debug("REST request to save Productos : {}", productosDTO);
        if (productosDTO.getId() != null) {
            throw new BadRequestAlertException("A new productos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductosDTO result = productosService.save(productosDTO);
        return ResponseEntity
            .created(new URI("/api/productos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /productos/:id} : Updates an existing productos.
     *
     * @param id the id of the productosDTO to save.
     * @param productosDTO the productosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productosDTO,
     * or with status {@code 400 (Bad Request)} if the productosDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/productos/{id}")
    public ResponseEntity<ProductosDTO> updateProductos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductosDTO productosDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Productos : {}, {}", id, productosDTO);
        if (productosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductosDTO result = productosService.update(productosDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productosDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /productos/:id} : Partial updates given fields of an existing productos, field will ignore if it is null
     *
     * @param id the id of the productosDTO to save.
     * @param productosDTO the productosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productosDTO,
     * or with status {@code 400 (Bad Request)} if the productosDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productosDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/productos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductosDTO> partialUpdateProductos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductosDTO productosDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Productos partially : {}, {}", id, productosDTO);
        if (productosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductosDTO> result = productosService.partialUpdate(productosDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productosDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /productos} : get all the productos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productos in body.
     */
    @GetMapping("/productos")
    public ResponseEntity<List<ProductosDTO>> getAllProductos(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Productos");
        Page<ProductosDTO> page = productosService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /productos/:id} : get the "id" productos.
     *
     * @param id the id of the productosDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productosDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/productos/{id}")
    public ResponseEntity<ProductosDTO> getProductos(@PathVariable Long id) {
        log.debug("REST request to get Productos : {}", id);
        Optional<ProductosDTO> productosDTO = productosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productosDTO);
    }

    /**
     * {@code DELETE  /productos/:id} : delete the "id" productos.
     *
     * @param id the id of the productosDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> deleteProductos(@PathVariable Long id) {
        log.debug("REST request to delete Productos : {}", id);
        productosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
