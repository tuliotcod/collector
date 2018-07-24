import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import LicensorCl from './licensor-cl';
import LicensorClDetail from './licensor-cl-detail';
import LicensorClUpdate from './licensor-cl-update';
import LicensorClDeleteDialog from './licensor-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LicensorClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LicensorClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LicensorClDetail} />
      <ErrorBoundaryRoute path={match.url} component={LicensorCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={LicensorClDeleteDialog} />
  </>
);

export default Routes;
