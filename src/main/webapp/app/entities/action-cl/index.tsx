import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ActionCl from './action-cl';
import ActionClDetail from './action-cl-detail';
import ActionClUpdate from './action-cl-update';
import ActionClDeleteDialog from './action-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ActionClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ActionClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ActionClDetail} />
      <ErrorBoundaryRoute path={match.url} component={ActionCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ActionClDeleteDialog} />
  </>
);

export default Routes;
