import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import IssueCl from './issue-cl';
import IssueClDetail from './issue-cl-detail';
import IssueClUpdate from './issue-cl-update';
import IssueClDeleteDialog from './issue-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={IssueClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={IssueClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={IssueClDetail} />
      <ErrorBoundaryRoute path={match.url} component={IssueCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={IssueClDeleteDialog} />
  </>
);

export default Routes;
