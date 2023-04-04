import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import NationalDay from './national-day';
import NationalDayDetail from './national-day-detail';
import NationalDayUpdate from './national-day-update';
import NationalDayDeleteDialog from './national-day-delete-dialog';

const NationalDayRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<NationalDay />} />
    <Route path="new" element={<NationalDayUpdate />} />
    <Route path=":id">
      <Route index element={<NationalDayDetail />} />
      <Route path="edit" element={<NationalDayUpdate />} />
      <Route path="delete" element={<NationalDayDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default NationalDayRoutes;
