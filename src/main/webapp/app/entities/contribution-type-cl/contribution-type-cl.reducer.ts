import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IContributionTypeCl, defaultValue } from 'app/shared/model/contribution-type-cl.model';

export const ACTION_TYPES = {
  SEARCH_CONTRIBUTIONTYPES: 'contributionType/SEARCH_CONTRIBUTIONTYPES',
  FETCH_CONTRIBUTIONTYPE_LIST: 'contributionType/FETCH_CONTRIBUTIONTYPE_LIST',
  FETCH_CONTRIBUTIONTYPE: 'contributionType/FETCH_CONTRIBUTIONTYPE',
  CREATE_CONTRIBUTIONTYPE: 'contributionType/CREATE_CONTRIBUTIONTYPE',
  UPDATE_CONTRIBUTIONTYPE: 'contributionType/UPDATE_CONTRIBUTIONTYPE',
  DELETE_CONTRIBUTIONTYPE: 'contributionType/DELETE_CONTRIBUTIONTYPE',
  RESET: 'contributionType/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IContributionTypeCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ContributionTypeClState = Readonly<typeof initialState>;

// Reducer

export default (state: ContributionTypeClState = initialState, action): ContributionTypeClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_CONTRIBUTIONTYPES):
    case REQUEST(ACTION_TYPES.FETCH_CONTRIBUTIONTYPE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CONTRIBUTIONTYPE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CONTRIBUTIONTYPE):
    case REQUEST(ACTION_TYPES.UPDATE_CONTRIBUTIONTYPE):
    case REQUEST(ACTION_TYPES.DELETE_CONTRIBUTIONTYPE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_CONTRIBUTIONTYPES):
    case FAILURE(ACTION_TYPES.FETCH_CONTRIBUTIONTYPE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CONTRIBUTIONTYPE):
    case FAILURE(ACTION_TYPES.CREATE_CONTRIBUTIONTYPE):
    case FAILURE(ACTION_TYPES.UPDATE_CONTRIBUTIONTYPE):
    case FAILURE(ACTION_TYPES.DELETE_CONTRIBUTIONTYPE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_CONTRIBUTIONTYPES):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CONTRIBUTIONTYPE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CONTRIBUTIONTYPE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CONTRIBUTIONTYPE):
    case SUCCESS(ACTION_TYPES.UPDATE_CONTRIBUTIONTYPE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CONTRIBUTIONTYPE):
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

const apiUrl = 'api/contribution-types';
const apiSearchUrl = 'api/_search/contribution-types';

// Actions

export const getSearchEntities: ICrudSearchAction<IContributionTypeCl> = query => ({
  type: ACTION_TYPES.SEARCH_CONTRIBUTIONTYPES,
  payload: axios.get<IContributionTypeCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IContributionTypeCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CONTRIBUTIONTYPE_LIST,
  payload: axios.get<IContributionTypeCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IContributionTypeCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CONTRIBUTIONTYPE,
    payload: axios.get<IContributionTypeCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IContributionTypeCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CONTRIBUTIONTYPE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IContributionTypeCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CONTRIBUTIONTYPE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IContributionTypeCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CONTRIBUTIONTYPE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
