import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IActionCl, defaultValue } from 'app/shared/model/action-cl.model';

export const ACTION_TYPES = {
  SEARCH_ACTIONS: 'action/SEARCH_ACTIONS',
  FETCH_ACTION_LIST: 'action/FETCH_ACTION_LIST',
  FETCH_ACTION: 'action/FETCH_ACTION',
  CREATE_ACTION: 'action/CREATE_ACTION',
  UPDATE_ACTION: 'action/UPDATE_ACTION',
  DELETE_ACTION: 'action/DELETE_ACTION',
  RESET: 'action/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IActionCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ActionClState = Readonly<typeof initialState>;

// Reducer

export default (state: ActionClState = initialState, action): ActionClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ACTIONS):
    case REQUEST(ACTION_TYPES.FETCH_ACTION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ACTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ACTION):
    case REQUEST(ACTION_TYPES.UPDATE_ACTION):
    case REQUEST(ACTION_TYPES.DELETE_ACTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_ACTIONS):
    case FAILURE(ACTION_TYPES.FETCH_ACTION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ACTION):
    case FAILURE(ACTION_TYPES.CREATE_ACTION):
    case FAILURE(ACTION_TYPES.UPDATE_ACTION):
    case FAILURE(ACTION_TYPES.DELETE_ACTION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ACTIONS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACTION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACTION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ACTION):
    case SUCCESS(ACTION_TYPES.UPDATE_ACTION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ACTION):
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

const apiUrl = 'api/actions';
const apiSearchUrl = 'api/_search/actions';

// Actions

export const getSearchEntities: ICrudSearchAction<IActionCl> = query => ({
  type: ACTION_TYPES.SEARCH_ACTIONS,
  payload: axios.get<IActionCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IActionCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ACTION_LIST,
  payload: axios.get<IActionCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IActionCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ACTION,
    payload: axios.get<IActionCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IActionCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ACTION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IActionCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ACTION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IActionCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ACTION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
