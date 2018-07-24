import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './contribution-cl.reducer';
import { IContributionCl } from 'app/shared/model/contribution-cl.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IContributionClDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class ContributionClDetail extends React.Component<IContributionClDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { contributionEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Contribution [<b>{contributionEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="creationDate">Creation Date</span>
            </dt>
            <dd>
              <TextFormat value={contributionEntity.creationDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>Type</dt>
            <dd>{contributionEntity.typeId ? contributionEntity.typeId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/contribution-cl" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/contribution-cl/${contributionEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ contribution }: IRootState) => ({
  contributionEntity: contribution.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ContributionClDetail);
