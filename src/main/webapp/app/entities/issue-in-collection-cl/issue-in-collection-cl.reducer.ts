import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IIssueInCollectionCl, defaultValue } from 'app/shared/model/issue-in-collection-cl.model';

export const ACTION_TYPES = {
  SEARCH_ISSUEINCOLLECTIONS: 'issueInCollection/SEARCH_ISSUEINCOLLECTIONS',
  FETCH_ISSUEINCOLLECTION_LIST: 'issueInCollection/FETCH_ISSUEINCOLLECTION_LIST',
  FETCH_ISSUEINCOLLECTION: 'issueInCollection/FETCH_ISSUEINCOLLECTION',
  CREATE_ISSUEINCOLLECTION: 'issueInCollection/CREATE_ISSUEINCOLLECTION',
  UPDATE_ISSUEINCOLLECTION: 'issueInCollection/UPDATE_ISSUEINCOLLECTION',
  DELETE_ISSUEINCOLLECTION: 'issueInCollection/DELETE_ISSUEINCOLLECTION',
  RESET: 'issueInCollection/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IIssueInCollectionCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type IssueInCollectionClState = Readonly<typeof initialState>;

// Reducer

export default (state: IssueInCollectionClState = initialState, action): IssueInCollectionClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ISSUEINCOLLECTIONS):
    case REQUEST(ACTION_TYPES.FETCH_ISSUEINCOLLECTION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ISSUEINCOLLECTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ISSUEINCOLLECTION):
    case REQUEST(ACTION_TYPES.UPDATE_ISSUEINCOLLECTION):
    case REQUEST(ACTION_TYPES.DELETE_ISSUEINCOLLECTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_ISSUEINCOLLECTIONS):
    case FAILURE(ACTION_TYPES.FETCH_ISSUEINCOLLECTION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ISSUEINCOLLECTION):
    case FAILURE(ACTION_TYPES.CREATE_ISSUEINCOLLECTION):
    case FAILURE(ACTION_TYPES.UPDATE_ISSUEINCOLLECTION):
    case FAILURE(ACTION_TYPES.DELETE_ISSUEINCOLLECTION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ISSUEINCOLLECTIONS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ISSUEINCOLLECTION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ISSUEINCOLLECTION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ISSUEINCOLLECTION):
    case SUCCESS(ACTION_TYPES.UPDATE_ISSUEINCOLLECTION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ISSUEINCOLLECTION):
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

const apiUrl = 'api/issue-in-collections';
const apiSearchUrl = 'api/_search/issue-in-collections';

// Actions

export const getSearchEntities: ICrudSearchAction<IIssueInCollectionCl> = query => ({
  type: ACTION_TYPES.SEARCH_ISSUEINCOLLECTIONS,
  payload: axios.get<IIssueInCollectionCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IIssueInCollectionCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ISSUEINCOLLECTION_LIST,
  payload: axios.get<IIssueInCollectionCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IIssueInCollectionCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ISSUEINCOLLECTION,
    payload: axios.get<IIssueInCollectionCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IIssueInCollectionCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ISSUEINCOLLECTION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IIssueInCollectionCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ISSUEINCOLLECTION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IIssueInCollectionCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ISSUEINCOLLECTION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
