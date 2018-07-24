import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import IssueStatusCl from './issue-status-cl';
import IssueStatusClDetail from './issue-status-cl-detail';
import IssueStatusClUpdate from './issue-status-cl-update';
import IssueStatusClDeleteDialog from './issue-status-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={IssueStatusClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={IssueStatusClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={IssueStatusClDetail} />
      <ErrorBoundaryRoute path={match.url} component={IssueStatusCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={IssueStatusClDeleteDialog} />
  </>
);

export default Routes;
