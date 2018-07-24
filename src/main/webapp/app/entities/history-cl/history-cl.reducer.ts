import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IHistoryCl, defaultValue } from 'app/shared/model/history-cl.model';

export const ACTION_TYPES = {
  SEARCH_HISTORIES: 'history/SEARCH_HISTORIES',
  FETCH_HISTORY_LIST: 'history/FETCH_HISTORY_LIST',
  FETCH_HISTORY: 'history/FETCH_HISTORY',
  CREATE_HISTORY: 'history/CREATE_HISTORY',
  UPDATE_HISTORY: 'history/UPDATE_HISTORY',
  DELETE_HISTORY: 'history/DELETE_HISTORY',
  RESET: 'history/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IHistoryCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type HistoryClState = Readonly<typeof initialState>;

// Reducer

export default (state: HistoryClState = initialState, action): HistoryClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_HISTORIES):
    case REQUEST(ACTION_TYPES.FETCH_HISTORY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_HISTORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_HISTORY):
    case REQUEST(ACTION_TYPES.UPDATE_HISTORY):
    case REQUEST(ACTION_TYPES.DELETE_HISTORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_HISTORIES):
    case FAILURE(ACTION_TYPES.FETCH_HISTORY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_HISTORY):
    case FAILURE(ACTION_TYPES.CREATE_HISTORY):
    case FAILURE(ACTION_TYPES.UPDATE_HISTORY):
    case FAILURE(ACTION_TYPES.DELETE_HISTORY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_HISTORIES):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_HISTORY_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_HISTORY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_HISTORY):
    case SUCCESS(ACTION_TYPES.UPDATE_HISTORY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_HISTORY):
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

const apiUrl = 'api/histories';
const apiSearchUrl = 'api/_search/histories';

// Actions

export const getSearchEntities: ICrudSearchAction<IHistoryCl> = query => ({
  type: ACTION_TYPES.SEARCH_HISTORIES,
  payload: axios.get<IHistoryCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IHistoryCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_HISTORY_LIST,
  payload: axios.get<IHistoryCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IHistoryCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_HISTORY,
    payload: axios.get<IHistoryCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IHistoryCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_HISTORY,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IHistoryCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_HISTORY,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IHistoryCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_HISTORY,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
