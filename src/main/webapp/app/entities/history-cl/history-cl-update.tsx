import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IIssueCl } from 'app/shared/model/collector/issue-cl.model';
import { getEntities as getIssues } from 'app/entities/collector/issue-cl/issue-cl.reducer';
import { IArcCl } from 'app/shared/model/collector/arc-cl.model';
import { getEntities as getArcs } from 'app/entities/collector/arc-cl/arc-cl.reducer';
import { getEntity, updateEntity, createEntity, reset } from './history-cl.reducer';
import { IHistoryCl } from 'app/shared/model/history-cl.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface IHistoryClUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface IHistoryClUpdateState {
  isNew: boolean;
  issueId: number;
  originalIssueId: number;
  arcId: number;
}

export class HistoryClUpdate extends React.Component<IHistoryClUpdateProps, IHistoryClUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      issueId: 0,
      originalIssueId: 0,
      arcId: 0,
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getIssues();
    this.props.getArcs();
  }

  saveEntity = (event, errors, values) => {
    values.creationDate = new Date(values.creationDate);
    values.lastUpdate = new Date(values.lastUpdate);

    if (errors.length === 0) {
      const { historyEntity } = this.props;
      const entity = {
        ...historyEntity,
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
    this.props.history.push('/entity/history-cl');
  };

  issueUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        issueId: -1
      });
    } else {
      for (const i in this.props.issues) {
        if (id === this.props.issues[i].id.toString()) {
          this.setState({
            issueId: this.props.issues[i].id
          });
        }
      }
    }
  };

  arcUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        arcId: -1
      });
    } else {
      for (const i in this.props.arcs) {
        if (id === this.props.arcs[i].id.toString()) {
          this.setState({
            arcId: this.props.arcs[i].id
          });
        }
      }
    }
  };

  originalIssueUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        originalIssueId: -1
      });
    } else {
      for (const i in this.props.issues) {
        if (id === this.props.issues[i].id.toString()) {
          this.setState({
            originalIssueId: this.props.issues[i].id
          });
        }
      }
    }
  };

  render() {
    const { historyEntity, issues, arcs, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="collectorApp.history.home.createOrEditLabel">Create or edit a History</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : historyEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="history-cl-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="orderLabel" for="order">
                    Order
                  </Label>
                  <AvField id="history-cl-order" type="number" className="form-control" name="order" />
                </AvGroup>
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    Name
                  </Label>
                  <AvField id="history-cl-name" type="text" name="name" />
                </AvGroup>
                <AvGroup>
                  <Label id="pagesLabel" for="pages">
                    Pages
                  </Label>
                  <AvField id="history-cl-pages" type="number" className="form-control" name="pages" />
                </AvGroup>
                <AvGroup>
                  <Label id="descLabel" for="desc">
                    Desc
                  </Label>
                  <AvField
                    id="history-cl-desc"
                    type="text"
                    name="desc"
                    validate={{
                      maxLength: { value: 500, errorMessage: 'This field cannot be longer than {{ max }} characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="partLabel" for="part">
                    Part
                  </Label>
                  <AvField id="history-cl-part" type="number" className="form-control" name="part" />
                </AvGroup>
                <AvGroup>
                  <Label id="creationDateLabel" for="creationDate">
                    Creation Date
                  </Label>
                  <AvInput
                    id="history-cl-creationDate"
                    type="datetime-local"
                    className="form-control"
                    name="creationDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.historyEntity.creationDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="lastUpdateLabel" for="lastUpdate">
                    Last Update
                  </Label>
                  <AvInput
                    id="history-cl-lastUpdate"
                    type="datetime-local"
                    className="form-control"
                    name="lastUpdate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.historyEntity.lastUpdate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="issue.id">Issue</Label>
                  <AvInput id="history-cl-issue" type="select" className="form-control" name="issueId" onChange={this.issueUpdate}>
                    <option value="" key="0" />
                    {issues
                      ? issues.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="arc.id">Arc</Label>
                  <AvInput id="history-cl-arc" type="select" className="form-control" name="arcId" onChange={this.arcUpdate}>
                    <option value="" key="0" />
                    {arcs
                      ? arcs.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="originalIssue.id">Original Issue</Label>
                  <AvInput
                    id="history-cl-originalIssue"
                    type="select"
                    className="form-control"
                    name="originalIssueId"
                    onChange={this.originalIssueUpdate}
                  >
                    <option value="" key="0" />
                    {issues
                      ? issues.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/history-cl" replace color="info">
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
  issues: storeState.issue.entities,
  arcs: storeState.arc.entities,
  historyEntity: storeState.history.entity,
  loading: storeState.history.loading,
  updating: storeState.history.updating
});

const mapDispatchToProps = {
  getIssues,
  getArcs,
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
)(HistoryClUpdate);
