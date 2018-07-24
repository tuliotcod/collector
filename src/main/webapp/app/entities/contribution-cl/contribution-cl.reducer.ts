import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IContributionCl, defaultValue } from 'app/shared/model/contribution-cl.model';

export const ACTION_TYPES = {
  SEARCH_CONTRIBUTIONS: 'contribution/SEARCH_CONTRIBUTIONS',
  FETCH_CONTRIBUTION_LIST: 'contribution/FETCH_CONTRIBUTION_LIST',
  FETCH_CONTRIBUTION: 'contribution/FETCH_CONTRIBUTION',
  CREATE_CONTRIBUTION: 'contribution/CREATE_CONTRIBUTION',
  UPDATE_CONTRIBUTION: 'contribution/UPDATE_CONTRIBUTION',
  DELETE_CONTRIBUTION: 'contribution/DELETE_CONTRIBUTION',
  RESET: 'contribution/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IContributionCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ContributionClState = Readonly<typeof initialState>;

// Reducer

export default (state: ContributionClState = initialState, action): ContributionClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_CONTRIBUTIONS):
    case REQUEST(ACTION_TYPES.FETCH_CONTRIBUTION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CONTRIBUTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CONTRIBUTION):
    case REQUEST(ACTION_TYPES.UPDATE_CONTRIBUTION):
    case REQUEST(ACTION_TYPES.DELETE_CONTRIBUTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_CONTRIBUTIONS):
    case FAILURE(ACTION_TYPES.FETCH_CONTRIBUTION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CONTRIBUTION):
    case FAILURE(ACTION_TYPES.CREATE_CONTRIBUTION):
    case FAILURE(ACTION_TYPES.UPDATE_CONTRIBUTION):
    case FAILURE(ACTION_TYPES.DELETE_CONTRIBUTION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_CONTRIBUTIONS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CONTRIBUTION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CONTRIBUTION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CONTRIBUTION):
    case SUCCESS(ACTION_TYPES.UPDATE_CONTRIBUTION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CONTRIBUTION):
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

const apiUrl = 'api/contributions';
const apiSearchUrl = 'api/_search/contributions';

// Actions

export const getSearchEntities: ICrudSearchAction<IContributionCl> = query => ({
  type: ACTION_TYPES.SEARCH_CONTRIBUTIONS,
  payload: axios.get<IContributionCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<IContributionCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CONTRIBUTION_LIST,
  payload: axios.get<IContributionCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IContributionCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CONTRIBUTION,
    payload: axios.get<IContributionCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IContributionCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CONTRIBUTION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IContributionCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CONTRIBUTION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IContributionCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CONTRIBUTION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
