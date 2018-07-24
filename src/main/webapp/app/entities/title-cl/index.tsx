import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TitleCl from './title-cl';
import TitleClDetail from './title-cl-detail';
import TitleClUpdate from './title-cl-update';
import TitleClDeleteDialog from './title-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TitleClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TitleClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TitleClDetail} />
      <ErrorBoundaryRoute path={match.url} component={TitleCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={TitleClDeleteDialog} />
  </>
);

export default Routes;
