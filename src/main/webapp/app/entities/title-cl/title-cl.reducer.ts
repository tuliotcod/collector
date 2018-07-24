import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITitleCl, defaultValue } from 'app/shared/model/title-cl.model';

export const ACTION_TYPES = {
  SEARCH_TITLES: 'title/SEARCH_TITLES',
  FETCH_TITLE_LIST: 'title/FETCH_TITLE_LIST',
  FETCH_TITLE: 'title/FETCH_TITLE',
  CREATE_TITLE: 'title/CREATE_TITLE',
  UPDATE_TITLE: 'title/UPDATE_TITLE',
  DELETE_TITLE: 'title/DELETE_TITLE',
  RESET: 'title/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITitleCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type TitleClState = Readonly<typeof initialState>;

// Reducer

export default (state: TitleClState = initialState, action): TitleClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_TITLES):
    case REQUEST(ACTION_TYPES.FETCH_TITLE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TITLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_TITLE):
    case REQUEST(ACTION_TYPES.UPDATE_TITLE):
    case REQUEST(ACTION_TYPES.DELETE_TITLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_TITLES):
    case FAILURE(ACTION_TYPES.FETCH_TITLE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TITLE):
    case FAILURE(ACTION_TYPES.CREATE_TITLE):
    case FAILURE(ACTION_TYPES.UPDATE_TITLE):
    case FAILURE(ACTION_TYPES.DELETE_TITLE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_TITLES):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_TITLE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_TITLE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_TITLE):
    case SUCCESS(ACTION_TYPES.UPDATE_TITLE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_TITLE):
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

const apiUrl = 'api/titles';
const apiSearchUrl = 'api/_search/titles';

// Actions

export const getSearchEntities: ICrudSearchAction<ITitleCl> = query => ({
  type: ACTION_TYPES.SEARCH_TITLES,
  payload: axios.get<ITitleCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<ITitleCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_TITLE_LIST,
  payload: axios.get<ITitleCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ITitleCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TITLE,
    payload: axios.get<ITitleCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ITitleCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TITLE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ITitleCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TITLE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITitleCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TITLE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
