package com.gian.demo.service;

import com.gian.demo.domain.Productos;
import com.gian.demo.repository.ProductosRepository;
import com.gian.demo.service.dto.ProductosDTO;
import com.gian.demo.service.mapper.ProductosMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Productos}.
 */
@Service
@Transactional
public class ProductosService {

    private final Logger log = LoggerFactory.getLogger(ProductosService.class);

    private final ProductosRepository productosRepository;

    private final ProductosMapper productosMapper;

    public ProductosService(ProductosRepository productosRepository, ProductosMapper productosMapper) {
        this.productosRepository = productosRepository;
        this.productosMapper = productosMapper;
    }

    /**
     * Save a productos.
     *
     * @param productosDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductosDTO save(ProductosDTO productosDTO) {
        log.debug("Request to save Productos : {}", productosDTO);
        Productos productos = productosMapper.toEntity(productosDTO);
        productos = productosRepository.save(productos);
        return productosMapper.toDto(productos);
    }

    /**
     * Update a productos.
     *
     * @param productosDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductosDTO update(ProductosDTO productosDTO) {
        log.debug("Request to update Productos : {}", productosDTO);
        Productos productos = productosMapper.toEntity(productosDTO);
        productos = productosRepository.save(productos);
        return productosMapper.toDto(productos);
    }

    /**
     * Partially update a productos.
     *
     * @param productosDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductosDTO> partialUpdate(ProductosDTO productosDTO) {
        log.debug("Request to partially update Productos : {}", productosDTO);

        return productosRepository
            .findById(productosDTO.getId())
            .map(existingProductos -> {
                productosMapper.partialUpdate(existingProductos, productosDTO);

                return existingProductos;
            })
            .map(productosRepository::save)
            .map(productosMapper::toDto);
    }

    /**
     * Get all the productos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductosDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Productos");
        return productosRepository.findAll(pageable).map(productosMapper::toDto);
    }

    /**
     * Get one productos by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductosDTO> findOne(Long id) {
        log.debug("Request to get Productos : {}", id);
        return productosRepository.findById(id).map(productosMapper::toDto);
    }

    /**
     * Delete the productos by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Productos : {}", id);
        productosRepository.deleteById(id);
    }
}
