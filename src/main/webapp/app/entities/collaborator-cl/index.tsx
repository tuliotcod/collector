import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CollaboratorCl from './collaborator-cl';
import CollaboratorClDetail from './collaborator-cl-detail';
import CollaboratorClUpdate from './collaborator-cl-update';
import CollaboratorClDeleteDialog from './collaborator-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CollaboratorClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CollaboratorClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CollaboratorClDetail} />
      <ErrorBoundaryRoute path={match.url} component={CollaboratorCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CollaboratorClDeleteDialog} />
  </>
);

export default Routes;
