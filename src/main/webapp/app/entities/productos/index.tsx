import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Productos from './productos';
import ProductosDetail from './productos-detail';
import ProductosUpdate from './productos-update';
import ProductosDeleteDialog from './productos-delete-dialog';

const ProductosRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Productos />} />
    <Route path="new" element={<ProductosUpdate />} />
    <Route path=":id">
      <Route index element={<ProductosDetail />} />
      <Route path="edit" element={<ProductosUpdate />} />
      <Route path="delete" element={<ProductosDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProductosRoutes;
