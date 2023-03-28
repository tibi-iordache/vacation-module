import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import VacationRequest from './vacation-request';
import VacationRequestDetail from './vacation-request-detail';

const VacationRequestRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<VacationRequest />} />
    <Route path=":id">
      <Route index element={<VacationRequestDetail />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VacationRequestRoutes;
