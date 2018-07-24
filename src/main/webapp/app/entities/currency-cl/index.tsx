import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CurrencyCl from './currency-cl';
import CurrencyClDetail from './currency-cl-detail';
import CurrencyClUpdate from './currency-cl-update';
import CurrencyClDeleteDialog from './currency-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CurrencyClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CurrencyClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CurrencyClDetail} />
      <ErrorBoundaryRoute path={match.url} component={CurrencyCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CurrencyClDeleteDialog} />
  </>
);

export default Routes;
