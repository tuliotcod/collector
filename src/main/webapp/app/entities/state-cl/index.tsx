import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import StateCl from './state-cl';
import StateClDetail from './state-cl-detail';
import StateClUpdate from './state-cl-update';
import StateClDeleteDialog from './state-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={StateClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={StateClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={StateClDetail} />
      <ErrorBoundaryRoute path={match.url} component={StateCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={StateClDeleteDialog} />
  </>
);

export default Routes;
