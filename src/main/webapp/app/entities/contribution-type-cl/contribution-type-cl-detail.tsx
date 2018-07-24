import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './contribution-type-cl.reducer';
import { IContributionTypeCl } from 'app/shared/model/contribution-type-cl.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IContributionTypeClDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class ContributionTypeClDetail extends React.Component<IContributionTypeClDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { contributionTypeEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            ContributionType [<b>{contributionTypeEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="desc">Desc</span>
            </dt>
            <dd>{contributionTypeEntity.desc}</dd>
          </dl>
          <Button tag={Link} to="/entity/contribution-type-cl" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/contribution-type-cl/${contributionTypeEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ contributionType }: IRootState) => ({
  contributionTypeEntity: contributionType.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ContributionTypeClDetail);
