import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './productos.reducer';

export const ProductosDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productosEntity = useAppSelector(state => state.productos.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productosDetailsHeading">Productos</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{productosEntity.id}</dd>
          <dt>
            <span id="nombre">Nombre</span>
          </dt>
          <dd>{productosEntity.nombre}</dd>
          <dt>
            <span id="descripcion">Descripcion</span>
          </dt>
          <dd>{productosEntity.descripcion}</dd>
          <dt>
            <span id="precio">Precio</span>
          </dt>
          <dd>{productosEntity.precio}</dd>
          <dt>
            <span id="cantidad">Cantidad</span>
          </dt>
          <dd>{productosEntity.cantidad}</dd>
        </dl>
        <Button tag={Link} to="/productos" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Volver</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/productos/${productosEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editar</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductosDetail;
