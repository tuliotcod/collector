import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FormatCl from './format-cl';
import FormatClDetail from './format-cl-detail';
import FormatClUpdate from './format-cl-update';
import FormatClDeleteDialog from './format-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FormatClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FormatClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FormatClDetail} />
      <ErrorBoundaryRoute path={match.url} component={FormatCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={FormatClDeleteDialog} />
  </>
);

export default Routes;
