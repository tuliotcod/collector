import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ContributionCl from './contribution-cl';
import ContributionClDetail from './contribution-cl-detail';
import ContributionClUpdate from './contribution-cl-update';
import ContributionClDeleteDialog from './contribution-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ContributionClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ContributionClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ContributionClDetail} />
      <ErrorBoundaryRoute path={match.url} component={ContributionCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ContributionClDeleteDialog} />
  </>
);

export default Routes;
