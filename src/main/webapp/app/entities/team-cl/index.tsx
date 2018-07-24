import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TeamCl from './team-cl';
import TeamClDetail from './team-cl-detail';
import TeamClUpdate from './team-cl-update';
import TeamClDeleteDialog from './team-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TeamClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TeamClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TeamClDetail} />
      <ErrorBoundaryRoute path={match.url} component={TeamCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={TeamClDeleteDialog} />
  </>
);

export default Routes;
