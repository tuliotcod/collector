import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './personage-cl.reducer';
import { IPersonageCl } from 'app/shared/model/personage-cl.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPersonageClDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class PersonageClDetail extends React.Component<IPersonageClDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { personageEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Personage [<b>{personageEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{personageEntity.name}</dd>
            <dt>
              <span id="lastName">Last Name</span>
            </dt>
            <dd>{personageEntity.lastName}</dd>
            <dt>
              <span id="codeName">Code Name</span>
            </dt>
            <dd>{personageEntity.codeName}</dd>
            <dt>
              <span id="originalName">Original Name</span>
            </dt>
            <dd>{personageEntity.originalName}</dd>
            <dt>
              <span id="bio">Bio</span>
            </dt>
            <dd>{personageEntity.bio}</dd>
            <dt>
              <span id="creationDate">Creation Date</span>
            </dt>
            <dd>
              <TextFormat value={personageEntity.creationDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="lastUpdate">Last Update</span>
            </dt>
            <dd>
              <TextFormat value={personageEntity.lastUpdate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>Country</dt>
            <dd>{personageEntity.countryId ? personageEntity.countryId : ''}</dd>
            <dt>Licensor</dt>
            <dd>{personageEntity.licensorId ? personageEntity.licensorId : ''}</dd>
            <dt>History</dt>
            <dd>{personageEntity.historyId ? personageEntity.historyId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/personage-cl" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/personage-cl/${personageEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ personage }: IRootState) => ({
  personageEntity: personage.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PersonageClDetail);
