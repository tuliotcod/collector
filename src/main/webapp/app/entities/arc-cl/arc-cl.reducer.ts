import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IArcCl, defaultValue } from 'app/shared/model/arc-cl.model';

export const ACTION_TYPES = {
  SEARCH_ARCS: 'arc/SEARCH_ARCS',
  FETCH_ARC_LIST: 'arc/FETCH_ARC_LIST',
  FETCH_ARC: 'arc/FETCH_ARC',
  CREATE_ARC: 'arc/CREATE_ARC',
  UPDATE_ARC: 'arc/UPDATE_ARC',
  DELETE_ARC: 'arc/DELETE_ARC',
  RESET: 'arc/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IArcCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ArcClState = Readonly<typeof initialState>;

// Reducer

export default (state: ArcClState = initialState, action): ArcClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ARCS):
    case REQUEST(ACTION_TYPES.FETCH_ARC_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ARC):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ARC):
    case REQUEST(ACTION_TYPES.UPDATE_ARC):
    case REQUEST(ACTION_TYPES.DELETE_ARC):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_ARCS):
    case FAILURE(ACTION_TYPES.FETCH_ARC_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ARC):
    case FAILURE(ACTION_TYPES.CREATE_ARC):
    case FAILURE(ACTION_TYPES.UPDATE_ARC):
    case FAILURE(ACTION_TYPES.DELETE_ARC):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ARCS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ARC_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ARC):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ARC):
    case SUCCESS(ACTION_TYPES.UPDATE_ARC):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ARC):
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

const apiUrl = 'api/arcs';
const apiSearchUrl = 'api/_search/arcs';

// Actions

export const getSearchEntities: ICrudSearchAction<IArcCl> = query => ({
  type: ACTION_TYPES.SEARCH_ARCS,
  payload: axios.get<IArcCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IArcCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ARC_LIST,
  payload: axios.get<IArcCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IArcCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ARC,
    payload: axios.get<IArcCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IArcCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ARC,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IArcCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ARC,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IArcCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ARC,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
