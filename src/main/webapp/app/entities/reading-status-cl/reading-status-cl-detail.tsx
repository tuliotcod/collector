import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './reading-status-cl.reducer';
import { IReadingStatusCl } from 'app/shared/model/reading-status-cl.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IReadingStatusClDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class ReadingStatusClDetail extends React.Component<IReadingStatusClDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { readingStatusEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            ReadingStatus [<b>{readingStatusEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="desc">Desc</span>
            </dt>
            <dd>{readingStatusEntity.desc}</dd>
          </dl>
          <Button tag={Link} to="/entity/reading-status-cl" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/reading-status-cl/${readingStatusEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ readingStatus }: IRootState) => ({
  readingStatusEntity: readingStatus.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ReadingStatusClDetail);
