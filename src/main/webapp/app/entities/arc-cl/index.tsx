import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ArcCl from './arc-cl';
import ArcClDetail from './arc-cl-detail';
import ArcClUpdate from './arc-cl-update';
import ArcClDeleteDialog from './arc-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ArcClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ArcClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ArcClDetail} />
      <ErrorBoundaryRoute path={match.url} component={ArcCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ArcClDeleteDialog} />
  </>
);

export default Routes;
