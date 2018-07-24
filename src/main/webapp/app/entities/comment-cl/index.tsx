import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CommentCl from './comment-cl';
import CommentClDetail from './comment-cl-detail';
import CommentClUpdate from './comment-cl-update';
import CommentClDeleteDialog from './comment-cl-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CommentClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CommentClUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CommentClDetail} />
      <ErrorBoundaryRoute path={match.url} component={CommentCl} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CommentClDeleteDialog} />
  </>
);

export default Routes;
