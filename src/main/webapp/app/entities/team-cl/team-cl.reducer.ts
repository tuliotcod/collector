import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITeamCl, defaultValue } from 'app/shared/model/team-cl.model';

export const ACTION_TYPES = {
  SEARCH_TEAMS: 'team/SEARCH_TEAMS',
  FETCH_TEAM_LIST: 'team/FETCH_TEAM_LIST',
  FETCH_TEAM: 'team/FETCH_TEAM',
  CREATE_TEAM: 'team/CREATE_TEAM',
  UPDATE_TEAM: 'team/UPDATE_TEAM',
  DELETE_TEAM: 'team/DELETE_TEAM',
  RESET: 'team/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITeamCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type TeamClState = Readonly<typeof initialState>;

// Reducer

export default (state: TeamClState = initialState, action): TeamClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_TEAMS):
    case REQUEST(ACTION_TYPES.FETCH_TEAM_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TEAM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_TEAM):
    case REQUEST(ACTION_TYPES.UPDATE_TEAM):
    case REQUEST(ACTION_TYPES.DELETE_TEAM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_TEAMS):
    case FAILURE(ACTION_TYPES.FETCH_TEAM_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TEAM):
    case FAILURE(ACTION_TYPES.CREATE_TEAM):
    case FAILURE(ACTION_TYPES.UPDATE_TEAM):
    case FAILURE(ACTION_TYPES.DELETE_TEAM):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_TEAMS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_TEAM_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_TEAM):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_TEAM):
    case SUCCESS(ACTION_TYPES.UPDATE_TEAM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_TEAM):
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

const apiUrl = 'api/teams';
const apiSearchUrl = 'api/_search/teams';

// Actions

export const getSearchEntities: ICrudSearchAction<ITeamCl> = query => ({
  type: ACTION_TYPES.SEARCH_TEAMS,
  payload: axios.get<ITeamCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<ITeamCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_TEAM_LIST,
  payload: axios.get<ITeamCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ITeamCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TEAM,
    payload: axios.get<ITeamCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ITeamCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TEAM,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ITeamCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TEAM,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITeamCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TEAM,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
