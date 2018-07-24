import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IContributionTypeCl } from 'app/shared/model/collector/contribution-type-cl.model';
import { getEntities as getContributionTypes } from 'app/entities/collector/contribution-type-cl/contribution-type-cl.reducer';
import { getEntity, updateEntity, createEntity, reset } from './contribution-cl.reducer';
import { IContributionCl } from 'app/shared/model/contribution-cl.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface IContributionClUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface IContributionClUpdateState {
  isNew: boolean;
  typeId: number;
}

export class ContributionClUpdate extends React.Component<IContributionClUpdateProps, IContributionClUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      typeId: 0,
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getContributionTypes();
  }

  saveEntity = (event, errors, values) => {
    values.creationDate = new Date(values.creationDate);

    if (errors.length === 0) {
      const { contributionEntity } = this.props;
      const entity = {
        ...contributionEntity,
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
    this.props.history.push('/entity/contribution-cl');
  };

  typeUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        typeId: -1
      });
    } else {
      for (const i in this.props.contributionTypes) {
        if (id === this.props.contributionTypes[i].id.toString()) {
          this.setState({
            typeId: this.props.contributionTypes[i].id
          });
        }
      }
    }
  };

  render() {
    const { contributionEntity, contributionTypes, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="collectorApp.contribution.home.createOrEditLabel">Create or edit a Contribution</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : contributionEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="contribution-cl-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="creationDateLabel" for="creationDate">
                    Creation Date
                  </Label>
                  <AvInput
                    id="contribution-cl-creationDate"
                    type="datetime-local"
                    className="form-control"
                    name="creationDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.contributionEntity.creationDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="type.id">Type</Label>
                  <AvInput id="contribution-cl-type" type="select" className="form-control" name="typeId" onChange={this.typeUpdate}>
                    <option value="" key="0" />
                    {contributionTypes
                      ? contributionTypes.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/contribution-cl" replace color="info">
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
  contributionTypes: storeState.contributionType.entities,
  contributionEntity: storeState.contribution.entity,
  loading: storeState.contribution.loading,
  updating: storeState.contribution.updating
});

const mapDispatchToProps = {
  getContributionTypes,
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
)(ContributionClUpdate);
