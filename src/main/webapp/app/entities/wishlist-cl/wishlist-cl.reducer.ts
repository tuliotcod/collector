import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IWishlistCl, defaultValue } from 'app/shared/model/wishlist-cl.model';

export const ACTION_TYPES = {
  SEARCH_WISHLISTS: 'wishlist/SEARCH_WISHLISTS',
  FETCH_WISHLIST_LIST: 'wishlist/FETCH_WISHLIST_LIST',
  FETCH_WISHLIST: 'wishlist/FETCH_WISHLIST',
  CREATE_WISHLIST: 'wishlist/CREATE_WISHLIST',
  UPDATE_WISHLIST: 'wishlist/UPDATE_WISHLIST',
  DELETE_WISHLIST: 'wishlist/DELETE_WISHLIST',
  RESET: 'wishlist/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IWishlistCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type WishlistClState = Readonly<typeof initialState>;

// Reducer

export default (state: WishlistClState = initialState, action): WishlistClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_WISHLISTS):
    case REQUEST(ACTION_TYPES.FETCH_WISHLIST_LIST):
    case REQUEST(ACTION_TYPES.FETCH_WISHLIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_WISHLIST):
    case REQUEST(ACTION_TYPES.UPDATE_WISHLIST):
    case REQUEST(ACTION_TYPES.DELETE_WISHLIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_WISHLISTS):
    case FAILURE(ACTION_TYPES.FETCH_WISHLIST_LIST):
    case FAILURE(ACTION_TYPES.FETCH_WISHLIST):
    case FAILURE(ACTION_TYPES.CREATE_WISHLIST):
    case FAILURE(ACTION_TYPES.UPDATE_WISHLIST):
    case FAILURE(ACTION_TYPES.DELETE_WISHLIST):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_WISHLISTS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_WISHLIST_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_WISHLIST):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_WISHLIST):
    case SUCCESS(ACTION_TYPES.UPDATE_WISHLIST):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_WISHLIST):
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

const apiUrl = 'api/wishlists';
const apiSearchUrl = 'api/_search/wishlists';

// Actions

export const getSearchEntities: ICrudSearchAction<IWishlistCl> = query => ({
  type: ACTION_TYPES.SEARCH_WISHLISTS,
  payload: axios.get<IWishlistCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IWishlistCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_WISHLIST_LIST,
  payload: axios.get<IWishlistCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IWishlistCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_WISHLIST,
    payload: axios.get<IWishlistCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IWishlistCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_WISHLIST,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IWishlistCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_WISHLIST,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IWishlistCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_WISHLIST,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
