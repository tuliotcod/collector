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
import { IIssueStatusCl } from 'app/shared/model/collector/issue-status-cl.model';
import { getEntities as getIssueStatuses } from 'app/entities/collector/issue-status-cl/issue-status-cl.reducer';
import { IReadingStatusCl } from 'app/shared/model/collector/reading-status-cl.model';
import { getEntities as getReadingStatuses } from 'app/entities/collector/reading-status-cl/reading-status-cl.reducer';
import { ICollectionCl } from 'app/shared/model/collector/collection-cl.model';
import { getEntities as getCollections } from 'app/entities/collector/collection-cl/collection-cl.reducer';
import { getEntity, updateEntity, createEntity, reset } from './issue-in-collection-cl.reducer';
import { IIssueInCollectionCl } from 'app/shared/model/issue-in-collection-cl.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface IIssueInCollectionClUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface IIssueInCollectionClUpdateState {
  isNew: boolean;
  issueId: number;
  issueStatusId: number;
  readingStatusId: number;
  collectionId: number;
}

export class IssueInCollectionClUpdate extends React.Component<IIssueInCollectionClUpdateProps, IIssueInCollectionClUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      issueId: 0,
      issueStatusId: 0,
      readingStatusId: 0,
      collectionId: 0,
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
    this.props.getIssueStatuses();
    this.props.getReadingStatuses();
    this.props.getCollections();
  }

  saveEntity = (event, errors, values) => {
    values.creationDate = new Date(values.creationDate);
    values.lastUpdate = new Date(values.lastUpdate);

    if (errors.length === 0) {
      const { issueInCollectionEntity } = this.props;
      const entity = {
        ...issueInCollectionEntity,
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
    this.props.history.push('/entity/issue-in-collection-cl');
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

  issueStatusUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        issueStatusId: -1
      });
    } else {
      for (const i in this.props.issueStatuses) {
        if (id === this.props.issueStatuses[i].id.toString()) {
          this.setState({
            issueStatusId: this.props.issueStatuses[i].id
          });
        }
      }
    }
  };

  readingStatusUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        readingStatusId: -1
      });
    } else {
      for (const i in this.props.readingStatuses) {
        if (id === this.props.readingStatuses[i].id.toString()) {
          this.setState({
            readingStatusId: this.props.readingStatuses[i].id
          });
        }
      }
    }
  };

  collectionUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        collectionId: -1
      });
    } else {
      for (const i in this.props.collections) {
        if (id === this.props.collections[i].id.toString()) {
          this.setState({
            collectionId: this.props.collections[i].id
          });
        }
      }
    }
  };

  render() {
    const { issueInCollectionEntity, issues, issueStatuses, readingStatuses, collections, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="collectorApp.issueInCollection.home.createOrEditLabel">Create or edit a IssueInCollection</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : issueInCollectionEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="issue-in-collection-cl-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="priceLabel" for="price">
                    Price
                  </Label>
                  <AvField id="issue-in-collection-cl-price" type="text" name="price" />
                </AvGroup>
                <AvGroup>
                  <Label id="amountLabel" for="amount">
                    Amount
                  </Label>
                  <AvField id="issue-in-collection-cl-amount" type="number" className="form-control" name="amount" />
                </AvGroup>
                <AvGroup>
                  <Label id="notesLabel" for="notes">
                    Notes
                  </Label>
                  <AvField
                    id="issue-in-collection-cl-notes"
                    type="text"
                    name="notes"
                    validate={{
                      maxLength: { value: 75, errorMessage: 'This field cannot be longer than {{ max }} characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="creationDateLabel" for="creationDate">
                    Creation Date
                  </Label>
                  <AvInput
                    id="issue-in-collection-cl-creationDate"
                    type="datetime-local"
                    className="form-control"
                    name="creationDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.issueInCollectionEntity.creationDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="lastUpdateLabel" for="lastUpdate">
                    Last Update
                  </Label>
                  <AvInput
                    id="issue-in-collection-cl-lastUpdate"
                    type="datetime-local"
                    className="form-control"
                    name="lastUpdate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.issueInCollectionEntity.lastUpdate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="issue.id">Issue</Label>
                  <AvInput
                    id="issue-in-collection-cl-issue"
                    type="select"
                    className="form-control"
                    name="issueId"
                    onChange={this.issueUpdate}
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
                <AvGroup>
                  <Label for="issueStatus.id">Issue Status</Label>
                  <AvInput
                    id="issue-in-collection-cl-issueStatus"
                    type="select"
                    className="form-control"
                    name="issueStatusId"
                    onChange={this.issueStatusUpdate}
                  >
                    <option value="" key="0" />
                    {issueStatuses
                      ? issueStatuses.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="readingStatus.id">Reading Status</Label>
                  <AvInput
                    id="issue-in-collection-cl-readingStatus"
                    type="select"
                    className="form-control"
                    name="readingStatusId"
                    onChange={this.readingStatusUpdate}
                  >
                    <option value="" key="0" />
                    {readingStatuses
                      ? readingStatuses.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="collection.id">Collection</Label>
                  <AvInput
                    id="issue-in-collection-cl-collection"
                    type="select"
                    className="form-control"
                    name="collectionId"
                    onChange={this.collectionUpdate}
                  >
                    <option value="" key="0" />
                    {collections
                      ? collections.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/issue-in-collection-cl" replace color="info">
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
  issueStatuses: storeState.issueStatus.entities,
  readingStatuses: storeState.readingStatus.entities,
  collections: storeState.collection.entities,
  issueInCollectionEntity: storeState.issueInCollection.entity,
  loading: storeState.issueInCollection.loading,
  updating: storeState.issueInCollection.updating
});

const mapDispatchToProps = {
  getIssues,
  getIssueStatuses,
  getReadingStatuses,
  getCollections,
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
)(IssueInCollectionClUpdate);
