import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICollaboratorCl, defaultValue } from 'app/shared/model/collaborator-cl.model';

export const ACTION_TYPES = {
  SEARCH_COLLABORATORS: 'collaborator/SEARCH_COLLABORATORS',
  FETCH_COLLABORATOR_LIST: 'collaborator/FETCH_COLLABORATOR_LIST',
  FETCH_COLLABORATOR: 'collaborator/FETCH_COLLABORATOR',
  CREATE_COLLABORATOR: 'collaborator/CREATE_COLLABORATOR',
  UPDATE_COLLABORATOR: 'collaborator/UPDATE_COLLABORATOR',
  DELETE_COLLABORATOR: 'collaborator/DELETE_COLLABORATOR',
  RESET: 'collaborator/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICollaboratorCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type CollaboratorClState = Readonly<typeof initialState>;

// Reducer

export default (state: CollaboratorClState = initialState, action): CollaboratorClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_COLLABORATORS):
    case REQUEST(ACTION_TYPES.FETCH_COLLABORATOR_LIST):
    case REQUEST(ACTION_TYPES.FETCH_COLLABORATOR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_COLLABORATOR):
    case REQUEST(ACTION_TYPES.UPDATE_COLLABORATOR):
    case REQUEST(ACTION_TYPES.DELETE_COLLABORATOR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_COLLABORATORS):
    case FAILURE(ACTION_TYPES.FETCH_COLLABORATOR_LIST):
    case FAILURE(ACTION_TYPES.FETCH_COLLABORATOR):
    case FAILURE(ACTION_TYPES.CREATE_COLLABORATOR):
    case FAILURE(ACTION_TYPES.UPDATE_COLLABORATOR):
    case FAILURE(ACTION_TYPES.DELETE_COLLABORATOR):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_COLLABORATORS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_COLLABORATOR_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_COLLABORATOR):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_COLLABORATOR):
    case SUCCESS(ACTION_TYPES.UPDATE_COLLABORATOR):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_COLLABORATOR):
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

const apiUrl = 'api/collaborators';
const apiSearchUrl = 'api/_search/collaborators';

// Actions

export const getSearchEntities: ICrudSearchAction<ICollaboratorCl> = query => ({
  type: ACTION_TYPES.SEARCH_COLLABORATORS,
  payload: axios.get<ICollaboratorCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<ICollaboratorCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_COLLABORATOR_LIST,
  payload: axios.get<ICollaboratorCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ICollaboratorCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_COLLABORATOR,
    payload: axios.get<ICollaboratorCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICollaboratorCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_COLLABORATOR,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICollaboratorCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_COLLABORATOR,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICollaboratorCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_COLLABORATOR,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
