import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PersonageCl from './personage-cl';
import PersonageClDetail from './personage-cl-detail';
import PersonageClUpdate from './personage-cl-update';
import PersonageClDeleteDialog from './personage-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PersonageClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PersonageClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PersonageClDetail} />
      <ErrorBoundaryRoute path={match.url} component={PersonageCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PersonageClDeleteDialog} />
  </>
);

export default Routes;
