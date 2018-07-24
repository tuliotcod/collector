import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IStateCl } from 'app/shared/model/collector/state-cl.model';
import { getEntities as getStates } from 'app/entities/collector/state-cl/state-cl.reducer';
import { ICountryCl } from 'app/shared/model/collector/country-cl.model';
import { getEntities as getCountries } from 'app/entities/collector/country-cl/country-cl.reducer';
import { getEntity, updateEntity, createEntity, reset } from './address-cl.reducer';
import { IAddressCl } from 'app/shared/model/address-cl.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface IAddressClUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface IAddressClUpdateState {
  isNew: boolean;
  stateId: number;
  countryId: number;
}

export class AddressClUpdate extends React.Component<IAddressClUpdateProps, IAddressClUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      stateId: 0,
      countryId: 0,
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getStates();
    this.props.getCountries();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { addressEntity } = this.props;
      const entity = {
        ...addressEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
      this.handleClose();
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/address-cl');
  };

  stateUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        stateId: -1
      });
    } else {
      for (const i in this.props.states) {
        if (id === this.props.states[i].id.toString()) {
          this.setState({
            stateId: this.props.states[i].id
          });
        }
      }
    }
  };

  countryUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        countryId: -1
      });
    } else {
      for (const i in this.props.countries) {
        if (id === this.props.countries[i].id.toString()) {
          this.setState({
            countryId: this.props.countries[i].id
          });
        }
      }
    }
  };

  render() {
    const { addressEntity, states, countries, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="collectorApp.address.home.createOrEditLabel">Create or edit a Address</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : addressEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="address-cl-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="cityLabel" for="city">
                    City
                  </Label>
                  <AvField id="address-cl-city" type="text" name="city" />
                </AvGroup>
                <AvGroup>
                  <Label for="state.id">State</Label>
                  <AvInput id="address-cl-state" type="select" className="form-control" name="stateId" onChange={this.stateUpdate}>
                    <option value="" key="0" />
                    {states
                      ? states.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="country.id">Country</Label>
                  <AvInput id="address-cl-country" type="select" className="form-control" name="countryId" onChange={this.countryUpdate}>
                    <option value="" key="0" />
                    {countries
                      ? countries.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/address-cl" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />&nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />&nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  states: storeState.state.entities,
  countries: storeState.country.entities,
  addressEntity: storeState.address.entity,
  loading: storeState.address.loading,
  updating: storeState.address.updating
});

const mapDispatchToProps = {
  getStates,
  getCountries,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AddressClUpdate);
