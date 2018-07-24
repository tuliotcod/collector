import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICurrencyCl, defaultValue } from 'app/shared/model/currency-cl.model';

export const ACTION_TYPES = {
  SEARCH_CURRENCIES: 'currency/SEARCH_CURRENCIES',
  FETCH_CURRENCY_LIST: 'currency/FETCH_CURRENCY_LIST',
  FETCH_CURRENCY: 'currency/FETCH_CURRENCY',
  CREATE_CURRENCY: 'currency/CREATE_CURRENCY',
  UPDATE_CURRENCY: 'currency/UPDATE_CURRENCY',
  DELETE_CURRENCY: 'currency/DELETE_CURRENCY',
  RESET: 'currency/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICurrencyCl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type CurrencyClState = Readonly<typeof initialState>;

// Reducer

export default (state: CurrencyClState = initialState, action): CurrencyClState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_CURRENCIES):
    case REQUEST(ACTION_TYPES.FETCH_CURRENCY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CURRENCY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CURRENCY):
    case REQUEST(ACTION_TYPES.UPDATE_CURRENCY):
    case REQUEST(ACTION_TYPES.DELETE_CURRENCY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_CURRENCIES):
    case FAILURE(ACTION_TYPES.FETCH_CURRENCY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CURRENCY):
    case FAILURE(ACTION_TYPES.CREATE_CURRENCY):
    case FAILURE(ACTION_TYPES.UPDATE_CURRENCY):
    case FAILURE(ACTION_TYPES.DELETE_CURRENCY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_CURRENCIES):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CURRENCY_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CURRENCY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CURRENCY):
    case SUCCESS(ACTION_TYPES.UPDATE_CURRENCY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CURRENCY):
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

const apiUrl = 'api/currencies';
const apiSearchUrl = 'api/_search/currencies';

// Actions

export const getSearchEntities: ICrudSearchAction<ICurrencyCl> = query => ({
  type: ACTION_TYPES.SEARCH_CURRENCIES,
  payload: axios.get<ICurrencyCl>(`${apiSearchUrl}?query=` + query)
});

export const getEntities: ICrudGetAllAction<ICurrencyCl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CURRENCY_LIST,
  payload: axios.get<ICurrencyCl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ICurrencyCl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CURRENCY,
    payload: axios.get<ICurrencyCl>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICurrencyCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CURRENCY,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICurrencyCl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CURRENCY,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICurrencyCl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CURRENCY,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
