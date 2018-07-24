import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IArtistCl, defaultValue } from 'app/shared/model/artist-cl.model';

export const ACTION_TYPES = {
  SEARCH_ARTISTS: 'artist/SEARCH_ARTISTS',
  FETCH_ARTIST_LIST: 'artist/FETCH_ARTIST_LIST',
  FETCH_ARTIST: 'artist/FETCH_ARTIST',
  CREATE_ARTIST: 'artist/CREATE_ARTIST',
  UPDATE_ARTIST: 'artist/UPDATE_ARTIST',
  DELETE_ARTIST: 'artist/DELETE_ARTIST',
  SET_BLOB: 'artist/SET_BLOB',
  RESET: 'artist/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IArtistCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ArtistClState = Readonly<typeof initialState>;

// Reducer

export default (state: ArtistClState = initialState, action): ArtistClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ARTISTS):
    case REQUEST(ACTION_TYPES.FETCH_ARTIST_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ARTIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ARTIST):
    case REQUEST(ACTION_TYPES.UPDATE_ARTIST):
    case REQUEST(ACTION_TYPES.DELETE_ARTIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_ARTISTS):
    case FAILURE(ACTION_TYPES.FETCH_ARTIST_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ARTIST):
    case FAILURE(ACTION_TYPES.CREATE_ARTIST):
    case FAILURE(ACTION_TYPES.UPDATE_ARTIST):
    case FAILURE(ACTION_TYPES.DELETE_ARTIST):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ARTISTS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ARTIST_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ARTIST):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ARTIST):
    case SUCCESS(ACTION_TYPES.UPDATE_ARTIST):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ARTIST):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.SET_BLOB:
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType
        }
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/artists';
const apiSearchUrl = 'api/_search/artists';

// Actions

export const getSearchEntities: ICrudSearchAction<IArtistCl> = query => ({
  type: ACTION_TYPES.SEARCH_ARTISTS,
  payload: axios.get<IArtistCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IArtistCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ARTIST_LIST,
  payload: axios.get<IArtistCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IArtistCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ARTIST,
    payload: axios.get<IArtistCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IArtistCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ARTIST,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IArtistCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ARTIST,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IArtistCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ARTIST,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType
  }
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
