import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IFinishingCl, defaultValue } from 'app/shared/model/finishing-cl.model';

export const ACTION_TYPES = {
  SEARCH_FINISHINGS: 'finishing/SEARCH_FINISHINGS',
  FETCH_FINISHING_LIST: 'finishing/FETCH_FINISHING_LIST',
  FETCH_FINISHING: 'finishing/FETCH_FINISHING',
  CREATE_FINISHING: 'finishing/CREATE_FINISHING',
  UPDATE_FINISHING: 'finishing/UPDATE_FINISHING',
  DELETE_FINISHING: 'finishing/DELETE_FINISHING',
  RESET: 'finishing/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFinishingCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type FinishingClState = Readonly<typeof initialState>;

// Reducer

export default (state: FinishingClState = initialState, action): FinishingClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_FINISHINGS):
    case REQUEST(ACTION_TYPES.FETCH_FINISHING_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FINISHING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_FINISHING):
    case REQUEST(ACTION_TYPES.UPDATE_FINISHING):
    case REQUEST(ACTION_TYPES.DELETE_FINISHING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_FINISHINGS):
    case FAILURE(ACTION_TYPES.FETCH_FINISHING_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FINISHING):
    case FAILURE(ACTION_TYPES.CREATE_FINISHING):
    case FAILURE(ACTION_TYPES.UPDATE_FINISHING):
    case FAILURE(ACTION_TYPES.DELETE_FINISHING):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_FINISHINGS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_FINISHING_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_FINISHING):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_FINISHING):
    case SUCCESS(ACTION_TYPES.UPDATE_FINISHING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_FINISHING):
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

const apiUrl = 'api/finishings';
const apiSearchUrl = 'api/_search/finishings';

// Actions

export const getSearchEntities: ICrudSearchAction<IFinishingCl> = query => ({
  type: ACTION_TYPES.SEARCH_FINISHINGS,
  payload: axios.get<IFinishingCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IFinishingCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_FINISHING_LIST,
  payload: axios.get<IFinishingCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IFinishingCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FINISHING,
    payload: axios.get<IFinishingCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IFinishingCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FINISHING,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IFinishingCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FINISHING,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFinishingCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FINISHING,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
