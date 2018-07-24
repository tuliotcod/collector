import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CollectorUserCl from './collector-user-cl';
import CollectorUserClDetail from './collector-user-cl-detail';
import CollectorUserClUpdate from './collector-user-cl-update';
import CollectorUserClDeleteDialog from './collector-user-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CollectorUserClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CollectorUserClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CollectorUserClDetail} />
      <ErrorBoundaryRoute path={match.url} component={CollectorUserCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CollectorUserClDeleteDialog} />
  </>
);

export default Routes;
