import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CollectionCl from './collection-cl';
import CollectionClDetail from './collection-cl-detail';
import CollectionClUpdate from './collection-cl-update';
import CollectionClDeleteDialog from './collection-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CollectionClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CollectionClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CollectionClDetail} />
      <ErrorBoundaryRoute path={match.url} component={CollectionCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CollectionClDeleteDialog} />
  </>
);

export default Routes;
