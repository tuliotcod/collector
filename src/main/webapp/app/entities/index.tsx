import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CollectorUserCl from './collector-user-cl';
import AddressCl from './address-cl';
import CountryCl from './country-cl';
import StateCl from './state-cl';
import PublisherCl from './publisher-cl';
import LicensorCl from './licensor-cl';
import CategoryCl from './category-cl';
import GenreCl from './genre-cl';
import StatusCl from './status-cl';
import TitleCl from './title-cl';
import FormatCl from './format-cl';
import FinishingCl from './finishing-cl';
import CurrencyCl from './currency-cl';
import ActionCl from './action-cl';
import ContributionTypeCl from './contribution-type-cl';
import ContributionCl from './contribution-cl';
import IssueCl from './issue-cl';
import CommentCl from './comment-cl';
import ArcCl from './arc-cl';
import TeamCl from './team-cl';
import PersonageCl from './personage-cl';
import HistoryCl from './history-cl';
import ArtistCl from './artist-cl';
import RoleCl from './role-cl';
import CollaboratorCl from './collaborator-cl';
import IssueStatusCl from './issue-status-cl';
import ReadingStatusCl from './reading-status-cl';
import IssueInCollectionCl from './issue-in-collection-cl';
import IssueInWishlistCl from './issue-in-wishlist-cl';
import WishlistCl from './wishlist-cl';
import CollectionCl from './collection-cl';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/collector-user-cl`} component={CollectorUserCl} />
      <ErrorBoundaryRoute path={`${match.url}/address-cl`} component={AddressCl} />
      <ErrorBoundaryRoute path={`${match.url}/country-cl`} component={CountryCl} />
      <ErrorBoundaryRoute path={`${match.url}/state-cl`} component={StateCl} />
      <ErrorBoundaryRoute path={`${match.url}/publisher-cl`} component={PublisherCl} />
      <ErrorBoundaryRoute path={`${match.url}/licensor-cl`} component={LicensorCl} />
      <ErrorBoundaryRoute path={`${match.url}/category-cl`} component={CategoryCl} />
      <ErrorBoundaryRoute path={`${match.url}/genre-cl`} component={GenreCl} />
      <ErrorBoundaryRoute path={`${match.url}/status-cl`} component={StatusCl} />
      <ErrorBoundaryRoute path={`${match.url}/title-cl`} component={TitleCl} />
      <ErrorBoundaryRoute path={`${match.url}/format-cl`} component={FormatCl} />
      <ErrorBoundaryRoute path={`${match.url}/finishing-cl`} component={FinishingCl} />
      <ErrorBoundaryRoute path={`${match.url}/currency-cl`} component={CurrencyCl} />
      <ErrorBoundaryRoute path={`${match.url}/action-cl`} component={ActionCl} />
      <ErrorBoundaryRoute path={`${match.url}/contribution-type-cl`} component={ContributionTypeCl} />
      <ErrorBoundaryRoute path={`${match.url}/contribution-cl`} component={ContributionCl} />
      <ErrorBoundaryRoute path={`${match.url}/issue-cl`} component={IssueCl} />
      <ErrorBoundaryRoute path={`${match.url}/comment-cl`} component={CommentCl} />
      <ErrorBoundaryRoute path={`${match.url}/arc-cl`} component={ArcCl} />
      <ErrorBoundaryRoute path={`${match.url}/team-cl`} component={TeamCl} />
      <ErrorBoundaryRoute path={`${match.url}/personage-cl`} component={PersonageCl} />
      <ErrorBoundaryRoute path={`${match.url}/history-cl`} component={HistoryCl} />
      <ErrorBoundaryRoute path={`${match.url}/artist-cl`} component={ArtistCl} />
      <ErrorBoundaryRoute path={`${match.url}/role-cl`} component={RoleCl} />
      <ErrorBoundaryRoute path={`${match.url}/collaborator-cl`} component={CollaboratorCl} />
      <ErrorBoundaryRoute path={`${match.url}/issue-status-cl`} component={IssueStatusCl} />
      <ErrorBoundaryRoute path={`${match.url}/reading-status-cl`} component={ReadingStatusCl} />
      <ErrorBoundaryRoute path={`${match.url}/issue-in-collection-cl`} component={IssueInCollectionCl} />
      <ErrorBoundaryRoute path={`${match.url}/issue-in-wishlist-cl`} component={IssueInWishlistCl} />
      <ErrorBoundaryRoute path={`${match.url}/wishlist-cl`} component={WishlistCl} />
      <ErrorBoundaryRoute path={`${match.url}/collection-cl`} component={CollectionCl} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;
