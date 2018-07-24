import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GenreCl from './genre-cl';
import GenreClDetail from './genre-cl-detail';
import GenreClUpdate from './genre-cl-update';
import GenreClDeleteDialog from './genre-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GenreClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GenreClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GenreClDetail} />
      <ErrorBoundaryRoute path={match.url} component={GenreCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={GenreClDeleteDialog} />
  </>
);

export default Routes;
