import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FinishingCl from './finishing-cl';
import FinishingClDetail from './finishing-cl-detail';
import FinishingClUpdate from './finishing-cl-update';
import FinishingClDeleteDialog from './finishing-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FinishingClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FinishingClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FinishingClDetail} />
      <ErrorBoundaryRoute path={match.url} component={FinishingCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={FinishingClDeleteDialog} />
  </>
);

export default Routes;
