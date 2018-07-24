import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IStatusCl, defaultValue } from 'app/shared/model/status-cl.model';

export const ACTION_TYPES = {
  SEARCH_STATUSES: 'status/SEARCH_STATUSES',
  FETCH_STATUS_LIST: 'status/FETCH_STATUS_LIST',
  FETCH_STATUS: 'status/FETCH_STATUS',
  CREATE_STATUS: 'status/CREATE_STATUS',
  UPDATE_STATUS: 'status/UPDATE_STATUS',
  DELETE_STATUS: 'status/DELETE_STATUS',
  RESET: 'status/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IStatusCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type StatusClState = Readonly<typeof initialState>;

// Reducer

export default (state: StatusClState = initialState, action): StatusClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_STATUSES):
    case REQUEST(ACTION_TYPES.FETCH_STATUS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_STATUS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_STATUS):
    case REQUEST(ACTION_TYPES.UPDATE_STATUS):
    case REQUEST(ACTION_TYPES.DELETE_STATUS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_STATUSES):
    case FAILURE(ACTION_TYPES.FETCH_STATUS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_STATUS):
    case FAILURE(ACTION_TYPES.CREATE_STATUS):
    case FAILURE(ACTION_TYPES.UPDATE_STATUS):
    case FAILURE(ACTION_TYPES.DELETE_STATUS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_STATUSES):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_STATUS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_STATUS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_STATUS):
    case SUCCESS(ACTION_TYPES.UPDATE_STATUS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_STATUS):
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

const apiUrl = 'api/statuses';
const apiSearchUrl = 'api/_search/statuses';

// Actions

export const getSearchEntities: ICrudSearchAction<IStatusCl> = query => ({
  type: ACTION_TYPES.SEARCH_STATUSES,
  payload: axios.get<IStatusCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IStatusCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_STATUS_LIST,
  payload: axios.get<IStatusCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IStatusCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_STATUS,
    payload: axios.get<IStatusCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IStatusCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_STATUS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IStatusCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_STATUS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IStatusCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_STATUS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
