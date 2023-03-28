import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Project from './project';
import Team from './team';
import NationalDay from './national-day';
import VacationRequest from './vacation-request';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="project/*" element={<Project />} />
        <Route path="team/*" element={<Team />} />
        <Route path="national-day/*" element={<NationalDay />} />
        <Route path="vacation-request/*" element={<VacationRequest />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
