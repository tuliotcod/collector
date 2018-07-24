import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import StatusCl from './status-cl';
import StatusClDetail from './status-cl-detail';
import StatusClUpdate from './status-cl-update';
import StatusClDeleteDialog from './status-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={StatusClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={StatusClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={StatusClDetail} />
      <ErrorBoundaryRoute path={match.url} component={StatusCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={StatusClDeleteDialog} />
  </>
);

export default Routes;
