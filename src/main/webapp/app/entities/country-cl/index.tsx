import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CountryCl from './country-cl';
import CountryClDetail from './country-cl-detail';
import CountryClUpdate from './country-cl-update';
import CountryClDeleteDialog from './country-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CountryClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CountryClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CountryClDetail} />
      <ErrorBoundaryRoute path={match.url} component={CountryCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CountryClDeleteDialog} />
  </>
);

export default Routes;
