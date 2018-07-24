import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICountryCl } from 'app/shared/model/collector/country-cl.model';
import { getEntities as getCountries } from 'app/entities/collector/country-cl/country-cl.reducer';
import { ILicensorCl } from 'app/shared/model/collector/licensor-cl.model';
import { getEntities as getLicensors } from 'app/entities/collector/licensor-cl/licensor-cl.reducer';
import { IHistoryCl } from 'app/shared/model/collector/history-cl.model';
import { getEntities as getHistories } from 'app/entities/collector/history-cl/history-cl.reducer';
import { getEntity, updateEntity, createEntity, reset } from './personage-cl.reducer';
import { IPersonageCl } from 'app/shared/model/personage-cl.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface IPersonageClUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface IPersonageClUpdateState {
  isNew: boolean;
  countryId: number;
  licensorId: number;
  historyId: number;
}

export class PersonageClUpdate extends React.Component<IPersonageClUpdateProps, IPersonageClUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      countryId: 0,
      licensorId: 0,
      historyId: 0,
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getCountries();
    this.props.getLicensors();
    this.props.getHistories();
  }

  saveEntity = (event, errors, values) => {
    values.creationDate = new Date(values.creationDate);
    values.lastUpdate = new Date(values.lastUpdate);

    if (errors.length === 0) {
      const { personageEntity } = this.props;
      const entity = {
        ...personageEntity,
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
    this.props.history.push('/entity/personage-cl');
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

  licensorUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        licensorId: -1
      });
    } else {
      for (const i in this.props.licensors) {
        if (id === this.props.licensors[i].id.toString()) {
          this.setState({
            licensorId: this.props.licensors[i].id
          });
        }
      }
    }
  };

  historyUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        historyId: -1
      });
    } else {
      for (const i in this.props.histories) {
        if (id === this.props.histories[i].id.toString()) {
          this.setState({
            historyId: this.props.histories[i].id
          });
        }
      }
    }
  };

  render() {
    const { personageEntity, countries, licensors, histories, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="collectorApp.personage.home.createOrEditLabel">Create or edit a Personage</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : personageEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="personage-cl-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    Name
                  </Label>
                  <AvField id="personage-cl-name" type="text" name="name" />
                </AvGroup>
                <AvGroup>
                  <Label id="lastNameLabel" for="lastName">
                    Last Name
                  </Label>
                  <AvField id="personage-cl-lastName" type="text" name="lastName" />
                </AvGroup>
                <AvGroup>
                  <Label id="codeNameLabel" for="codeName">
                    Code Name
                  </Label>
                  <AvField id="personage-cl-codeName" type="text" name="codeName" />
                </AvGroup>
                <AvGroup>
                  <Label id="originalNameLabel" for="originalName">
                    Original Name
                  </Label>
                  <AvField id="personage-cl-originalName" type="text" name="originalName" />
                </AvGroup>
                <AvGroup>
                  <Label id="bioLabel" for="bio">
                    Bio
                  </Label>
                  <AvField
                    id="personage-cl-bio"
                    type="text"
                    name="bio"
                    validate={{
                      maxLength: { value: 5000, errorMessage: 'This field cannot be longer than {{ max }} characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="creationDateLabel" for="creationDate">
                    Creation Date
                  </Label>
                  <AvInput
                    id="personage-cl-creationDate"
                    type="datetime-local"
                    className="form-control"
                    name="creationDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.personageEntity.creationDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="lastUpdateLabel" for="lastUpdate">
                    Last Update
                  </Label>
                  <AvInput
                    id="personage-cl-lastUpdate"
                    type="datetime-local"
                    className="form-control"
                    name="lastUpdate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.personageEntity.lastUpdate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="country.id">Country</Label>
                  <AvInput id="personage-cl-country" type="select" className="form-control" name="countryId" onChange={this.countryUpdate}>
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
                <AvGroup>
                  <Label for="licensor.id">Licensor</Label>
                  <AvInput
                    id="personage-cl-licensor"
                    type="select"
                    className="form-control"
                    name="licensorId"
                    onChange={this.licensorUpdate}
                  >
                    <option value="" key="0" />
                    {licensors
                      ? licensors.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="history.id">History</Label>
                  <AvInput id="personage-cl-history" type="select" className="form-control" name="historyId" onChange={this.historyUpdate}>
                    <option value="" key="0" />
                    {histories
                      ? histories.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/personage-cl" replace color="info">
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
  countries: storeState.country.entities,
  licensors: storeState.licensor.entities,
  histories: storeState.history.entities,
  personageEntity: storeState.personage.entity,
  loading: storeState.personage.loading,
  updating: storeState.personage.updating
});

const mapDispatchToProps = {
  getCountries,
  getLicensors,
  getHistories,
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
)(PersonageClUpdate);
