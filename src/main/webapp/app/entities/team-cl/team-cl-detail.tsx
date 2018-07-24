import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './team-cl.reducer';
import { ITeamCl } from 'app/shared/model/team-cl.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITeamClDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class TeamClDetail extends React.Component<ITeamClDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { teamEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Team [<b>{teamEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{teamEntity.name}</dd>
            <dt>
              <span id="originalName">Original Name</span>
            </dt>
            <dd>{teamEntity.originalName}</dd>
            <dt>
              <span id="bio">Bio</span>
            </dt>
            <dd>{teamEntity.bio}</dd>
            <dt>
              <span id="creationDate">Creation Date</span>
            </dt>
            <dd>
              <TextFormat value={teamEntity.creationDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="lastUpdate">Last Update</span>
            </dt>
            <dd>
              <TextFormat value={teamEntity.lastUpdate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>Country</dt>
            <dd>{teamEntity.countryId ? teamEntity.countryId : ''}</dd>
            <dt>Licensor</dt>
            <dd>{teamEntity.licensorId ? teamEntity.licensorId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/team-cl" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/team-cl/${teamEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ team }: IRootState) => ({
  teamEntity: team.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TeamClDetail);
