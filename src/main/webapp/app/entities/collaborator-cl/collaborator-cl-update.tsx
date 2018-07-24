import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IIssueCl } from 'app/shared/model/collector/issue-cl.model';
import { getEntities as getIssues } from 'app/entities/collector/issue-cl/issue-cl.reducer';
import { IHistoryCl } from 'app/shared/model/collector/history-cl.model';
import { getEntities as getHistories } from 'app/entities/collector/history-cl/history-cl.reducer';
import { IArtistCl } from 'app/shared/model/collector/artist-cl.model';
import { getEntities as getArtists } from 'app/entities/collector/artist-cl/artist-cl.reducer';
import { IRoleCl } from 'app/shared/model/collector/role-cl.model';
import { getEntities as getRoles } from 'app/entities/collector/role-cl/role-cl.reducer';
import { getEntity, updateEntity, createEntity, reset } from './collaborator-cl.reducer';
import { ICollaboratorCl } from 'app/shared/model/collaborator-cl.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface ICollaboratorClUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface ICollaboratorClUpdateState {
  isNew: boolean;
  issueId: number;
  historyId: number;
  artistId: number;
  functionId: number;
}

export class CollaboratorClUpdate extends React.Component<ICollaboratorClUpdateProps, ICollaboratorClUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      issueId: 0,
      historyId: 0,
      artistId: 0,
      functionId: 0,
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
    this.props.getHistories();
    this.props.getArtists();
    this.props.getRoles();
  }

  saveEntity = (event, errors, values) => {
    values.creationDate = new Date(values.creationDate);
    values.lastUpdate = new Date(values.lastUpdate);

    if (errors.length === 0) {
      const { collaboratorEntity } = this.props;
      const entity = {
        ...collaboratorEntity,
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
    this.props.history.push('/entity/collaborator-cl');
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

  artistUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        artistId: -1
      });
    } else {
      for (const i in this.props.artists) {
        if (id === this.props.artists[i].id.toString()) {
          this.setState({
            artistId: this.props.artists[i].id
          });
        }
      }
    }
  };

  functionUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        functionId: -1
      });
    } else {
      for (const i in this.props.roles) {
        if (id === this.props.roles[i].id.toString()) {
          this.setState({
            functionId: this.props.roles[i].id
          });
        }
      }
    }
  };

  render() {
    const { collaboratorEntity, issues, histories, artists, roles, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="collectorApp.collaborator.home.createOrEditLabel">Create or edit a Collaborator</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : collaboratorEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="collaborator-cl-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="creationDateLabel" for="creationDate">
                    Creation Date
                  </Label>
                  <AvInput
                    id="collaborator-cl-creationDate"
                    type="datetime-local"
                    className="form-control"
                    name="creationDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.collaboratorEntity.creationDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="lastUpdateLabel" for="lastUpdate">
                    Last Update
                  </Label>
                  <AvInput
                    id="collaborator-cl-lastUpdate"
                    type="datetime-local"
                    className="form-control"
                    name="lastUpdate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.collaboratorEntity.lastUpdate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="issue.id">Issue</Label>
                  <AvInput id="collaborator-cl-issue" type="select" className="form-control" name="issueId" onChange={this.issueUpdate}>
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
                  <Label for="history.id">History</Label>
                  <AvInput
                    id="collaborator-cl-history"
                    type="select"
                    className="form-control"
                    name="historyId"
                    onChange={this.historyUpdate}
                  >
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
                <AvGroup>
                  <Label for="artist.id">Artist</Label>
                  <AvInput id="collaborator-cl-artist" type="select" className="form-control" name="artistId" onChange={this.artistUpdate}>
                    <option value="" key="0" />
                    {artists
                      ? artists.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="function.id">Function</Label>
                  <AvInput
                    id="collaborator-cl-function"
                    type="select"
                    className="form-control"
                    name="functionId"
                    onChange={this.functionUpdate}
                  >
                    <option value="" key="0" />
                    {roles
                      ? roles.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/collaborator-cl" replace color="info">
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
  histories: storeState.history.entities,
  artists: storeState.artist.entities,
  roles: storeState.role.entities,
  collaboratorEntity: storeState.collaborator.entity,
  loading: storeState.collaborator.loading,
  updating: storeState.collaborator.updating
});

const mapDispatchToProps = {
  getIssues,
  getHistories,
  getArtists,
  getRoles,
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
)(CollaboratorClUpdate);
