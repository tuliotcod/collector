import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ContributionTypeCl from './contribution-type-cl';
import ContributionTypeClDetail from './contribution-type-cl-detail';
import ContributionTypeClUpdate from './contribution-type-cl-update';
import ContributionTypeClDeleteDialog from './contribution-type-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ContributionTypeClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ContributionTypeClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ContributionTypeClDetail} />
      <ErrorBoundaryRoute path={match.url} component={ContributionTypeCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ContributionTypeClDeleteDialog} />
  </>
);

export default Routes;
