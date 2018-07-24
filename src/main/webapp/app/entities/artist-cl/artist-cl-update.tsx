import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICountryCl } from 'app/shared/model/collector/country-cl.model';
import { getEntities as getCountries } from 'app/entities/collector/country-cl/country-cl.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './artist-cl.reducer';
import { IArtistCl } from 'app/shared/model/artist-cl.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface IArtistClUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface IArtistClUpdateState {
  isNew: boolean;
  countryId: number;
}

export class ArtistClUpdate extends React.Component<IArtistClUpdateProps, IArtistClUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
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

    this.props.getCountries();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    values.birthday = new Date(values.birthday);
    values.dateOfDeath = new Date(values.dateOfDeath);
    values.creationDate = new Date(values.creationDate);
    values.lastUpdate = new Date(values.lastUpdate);

    if (errors.length === 0) {
      const { artistEntity } = this.props;
      const entity = {
        ...artistEntity,
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
    this.props.history.push('/entity/artist-cl');
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
    const { artistEntity, countries, loading, updating } = this.props;
    const { isNew } = this.state;

    const { image, imageContentType } = artistEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="collectorApp.artist.home.createOrEditLabel">Create or edit a Artist</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : artistEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="artist-cl-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    Name
                  </Label>
                  <AvField id="artist-cl-name" type="text" name="name" />
                </AvGroup>
                <AvGroup>
                  <Label id="lastNameLabel" for="lastName">
                    Last Name
                  </Label>
                  <AvField id="artist-cl-lastName" type="text" name="lastName" />
                </AvGroup>
                <AvGroup>
                  <Label id="nicknameLabel" for="nickname">
                    Nickname
                  </Label>
                  <AvField id="artist-cl-nickname" type="text" name="nickname" />
                </AvGroup>
                <AvGroup>
                  <Label id="birthdayLabel" for="birthday">
                    Birthday
                  </Label>
                  <AvInput
                    id="artist-cl-birthday"
                    type="datetime-local"
                    className="form-control"
                    name="birthday"
                    value={isNew ? null : convertDateTimeFromServer(this.props.artistEntity.birthday)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="dateOfDeathLabel" for="dateOfDeath">
                    Date Of Death
                  </Label>
                  <AvInput
                    id="artist-cl-dateOfDeath"
                    type="datetime-local"
                    className="form-control"
                    name="dateOfDeath"
                    value={isNew ? null : convertDateTimeFromServer(this.props.artistEntity.dateOfDeath)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="bioLabel" for="bio">
                    Bio
                  </Label>
                  <AvField
                    id="artist-cl-bio"
                    type="text"
                    name="bio"
                    validate={{
                      maxLength: { value: 5000, errorMessage: 'This field cannot be longer than {{ max }} characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="imageLabel" for="image">
                      Image
                    </Label>
                    <br />
                    {image ? (
                      <div>
                        <a onClick={openFile(imageContentType, image)}>
                          <img src={`data:${imageContentType};base64,${image}`} style={{ maxHeight: '100px' }} />
                        </a>
                        <br />
                        <Row>
                          <Col md="11">
                            <span>
                              {imageContentType}, {byteSize(image)}
                            </span>
                          </Col>
                          <Col md="1">
                            <Button color="danger" onClick={this.clearBlob('image')}>
                              <FontAwesomeIcon icon="times-circle" />
                            </Button>
                          </Col>
                        </Row>
                      </div>
                    ) : null}
                    <input id="file_image" type="file" onChange={this.onBlobChange(true, 'image')} accept="image/*" />
                    <AvInput type="hidden" name="image" value={image} />
                  </AvGroup>
                </AvGroup>
                <AvGroup>
                  <Label id="creationDateLabel" for="creationDate">
                    Creation Date
                  </Label>
                  <AvInput
                    id="artist-cl-creationDate"
                    type="datetime-local"
                    className="form-control"
                    name="creationDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.artistEntity.creationDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="lastUpdateLabel" for="lastUpdate">
                    Last Update
                  </Label>
                  <AvInput
                    id="artist-cl-lastUpdate"
                    type="datetime-local"
                    className="form-control"
                    name="lastUpdate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.artistEntity.lastUpdate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="country.id">Country</Label>
                  <AvInput id="artist-cl-country" type="select" className="form-control" name="countryId" onChange={this.countryUpdate}>
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
                <Button tag={Link} id="cancel-save" to="/entity/artist-cl" replace color="info">
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
  artistEntity: storeState.artist.entity,
  loading: storeState.artist.loading,
  updating: storeState.artist.updating
});

const mapDispatchToProps = {
  getCountries,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ArtistClUpdate);
