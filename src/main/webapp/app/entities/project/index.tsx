import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Project from './project';
import ProjectDetail from './project-detail';

const ProjectRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Project />} />
    <Route path=":id">
      <Route index element={<ProjectDetail />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProjectRoutes;
