import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './collector-user-cl.reducer';
import { ICollectorUserCl } from 'app/shared/model/collector-user-cl.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICollectorUserClDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class CollectorUserClDetail extends React.Component<ICollectorUserClDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { collectorUserEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            CollectorUser [<b>{collectorUserEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="username">Username</span>
            </dt>
            <dd>{collectorUserEntity.username}</dd>
            <dt>
              <span id="email">Email</span>
            </dt>
            <dd>{collectorUserEntity.email}</dd>
            <dt>
              <span id="password">Password</span>
            </dt>
            <dd>{collectorUserEntity.password}</dd>
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{collectorUserEntity.name}</dd>
            <dt>
              <span id="lastName">Last Name</span>
            </dt>
            <dd>{collectorUserEntity.lastName}</dd>
            <dt>
              <span id="birthday">Birthday</span>
            </dt>
            <dd>
              <TextFormat value={collectorUserEntity.birthday} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="gender">Gender</span>
            </dt>
            <dd>{collectorUserEntity.gender}</dd>
            <dt>
              <span id="creationDate">Creation Date</span>
            </dt>
            <dd>
              <TextFormat value={collectorUserEntity.creationDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="lastUpdate">Last Update</span>
            </dt>
            <dd>
              <TextFormat value={collectorUserEntity.lastUpdate} type="date" format={APP_DATE_FORMAT} />
            </dd>
          </dl>
          <Button tag={Link} to="/entity/collector-user-cl" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/collector-user-cl/${collectorUserEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ collectorUser }: IRootState) => ({
  collectorUserEntity: collectorUser.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CollectorUserClDetail);
