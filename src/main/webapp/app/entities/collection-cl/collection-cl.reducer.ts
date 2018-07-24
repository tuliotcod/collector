import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICollectionCl, defaultValue } from 'app/shared/model/collection-cl.model';

export const ACTION_TYPES = {
  SEARCH_COLLECTIONS: 'collection/SEARCH_COLLECTIONS',
  FETCH_COLLECTION_LIST: 'collection/FETCH_COLLECTION_LIST',
  FETCH_COLLECTION: 'collection/FETCH_COLLECTION',
  CREATE_COLLECTION: 'collection/CREATE_COLLECTION',
  UPDATE_COLLECTION: 'collection/UPDATE_COLLECTION',
  DELETE_COLLECTION: 'collection/DELETE_COLLECTION',
  RESET: 'collection/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICollectionCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type CollectionClState = Readonly<typeof initialState>;

// Reducer

export default (state: CollectionClState = initialState, action): CollectionClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_COLLECTIONS):
    case REQUEST(ACTION_TYPES.FETCH_COLLECTION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_COLLECTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_COLLECTION):
    case REQUEST(ACTION_TYPES.UPDATE_COLLECTION):
    case REQUEST(ACTION_TYPES.DELETE_COLLECTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_COLLECTIONS):
    case FAILURE(ACTION_TYPES.FETCH_COLLECTION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_COLLECTION):
    case FAILURE(ACTION_TYPES.CREATE_COLLECTION):
    case FAILURE(ACTION_TYPES.UPDATE_COLLECTION):
    case FAILURE(ACTION_TYPES.DELETE_COLLECTION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_COLLECTIONS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_COLLECTION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_COLLECTION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_COLLECTION):
    case SUCCESS(ACTION_TYPES.UPDATE_COLLECTION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_COLLECTION):
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

const apiUrl = 'api/collections';
const apiSearchUrl = 'api/_search/collections';

// Actions

export const getSearchEntities: ICrudSearchAction<ICollectionCl> = query => ({
  type: ACTION_TYPES.SEARCH_COLLECTIONS,
  payload: axios.get<ICollectionCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<ICollectionCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_COLLECTION_LIST,
  payload: axios.get<ICollectionCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ICollectionCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_COLLECTION,
    payload: axios.get<ICollectionCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICollectionCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_COLLECTION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICollectionCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_COLLECTION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICollectionCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_COLLECTION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
