import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import IssueInWishlistCl from './issue-in-wishlist-cl';
import IssueInWishlistClDetail from './issue-in-wishlist-cl-detail';
import IssueInWishlistClUpdate from './issue-in-wishlist-cl-update';
import IssueInWishlistClDeleteDialog from './issue-in-wishlist-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={IssueInWishlistClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={IssueInWishlistClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={IssueInWishlistClDetail} />
      <ErrorBoundaryRoute path={match.url} component={IssueInWishlistCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={IssueInWishlistClDeleteDialog} />
  </>
);

export default Routes;
