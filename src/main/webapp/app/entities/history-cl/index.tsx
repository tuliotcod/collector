import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import HistoryCl from './history-cl';
import HistoryClDetail from './history-cl-detail';
import HistoryClUpdate from './history-cl-update';
import HistoryClDeleteDialog from './history-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HistoryClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HistoryClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HistoryClDetail} />
      <ErrorBoundaryRoute path={match.url} component={HistoryCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={HistoryClDeleteDialog} />
  </>
);

export default Routes;
