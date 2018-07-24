import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IIssueStatusCl, defaultValue } from 'app/shared/model/issue-status-cl.model';

export const ACTION_TYPES = {
  SEARCH_ISSUESTATUSES: 'issueStatus/SEARCH_ISSUESTATUSES',
  FETCH_ISSUESTATUS_LIST: 'issueStatus/FETCH_ISSUESTATUS_LIST',
  FETCH_ISSUESTATUS: 'issueStatus/FETCH_ISSUESTATUS',
  CREATE_ISSUESTATUS: 'issueStatus/CREATE_ISSUESTATUS',
  UPDATE_ISSUESTATUS: 'issueStatus/UPDATE_ISSUESTATUS',
  DELETE_ISSUESTATUS: 'issueStatus/DELETE_ISSUESTATUS',
  RESET: 'issueStatus/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IIssueStatusCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type IssueStatusClState = Readonly<typeof initialState>;

// Reducer

export default (state: IssueStatusClState = initialState, action): IssueStatusClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ISSUESTATUSES):
    case REQUEST(ACTION_TYPES.FETCH_ISSUESTATUS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ISSUESTATUS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ISSUESTATUS):
    case REQUEST(ACTION_TYPES.UPDATE_ISSUESTATUS):
    case REQUEST(ACTION_TYPES.DELETE_ISSUESTATUS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_ISSUESTATUSES):
    case FAILURE(ACTION_TYPES.FETCH_ISSUESTATUS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ISSUESTATUS):
    case FAILURE(ACTION_TYPES.CREATE_ISSUESTATUS):
    case FAILURE(ACTION_TYPES.UPDATE_ISSUESTATUS):
    case FAILURE(ACTION_TYPES.DELETE_ISSUESTATUS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ISSUESTATUSES):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ISSUESTATUS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ISSUESTATUS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ISSUESTATUS):
    case SUCCESS(ACTION_TYPES.UPDATE_ISSUESTATUS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ISSUESTATUS):
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

const apiUrl = 'api/issue-statuses';
const apiSearchUrl = 'api/_search/issue-statuses';

// Actions

export const getSearchEntities: ICrudSearchAction<IIssueStatusCl> = query => ({
  type: ACTION_TYPES.SEARCH_ISSUESTATUSES,
  payload: axios.get<IIssueStatusCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IIssueStatusCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ISSUESTATUS_LIST,
  payload: axios.get<IIssueStatusCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IIssueStatusCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ISSUESTATUS,
    payload: axios.get<IIssueStatusCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IIssueStatusCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ISSUESTATUS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IIssueStatusCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ISSUESTATUS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IIssueStatusCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ISSUESTATUS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
