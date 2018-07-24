import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPersonageCl, defaultValue } from 'app/shared/model/personage-cl.model';

export const ACTION_TYPES = {
  SEARCH_PERSONAGES: 'personage/SEARCH_PERSONAGES',
  FETCH_PERSONAGE_LIST: 'personage/FETCH_PERSONAGE_LIST',
  FETCH_PERSONAGE: 'personage/FETCH_PERSONAGE',
  CREATE_PERSONAGE: 'personage/CREATE_PERSONAGE',
  UPDATE_PERSONAGE: 'personage/UPDATE_PERSONAGE',
  DELETE_PERSONAGE: 'personage/DELETE_PERSONAGE',
  RESET: 'personage/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPersonageCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type PersonageClState = Readonly<typeof initialState>;

// Reducer

export default (state: PersonageClState = initialState, action): PersonageClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_PERSONAGES):
    case REQUEST(ACTION_TYPES.FETCH_PERSONAGE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PERSONAGE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PERSONAGE):
    case REQUEST(ACTION_TYPES.UPDATE_PERSONAGE):
    case REQUEST(ACTION_TYPES.DELETE_PERSONAGE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_PERSONAGES):
    case FAILURE(ACTION_TYPES.FETCH_PERSONAGE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PERSONAGE):
    case FAILURE(ACTION_TYPES.CREATE_PERSONAGE):
    case FAILURE(ACTION_TYPES.UPDATE_PERSONAGE):
    case FAILURE(ACTION_TYPES.DELETE_PERSONAGE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_PERSONAGES):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PERSONAGE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PERSONAGE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PERSONAGE):
    case SUCCESS(ACTION_TYPES.UPDATE_PERSONAGE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PERSONAGE):
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

const apiUrl = 'api/personages';
const apiSearchUrl = 'api/_search/personages';

// Actions

export const getSearchEntities: ICrudSearchAction<IPersonageCl> = query => ({
  type: ACTION_TYPES.SEARCH_PERSONAGES,
  payload: axios.get<IPersonageCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IPersonageCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PERSONAGE_LIST,
  payload: axios.get<IPersonageCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IPersonageCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PERSONAGE,
    payload: axios.get<IPersonageCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPersonageCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PERSONAGE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPersonageCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PERSONAGE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPersonageCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PERSONAGE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
