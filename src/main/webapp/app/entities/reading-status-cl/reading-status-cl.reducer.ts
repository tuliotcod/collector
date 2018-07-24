import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IReadingStatusCl, defaultValue } from 'app/shared/model/reading-status-cl.model';

export const ACTION_TYPES = {
  SEARCH_READINGSTATUSES: 'readingStatus/SEARCH_READINGSTATUSES',
  FETCH_READINGSTATUS_LIST: 'readingStatus/FETCH_READINGSTATUS_LIST',
  FETCH_READINGSTATUS: 'readingStatus/FETCH_READINGSTATUS',
  CREATE_READINGSTATUS: 'readingStatus/CREATE_READINGSTATUS',
  UPDATE_READINGSTATUS: 'readingStatus/UPDATE_READINGSTATUS',
  DELETE_READINGSTATUS: 'readingStatus/DELETE_READINGSTATUS',
  RESET: 'readingStatus/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IReadingStatusCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ReadingStatusClState = Readonly<typeof initialState>;

// Reducer

export default (state: ReadingStatusClState = initialState, action): ReadingStatusClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_READINGSTATUSES):
    case REQUEST(ACTION_TYPES.FETCH_READINGSTATUS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_READINGSTATUS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_READINGSTATUS):
    case REQUEST(ACTION_TYPES.UPDATE_READINGSTATUS):
    case REQUEST(ACTION_TYPES.DELETE_READINGSTATUS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_READINGSTATUSES):
    case FAILURE(ACTION_TYPES.FETCH_READINGSTATUS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_READINGSTATUS):
    case FAILURE(ACTION_TYPES.CREATE_READINGSTATUS):
    case FAILURE(ACTION_TYPES.UPDATE_READINGSTATUS):
    case FAILURE(ACTION_TYPES.DELETE_READINGSTATUS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_READINGSTATUSES):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_READINGSTATUS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_READINGSTATUS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_READINGSTATUS):
    case SUCCESS(ACTION_TYPES.UPDATE_READINGSTATUS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_READINGSTATUS):
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

const apiUrl = 'api/reading-statuses';
const apiSearchUrl = 'api/_search/reading-statuses';

// Actions

export const getSearchEntities: ICrudSearchAction<IReadingStatusCl> = query => ({
  type: ACTION_TYPES.SEARCH_READINGSTATUSES,
  payload: axios.get<IReadingStatusCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IReadingStatusCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_READINGSTATUS_LIST,
  payload: axios.get<IReadingStatusCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IReadingStatusCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_READINGSTATUS,
    payload: axios.get<IReadingStatusCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IReadingStatusCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_READINGSTATUS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IReadingStatusCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_READINGSTATUS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IReadingStatusCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_READINGSTATUS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
