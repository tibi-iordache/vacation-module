import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import NationalDay from './national-day';
import NationalDayDetail from './national-day-detail';

const NationalDayRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<NationalDay />} />
    <Route path=":id">
      <Route index element={<NationalDayDetail />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default NationalDayRoutes;
