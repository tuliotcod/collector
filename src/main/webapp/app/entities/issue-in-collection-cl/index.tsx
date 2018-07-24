import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import IssueInCollectionCl from './issue-in-collection-cl';
import IssueInCollectionClDetail from './issue-in-collection-cl-detail';
import IssueInCollectionClUpdate from './issue-in-collection-cl-update';
import IssueInCollectionClDeleteDialog from './issue-in-collection-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={IssueInCollectionClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={IssueInCollectionClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={IssueInCollectionClDetail} />
      <ErrorBoundaryRoute path={match.url} component={IssueInCollectionCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={IssueInCollectionClDeleteDialog} />
  </>
);

export default Routes;
