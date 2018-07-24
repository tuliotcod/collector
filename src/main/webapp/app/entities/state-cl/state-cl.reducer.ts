import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IStateCl, defaultValue } from 'app/shared/model/state-cl.model';

export const ACTION_TYPES = {
  SEARCH_STATES: 'state/SEARCH_STATES',
  FETCH_STATE_LIST: 'state/FETCH_STATE_LIST',
  FETCH_STATE: 'state/FETCH_STATE',
  CREATE_STATE: 'state/CREATE_STATE',
  UPDATE_STATE: 'state/UPDATE_STATE',
  DELETE_STATE: 'state/DELETE_STATE',
  RESET: 'state/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IStateCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type StateClState = Readonly<typeof initialState>;

// Reducer

export default (state: StateClState = initialState, action): StateClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_STATES):
    case REQUEST(ACTION_TYPES.FETCH_STATE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_STATE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_STATE):
    case REQUEST(ACTION_TYPES.UPDATE_STATE):
    case REQUEST(ACTION_TYPES.DELETE_STATE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_STATES):
    case FAILURE(ACTION_TYPES.FETCH_STATE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_STATE):
    case FAILURE(ACTION_TYPES.CREATE_STATE):
    case FAILURE(ACTION_TYPES.UPDATE_STATE):
    case FAILURE(ACTION_TYPES.DELETE_STATE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_STATES):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_STATE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_STATE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_STATE):
    case SUCCESS(ACTION_TYPES.UPDATE_STATE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_STATE):
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

const apiUrl = 'api/states';
const apiSearchUrl = 'api/_search/states';

// Actions

export const getSearchEntities: ICrudSearchAction<IStateCl> = query => ({
  type: ACTION_TYPES.SEARCH_STATES,
  payload: axios.get<IStateCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IStateCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_STATE_LIST,
  payload: axios.get<IStateCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IStateCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_STATE,
    payload: axios.get<IStateCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IStateCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_STATE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IStateCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_STATE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IStateCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_STATE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
