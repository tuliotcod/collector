import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './collaborator-cl.reducer';
import { ICollaboratorCl } from 'app/shared/model/collaborator-cl.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICollaboratorClDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class CollaboratorClDetail extends React.Component<ICollaboratorClDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { collaboratorEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Collaborator [<b>{collaboratorEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="creationDate">Creation Date</span>
            </dt>
            <dd>
              <TextFormat value={collaboratorEntity.creationDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="lastUpdate">Last Update</span>
            </dt>
            <dd>
              <TextFormat value={collaboratorEntity.lastUpdate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>Issue</dt>
            <dd>{collaboratorEntity.issueId ? collaboratorEntity.issueId : ''}</dd>
            <dt>History</dt>
            <dd>{collaboratorEntity.historyId ? collaboratorEntity.historyId : ''}</dd>
            <dt>Artist</dt>
            <dd>{collaboratorEntity.artistId ? collaboratorEntity.artistId : ''}</dd>
            <dt>Function</dt>
            <dd>{collaboratorEntity.functionId ? collaboratorEntity.functionId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/collaborator-cl" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/collaborator-cl/${collaboratorEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ collaborator }: IRootState) => ({
  collaboratorEntity: collaborator.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CollaboratorClDetail);
