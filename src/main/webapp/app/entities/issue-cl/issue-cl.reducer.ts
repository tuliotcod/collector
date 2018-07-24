import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IIssueCl, defaultValue } from 'app/shared/model/issue-cl.model';

export const ACTION_TYPES = {
  SEARCH_ISSUES: 'issue/SEARCH_ISSUES',
  FETCH_ISSUE_LIST: 'issue/FETCH_ISSUE_LIST',
  FETCH_ISSUE: 'issue/FETCH_ISSUE',
  CREATE_ISSUE: 'issue/CREATE_ISSUE',
  UPDATE_ISSUE: 'issue/UPDATE_ISSUE',
  DELETE_ISSUE: 'issue/DELETE_ISSUE',
  SET_BLOB: 'issue/SET_BLOB',
  RESET: 'issue/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IIssueCl>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type IssueClState = Readonly<typeof initialState>;

// Reducer

export default (state: IssueClState = initialState, action): IssueClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ISSUES):
    case REQUEST(ACTION_TYPES.FETCH_ISSUE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ISSUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ISSUE):
    case REQUEST(ACTION_TYPES.UPDATE_ISSUE):
    case REQUEST(ACTION_TYPES.DELETE_ISSUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_ISSUES):
    case FAILURE(ACTION_TYPES.FETCH_ISSUE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ISSUE):
    case FAILURE(ACTION_TYPES.CREATE_ISSUE):
    case FAILURE(ACTION_TYPES.UPDATE_ISSUE):
    case FAILURE(ACTION_TYPES.DELETE_ISSUE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ISSUES):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ISSUE_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ISSUE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ISSUE):
    case SUCCESS(ACTION_TYPES.UPDATE_ISSUE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ISSUE):
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

const apiUrl = 'api/issues';
const apiSearchUrl = 'api/_search/issues';

// Actions

export const getSearchEntities: ICrudSearchAction<IIssueCl> = query => ({
  type: ACTION_TYPES.SEARCH_ISSUES,
  payload: axios.get<IIssueCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IIssueCl> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ISSUE_LIST,
    payload: axios.get<IIssueCl>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IIssueCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ISSUE,
    payload: axios.get<IIssueCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IIssueCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ISSUE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IIssueCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ISSUE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IIssueCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ISSUE,
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
