import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IFormatCl, defaultValue } from 'app/shared/model/format-cl.model';

export const ACTION_TYPES = {
  SEARCH_FORMATS: 'format/SEARCH_FORMATS',
  FETCH_FORMAT_LIST: 'format/FETCH_FORMAT_LIST',
  FETCH_FORMAT: 'format/FETCH_FORMAT',
  CREATE_FORMAT: 'format/CREATE_FORMAT',
  UPDATE_FORMAT: 'format/UPDATE_FORMAT',
  DELETE_FORMAT: 'format/DELETE_FORMAT',
  RESET: 'format/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFormatCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type FormatClState = Readonly<typeof initialState>;

// Reducer

export default (state: FormatClState = initialState, action): FormatClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_FORMATS):
    case REQUEST(ACTION_TYPES.FETCH_FORMAT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FORMAT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_FORMAT):
    case REQUEST(ACTION_TYPES.UPDATE_FORMAT):
    case REQUEST(ACTION_TYPES.DELETE_FORMAT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_FORMATS):
    case FAILURE(ACTION_TYPES.FETCH_FORMAT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FORMAT):
    case FAILURE(ACTION_TYPES.CREATE_FORMAT):
    case FAILURE(ACTION_TYPES.UPDATE_FORMAT):
    case FAILURE(ACTION_TYPES.DELETE_FORMAT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_FORMATS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_FORMAT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_FORMAT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_FORMAT):
    case SUCCESS(ACTION_TYPES.UPDATE_FORMAT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_FORMAT):
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

const apiUrl = 'api/formats';
const apiSearchUrl = 'api/_search/formats';

// Actions

export const getSearchEntities: ICrudSearchAction<IFormatCl> = query => ({
  type: ACTION_TYPES.SEARCH_FORMATS,
  payload: axios.get<IFormatCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IFormatCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_FORMAT_LIST,
  payload: axios.get<IFormatCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IFormatCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FORMAT,
    payload: axios.get<IFormatCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IFormatCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FORMAT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IFormatCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FORMAT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFormatCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FORMAT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
