import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPublisherCl, defaultValue } from 'app/shared/model/publisher-cl.model';

export const ACTION_TYPES = {
  SEARCH_PUBLISHERS: 'publisher/SEARCH_PUBLISHERS',
  FETCH_PUBLISHER_LIST: 'publisher/FETCH_PUBLISHER_LIST',
  FETCH_PUBLISHER: 'publisher/FETCH_PUBLISHER',
  CREATE_PUBLISHER: 'publisher/CREATE_PUBLISHER',
  UPDATE_PUBLISHER: 'publisher/UPDATE_PUBLISHER',
  DELETE_PUBLISHER: 'publisher/DELETE_PUBLISHER',
  SET_BLOB: 'publisher/SET_BLOB',
  RESET: 'publisher/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPublisherCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type PublisherClState = Readonly<typeof initialState>;

// Reducer

export default (state: PublisherClState = initialState, action): PublisherClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_PUBLISHERS):
    case REQUEST(ACTION_TYPES.FETCH_PUBLISHER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PUBLISHER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PUBLISHER):
    case REQUEST(ACTION_TYPES.UPDATE_PUBLISHER):
    case REQUEST(ACTION_TYPES.DELETE_PUBLISHER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_PUBLISHERS):
    case FAILURE(ACTION_TYPES.FETCH_PUBLISHER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PUBLISHER):
    case FAILURE(ACTION_TYPES.CREATE_PUBLISHER):
    case FAILURE(ACTION_TYPES.UPDATE_PUBLISHER):
    case FAILURE(ACTION_TYPES.DELETE_PUBLISHER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_PUBLISHERS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PUBLISHER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PUBLISHER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PUBLISHER):
    case SUCCESS(ACTION_TYPES.UPDATE_PUBLISHER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PUBLISHER):
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

const apiUrl = 'api/publishers';
const apiSearchUrl = 'api/_search/publishers';

// Actions

export const getSearchEntities: ICrudSearchAction<IPublisherCl> = query => ({
  type: ACTION_TYPES.SEARCH_PUBLISHERS,
  payload: axios.get<IPublisherCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IPublisherCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PUBLISHER_LIST,
  payload: axios.get<IPublisherCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IPublisherCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PUBLISHER,
    payload: axios.get<IPublisherCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPublisherCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PUBLISHER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPublisherCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PUBLISHER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPublisherCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PUBLISHER,
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
