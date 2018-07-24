import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AddressCl from './address-cl';
import AddressClDetail from './address-cl-detail';
import AddressClUpdate from './address-cl-update';
import AddressClDeleteDialog from './address-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AddressClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AddressClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AddressClDetail} />
      <ErrorBoundaryRoute path={match.url} component={AddressCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={AddressClDeleteDialog} />
  </>
);

export default Routes;
