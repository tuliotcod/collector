import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RoleCl from './role-cl';
import RoleClDetail from './role-cl-detail';
import RoleClUpdate from './role-cl-update';
import RoleClDeleteDialog from './role-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RoleClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RoleClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RoleClDetail} />
      <ErrorBoundaryRoute path={match.url} component={RoleCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RoleClDeleteDialog} />
  </>
);

export default Routes;
