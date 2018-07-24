import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './collector-user-cl.reducer';
import { ICollectorUserCl } from 'app/shared/model/collector-user-cl.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface ICollectorUserClUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface ICollectorUserClUpdateState {
  isNew: boolean;
}

export class CollectorUserClUpdate extends React.Component<ICollectorUserClUpdateProps, ICollectorUserClUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }
  }

  saveEntity = (event, errors, values) => {
    values.birthday = new Date(values.birthday);
    values.creationDate = new Date(values.creationDate);
    values.lastUpdate = new Date(values.lastUpdate);

    if (errors.length === 0) {
      const { collectorUserEntity } = this.props;
      const entity = {
        ...collectorUserEntity,
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
    this.props.history.push('/entity/collector-user-cl');
  };

  render() {
    const { collectorUserEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="collectorApp.collectorUser.home.createOrEditLabel">Create or edit a CollectorUser</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : collectorUserEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="collector-user-cl-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="usernameLabel" for="username">
                    Username
                  </Label>
                  <AvField id="collector-user-cl-username" type="text" name="username" />
                </AvGroup>
                <AvGroup>
                  <Label id="emailLabel" for="email">
                    Email
                  </Label>
                  <AvField id="collector-user-cl-email" type="text" name="email" />
                </AvGroup>
                <AvGroup>
                  <Label id="passwordLabel" for="password">
                    Password
                  </Label>
                  <AvField id="collector-user-cl-password" type="text" name="password" />
                </AvGroup>
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    Name
                  </Label>
                  <AvField id="collector-user-cl-name" type="text" name="name" />
                </AvGroup>
                <AvGroup>
                  <Label id="lastNameLabel" for="lastName">
                    Last Name
                  </Label>
                  <AvField id="collector-user-cl-lastName" type="text" name="lastName" />
                </AvGroup>
                <AvGroup>
                  <Label id="birthdayLabel" for="birthday">
                    Birthday
                  </Label>
                  <AvInput
                    id="collector-user-cl-birthday"
                    type="datetime-local"
                    className="form-control"
                    name="birthday"
                    value={isNew ? null : convertDateTimeFromServer(this.props.collectorUserEntity.birthday)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="genderLabel" for="gender">
                    Gender
                  </Label>
                  <AvField id="collector-user-cl-gender" type="number" className="form-control" name="gender" />
                </AvGroup>
                <AvGroup>
                  <Label id="creationDateLabel" for="creationDate">
                    Creation Date
                  </Label>
                  <AvInput
                    id="collector-user-cl-creationDate"
                    type="datetime-local"
                    className="form-control"
                    name="creationDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.collectorUserEntity.creationDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="lastUpdateLabel" for="lastUpdate">
                    Last Update
                  </Label>
                  <AvInput
                    id="collector-user-cl-lastUpdate"
                    type="datetime-local"
                    className="form-control"
                    name="lastUpdate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.collectorUserEntity.lastUpdate)}
                  />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/collector-user-cl" replace color="info">
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
  collectorUserEntity: storeState.collectorUser.entity,
  loading: storeState.collectorUser.loading,
  updating: storeState.collectorUser.updating
});

const mapDispatchToProps = {
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
)(CollectorUserClUpdate);
