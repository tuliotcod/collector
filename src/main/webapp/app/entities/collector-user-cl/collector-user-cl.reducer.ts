import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICollectorUserCl, defaultValue } from 'app/shared/model/collector-user-cl.model';

export const ACTION_TYPES = {
  SEARCH_COLLECTORUSERS: 'collectorUser/SEARCH_COLLECTORUSERS',
  FETCH_COLLECTORUSER_LIST: 'collectorUser/FETCH_COLLECTORUSER_LIST',
  FETCH_COLLECTORUSER: 'collectorUser/FETCH_COLLECTORUSER',
  CREATE_COLLECTORUSER: 'collectorUser/CREATE_COLLECTORUSER',
  UPDATE_COLLECTORUSER: 'collectorUser/UPDATE_COLLECTORUSER',
  DELETE_COLLECTORUSER: 'collectorUser/DELETE_COLLECTORUSER',
  RESET: 'collectorUser/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICollectorUserCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type CollectorUserClState = Readonly<typeof initialState>;

// Reducer

export default (state: CollectorUserClState = initialState, action): CollectorUserClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_COLLECTORUSERS):
    case REQUEST(ACTION_TYPES.FETCH_COLLECTORUSER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_COLLECTORUSER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_COLLECTORUSER):
    case REQUEST(ACTION_TYPES.UPDATE_COLLECTORUSER):
    case REQUEST(ACTION_TYPES.DELETE_COLLECTORUSER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_COLLECTORUSERS):
    case FAILURE(ACTION_TYPES.FETCH_COLLECTORUSER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_COLLECTORUSER):
    case FAILURE(ACTION_TYPES.CREATE_COLLECTORUSER):
    case FAILURE(ACTION_TYPES.UPDATE_COLLECTORUSER):
    case FAILURE(ACTION_TYPES.DELETE_COLLECTORUSER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_COLLECTORUSERS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_COLLECTORUSER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_COLLECTORUSER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_COLLECTORUSER):
    case SUCCESS(ACTION_TYPES.UPDATE_COLLECTORUSER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_COLLECTORUSER):
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

const apiUrl = 'api/collector-users';
const apiSearchUrl = 'api/_search/collector-users';

// Actions

export const getSearchEntities: ICrudSearchAction<ICollectorUserCl> = query => ({
  type: ACTION_TYPES.SEARCH_COLLECTORUSERS,
  payload: axios.get<ICollectorUserCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<ICollectorUserCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_COLLECTORUSER_LIST,
  payload: axios.get<ICollectorUserCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ICollectorUserCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_COLLECTORUSER,
    payload: axios.get<ICollectorUserCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICollectorUserCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_COLLECTORUSER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICollectorUserCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_COLLECTORUSER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICollectorUserCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_COLLECTORUSER,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
