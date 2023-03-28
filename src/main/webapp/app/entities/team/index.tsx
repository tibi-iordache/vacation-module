import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Team from './team';
import TeamDetail from './team-detail';

const TeamRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Team />} />
    <Route path=":id">
      <Route index element={<TeamDetail />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TeamRoutes;
