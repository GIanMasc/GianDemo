import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IProductos } from 'app/shared/model/productos.model';
import { getEntity, updateEntity, createEntity, reset } from './productos.reducer';

export const ProductosUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const productosEntity = useAppSelector(state => state.productos.entity);
  const loading = useAppSelector(state => state.productos.loading);
  const updating = useAppSelector(state => state.productos.updating);
  const updateSuccess = useAppSelector(state => state.productos.updateSuccess);

  const handleClose = () => {
    navigate('/productos' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...productosEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...productosEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gianDemoApp.productos.home.createOrEditLabel" data-cy="ProductosCreateUpdateHeading">
            Crear o editar Productos
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="productos-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Nombre" id="productos-nombre" name="nombre" data-cy="nombre" type="text" />
              <ValidatedField label="Descripcion" id="productos-descripcion" name="descripcion" data-cy="descripcion" type="text" />
              <ValidatedField label="Precio" id="productos-precio" name="precio" data-cy="precio" type="text" />
              <ValidatedField label="Cantidad" id="productos-cantidad" name="cantidad" data-cy="cantidad" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/productos" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Volver</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Guardar
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ProductosUpdate;
