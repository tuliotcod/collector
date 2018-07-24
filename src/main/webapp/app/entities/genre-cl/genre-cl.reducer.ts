import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IGenreCl, defaultValue } from 'app/shared/model/genre-cl.model';

export const ACTION_TYPES = {
  SEARCH_GENRES: 'genre/SEARCH_GENRES',
  FETCH_GENRE_LIST: 'genre/FETCH_GENRE_LIST',
  FETCH_GENRE: 'genre/FETCH_GENRE',
  CREATE_GENRE: 'genre/CREATE_GENRE',
  UPDATE_GENRE: 'genre/UPDATE_GENRE',
  DELETE_GENRE: 'genre/DELETE_GENRE',
  RESET: 'genre/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IGenreCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type GenreClState = Readonly<typeof initialState>;

// Reducer

export default (state: GenreClState = initialState, action): GenreClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_GENRES):
    case REQUEST(ACTION_TYPES.FETCH_GENRE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GENRE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_GENRE):
    case REQUEST(ACTION_TYPES.UPDATE_GENRE):
    case REQUEST(ACTION_TYPES.DELETE_GENRE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_GENRES):
    case FAILURE(ACTION_TYPES.FETCH_GENRE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GENRE):
    case FAILURE(ACTION_TYPES.CREATE_GENRE):
    case FAILURE(ACTION_TYPES.UPDATE_GENRE):
    case FAILURE(ACTION_TYPES.DELETE_GENRE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_GENRES):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_GENRE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_GENRE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_GENRE):
    case SUCCESS(ACTION_TYPES.UPDATE_GENRE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_GENRE):
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

const apiUrl = 'api/genres';
const apiSearchUrl = 'api/_search/genres';

// Actions

export const getSearchEntities: ICrudSearchAction<IGenreCl> = query => ({
  type: ACTION_TYPES.SEARCH_GENRES,
  payload: axios.get<IGenreCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IGenreCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_GENRE_LIST,
  payload: axios.get<IGenreCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IGenreCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GENRE,
    payload: axios.get<IGenreCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IGenreCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GENRE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IGenreCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GENRE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGenreCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GENRE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
