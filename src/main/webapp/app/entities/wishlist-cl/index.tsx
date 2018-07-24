import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import WishlistCl from './wishlist-cl';
import WishlistClDetail from './wishlist-cl-detail';
import WishlistClUpdate from './wishlist-cl-update';
import WishlistClDeleteDialog from './wishlist-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WishlistClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WishlistClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WishlistClDetail} />
      <ErrorBoundaryRoute path={match.url} component={WishlistCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={WishlistClDeleteDialog} />
  </>
);

export default Routes;
