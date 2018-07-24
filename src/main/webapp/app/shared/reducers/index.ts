import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import collectorUser, {
  CollectorUserClState
} from 'app/entities/collector-user-cl/collector-user-cl.reducer';
// prettier-ignore
import address, {
  AddressClState
} from 'app/entities/address-cl/address-cl.reducer';
// prettier-ignore
import country, {
  CountryClState
} from 'app/entities/country-cl/country-cl.reducer';
// prettier-ignore
import state, {
  StateClState
} from 'app/entities/state-cl/state-cl.reducer';
// prettier-ignore
import publisher, {
  PublisherClState
} from 'app/entities/publisher-cl/publisher-cl.reducer';
// prettier-ignore
import licensor, {
  LicensorClState
} from 'app/entities/licensor-cl/licensor-cl.reducer';
// prettier-ignore
import category, {
  CategoryClState
} from 'app/entities/category-cl/category-cl.reducer';
// prettier-ignore
import genre, {
  GenreClState
} from 'app/entities/genre-cl/genre-cl.reducer';
// prettier-ignore
import status, {
  StatusClState
} from 'app/entities/status-cl/status-cl.reducer';
// prettier-ignore
import title, {
  TitleClState
} from 'app/entities/title-cl/title-cl.reducer';
// prettier-ignore
import format, {
  FormatClState
} from 'app/entities/format-cl/format-cl.reducer';
// prettier-ignore
import finishing, {
  FinishingClState
} from 'app/entities/finishing-cl/finishing-cl.reducer';
// prettier-ignore
import currency, {
  CurrencyClState
} from 'app/entities/currency-cl/currency-cl.reducer';
// prettier-ignore
import action, {
  ActionClState
} from 'app/entities/action-cl/action-cl.reducer';
// prettier-ignore
import contributionType, {
  ContributionTypeClState
} from 'app/entities/contribution-type-cl/contribution-type-cl.reducer';
// prettier-ignore
import contribution, {
  ContributionClState
} from 'app/entities/contribution-cl/contribution-cl.reducer';
// prettier-ignore
import issue, {
  IssueClState
} from 'app/entities/issue-cl/issue-cl.reducer';
// prettier-ignore
import comment, {
  CommentClState
} from 'app/entities/comment-cl/comment-cl.reducer';
// prettier-ignore
import arc, {
  ArcClState
} from 'app/entities/arc-cl/arc-cl.reducer';
// prettier-ignore
import team, {
  TeamClState
} from 'app/entities/team-cl/team-cl.reducer';
// prettier-ignore
import personage, {
  PersonageClState
} from 'app/entities/personage-cl/personage-cl.reducer';
// prettier-ignore
import history, {
  HistoryClState
} from 'app/entities/history-cl/history-cl.reducer';
// prettier-ignore
import artist, {
  ArtistClState
} from 'app/entities/artist-cl/artist-cl.reducer';
// prettier-ignore
import role, {
  RoleClState
} from 'app/entities/role-cl/role-cl.reducer';
// prettier-ignore
import collaborator, {
  CollaboratorClState
} from 'app/entities/collaborator-cl/collaborator-cl.reducer';
// prettier-ignore
import issueStatus, {
  IssueStatusClState
} from 'app/entities/issue-status-cl/issue-status-cl.reducer';
// prettier-ignore
import readingStatus, {
  ReadingStatusClState
} from 'app/entities/reading-status-cl/reading-status-cl.reducer';
// prettier-ignore
import issueInCollection, {
  IssueInCollectionClState
} from 'app/entities/issue-in-collection-cl/issue-in-collection-cl.reducer';
// prettier-ignore
import issueInWishlist, {
  IssueInWishlistClState
} from 'app/entities/issue-in-wishlist-cl/issue-in-wishlist-cl.reducer';
// prettier-ignore
import wishlist, {
  WishlistClState
} from 'app/entities/wishlist-cl/wishlist-cl.reducer';
// prettier-ignore
import collection, {
  CollectionClState
} from 'app/entities/collection-cl/collection-cl.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly collectorUser: CollectorUserClState;
  readonly address: AddressClState;
  readonly country: CountryClState;
  readonly state: StateClState;
  readonly publisher: PublisherClState;
  readonly licensor: LicensorClState;
  readonly category: CategoryClState;
  readonly genre: GenreClState;
  readonly status: StatusClState;
  readonly title: TitleClState;
  readonly format: FormatClState;
  readonly finishing: FinishingClState;
  readonly currency: CurrencyClState;
  readonly action: ActionClState;
  readonly contributionType: ContributionTypeClState;
  readonly contribution: ContributionClState;
  readonly issue: IssueClState;
  readonly comment: CommentClState;
  readonly arc: ArcClState;
  readonly team: TeamClState;
  readonly personage: PersonageClState;
  readonly history: HistoryClState;
  readonly artist: ArtistClState;
  readonly role: RoleClState;
  readonly collaborator: CollaboratorClState;
  readonly issueStatus: IssueStatusClState;
  readonly readingStatus: ReadingStatusClState;
  readonly issueInCollection: IssueInCollectionClState;
  readonly issueInWishlist: IssueInWishlistClState;
  readonly wishlist: WishlistClState;
  readonly collection: CollectionClState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  collectorUser,
  address,
  country,
  state,
  publisher,
  licensor,
  category,
  genre,
  status,
  title,
  format,
  finishing,
  currency,
  action,
  contributionType,
  contribution,
  issue,
  comment,
  arc,
  team,
  personage,
  history,
  artist,
  role,
  collaborator,
  issueStatus,
  readingStatus,
  issueInCollection,
  issueInWishlist,
  wishlist,
  collection,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;
