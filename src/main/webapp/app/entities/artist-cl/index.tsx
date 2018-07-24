import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ArtistCl from './artist-cl';
import ArtistClDetail from './artist-cl-detail';
import ArtistClUpdate from './artist-cl-update';
import ArtistClDeleteDialog from './artist-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ArtistClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ArtistClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ArtistClDetail} />
      <ErrorBoundaryRoute path={match.url} component={ArtistCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ArtistClDeleteDialog} />
  </>
);

export default Routes;
