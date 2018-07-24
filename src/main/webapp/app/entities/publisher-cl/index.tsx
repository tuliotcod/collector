import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PublisherCl from './publisher-cl';
import PublisherClDetail from './publisher-cl-detail';
import PublisherClUpdate from './publisher-cl-update';
import PublisherClDeleteDialog from './publisher-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PublisherClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PublisherClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PublisherClDetail} />
      <ErrorBoundaryRoute path={match.url} component={PublisherCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PublisherClDeleteDialog} />
  </>
);

export default Routes;
