import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ILicensorCl, defaultValue } from 'app/shared/model/licensor-cl.model';

export const ACTION_TYPES = {
  SEARCH_LICENSORS: 'licensor/SEARCH_LICENSORS',
  FETCH_LICENSOR_LIST: 'licensor/FETCH_LICENSOR_LIST',
  FETCH_LICENSOR: 'licensor/FETCH_LICENSOR',
  CREATE_LICENSOR: 'licensor/CREATE_LICENSOR',
  UPDATE_LICENSOR: 'licensor/UPDATE_LICENSOR',
  DELETE_LICENSOR: 'licensor/DELETE_LICENSOR',
  SET_BLOB: 'licensor/SET_BLOB',
  RESET: 'licensor/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ILicensorCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type LicensorClState = Readonly<typeof initialState>;

// Reducer

export default (state: LicensorClState = initialState, action): LicensorClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_LICENSORS):
    case REQUEST(ACTION_TYPES.FETCH_LICENSOR_LIST):
    case REQUEST(ACTION_TYPES.FETCH_LICENSOR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_LICENSOR):
    case REQUEST(ACTION_TYPES.UPDATE_LICENSOR):
    case REQUEST(ACTION_TYPES.DELETE_LICENSOR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_LICENSORS):
    case FAILURE(ACTION_TYPES.FETCH_LICENSOR_LIST):
    case FAILURE(ACTION_TYPES.FETCH_LICENSOR):
    case FAILURE(ACTION_TYPES.CREATE_LICENSOR):
    case FAILURE(ACTION_TYPES.UPDATE_LICENSOR):
    case FAILURE(ACTION_TYPES.DELETE_LICENSOR):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_LICENSORS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_LICENSOR_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_LICENSOR):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_LICENSOR):
    case SUCCESS(ACTION_TYPES.UPDATE_LICENSOR):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_LICENSOR):
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

const apiUrl = 'api/licensors';
const apiSearchUrl = 'api/_search/licensors';

// Actions

export const getSearchEntities: ICrudSearchAction<ILicensorCl> = query => ({
  type: ACTION_TYPES.SEARCH_LICENSORS,
  payload: axios.get<ILicensorCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<ILicensorCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_LICENSOR_LIST,
  payload: axios.get<ILicensorCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ILicensorCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_LICENSOR,
    payload: axios.get<ILicensorCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ILicensorCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_LICENSOR,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ILicensorCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_LICENSOR,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ILicensorCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_LICENSOR,
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
