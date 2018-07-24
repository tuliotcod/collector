import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRoleCl, defaultValue } from 'app/shared/model/role-cl.model';

export const ACTION_TYPES = {
  SEARCH_ROLES: 'role/SEARCH_ROLES',
  FETCH_ROLE_LIST: 'role/FETCH_ROLE_LIST',
  FETCH_ROLE: 'role/FETCH_ROLE',
  CREATE_ROLE: 'role/CREATE_ROLE',
  UPDATE_ROLE: 'role/UPDATE_ROLE',
  DELETE_ROLE: 'role/DELETE_ROLE',
  RESET: 'role/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRoleCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type RoleClState = Readonly<typeof initialState>;

// Reducer

export default (state: RoleClState = initialState, action): RoleClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ROLES):
    case REQUEST(ACTION_TYPES.FETCH_ROLE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ROLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ROLE):
    case REQUEST(ACTION_TYPES.UPDATE_ROLE):
    case REQUEST(ACTION_TYPES.DELETE_ROLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_ROLES):
    case FAILURE(ACTION_TYPES.FETCH_ROLE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ROLE):
    case FAILURE(ACTION_TYPES.CREATE_ROLE):
    case FAILURE(ACTION_TYPES.UPDATE_ROLE):
    case FAILURE(ACTION_TYPES.DELETE_ROLE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ROLES):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ROLE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ROLE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ROLE):
    case SUCCESS(ACTION_TYPES.UPDATE_ROLE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ROLE):
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

const apiUrl = 'api/roles';
const apiSearchUrl = 'api/_search/roles';

// Actions

export const getSearchEntities: ICrudSearchAction<IRoleCl> = query => ({
  type: ACTION_TYPES.SEARCH_ROLES,
  payload: axios.get<IRoleCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IRoleCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ROLE_LIST,
  payload: axios.get<IRoleCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IRoleCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ROLE,
    payload: axios.get<IRoleCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRoleCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ROLE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRoleCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ROLE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRoleCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ROLE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
