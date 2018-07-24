import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ReadingStatusCl from './reading-status-cl';
import ReadingStatusClDetail from './reading-status-cl-detail';
import ReadingStatusClUpdate from './reading-status-cl-update';
import ReadingStatusClDeleteDialog from './reading-status-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ReadingStatusClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ReadingStatusClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ReadingStatusClDetail} />
      <ErrorBoundaryRoute path={match.url} component={ReadingStatusCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ReadingStatusClDeleteDialog} />
  </>
);

export default Routes;
