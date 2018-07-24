import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IIssueInWishlistCl, defaultValue } from 'app/shared/model/issue-in-wishlist-cl.model';

export const ACTION_TYPES = {
  SEARCH_ISSUEINWISHLISTS: 'issueInWishlist/SEARCH_ISSUEINWISHLISTS',
  FETCH_ISSUEINWISHLIST_LIST: 'issueInWishlist/FETCH_ISSUEINWISHLIST_LIST',
  FETCH_ISSUEINWISHLIST: 'issueInWishlist/FETCH_ISSUEINWISHLIST',
  CREATE_ISSUEINWISHLIST: 'issueInWishlist/CREATE_ISSUEINWISHLIST',
  UPDATE_ISSUEINWISHLIST: 'issueInWishlist/UPDATE_ISSUEINWISHLIST',
  DELETE_ISSUEINWISHLIST: 'issueInWishlist/DELETE_ISSUEINWISHLIST',
  RESET: 'issueInWishlist/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IIssueInWishlistCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type IssueInWishlistClState = Readonly<typeof initialState>;

// Reducer

export default (state: IssueInWishlistClState = initialState, action): IssueInWishlistClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ISSUEINWISHLISTS):
    case REQUEST(ACTION_TYPES.FETCH_ISSUEINWISHLIST_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ISSUEINWISHLIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ISSUEINWISHLIST):
    case REQUEST(ACTION_TYPES.UPDATE_ISSUEINWISHLIST):
    case REQUEST(ACTION_TYPES.DELETE_ISSUEINWISHLIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_ISSUEINWISHLISTS):
    case FAILURE(ACTION_TYPES.FETCH_ISSUEINWISHLIST_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ISSUEINWISHLIST):
    case FAILURE(ACTION_TYPES.CREATE_ISSUEINWISHLIST):
    case FAILURE(ACTION_TYPES.UPDATE_ISSUEINWISHLIST):
    case FAILURE(ACTION_TYPES.DELETE_ISSUEINWISHLIST):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ISSUEINWISHLISTS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ISSUEINWISHLIST_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ISSUEINWISHLIST):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ISSUEINWISHLIST):
    case SUCCESS(ACTION_TYPES.UPDATE_ISSUEINWISHLIST):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ISSUEINWISHLIST):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/issue-in-wishlists';
const apiSearchUrl = 'api/_search/issue-in-wishlists';

// Actions

export const getSearchEntities: ICrudSearchAction<IIssueInWishlistCl> = query => ({
  type: ACTION_TYPES.SEARCH_ISSUEINWISHLISTS,
  payload: axios.get<IIssueInWishlistCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IIssueInWishlistCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ISSUEINWISHLIST_LIST,
  payload: axios.get<IIssueInWishlistCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IIssueInWishlistCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ISSUEINWISHLIST,
    payload: axios.get<IIssueInWishlistCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IIssueInWishlistCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ISSUEINWISHLIST,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IIssueInWishlistCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ISSUEINWISHLIST,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IIssueInWishlistCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ISSUEINWISHLIST,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
