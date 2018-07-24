import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CategoryCl from './category-cl';
import CategoryClDetail from './category-cl-detail';
import CategoryClUpdate from './category-cl-update';
import CategoryClDeleteDialog from './category-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CategoryClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CategoryClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CategoryClDetail} />
      <ErrorBoundaryRoute path={match.url} component={CategoryCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CategoryClDeleteDialog} />
  </>
);

export default Routes;
