import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './history-cl.reducer';
import { IHistoryCl } from 'app/shared/model/history-cl.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IHistoryClDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class HistoryClDetail extends React.Component<IHistoryClDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { historyEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            History [<b>{historyEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="order">Order</span>
            </dt>
            <dd>{historyEntity.order}</dd>
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{historyEntity.name}</dd>
            <dt>
              <span id="pages">Pages</span>
            </dt>
            <dd>{historyEntity.pages}</dd>
            <dt>
              <span id="desc">Desc</span>
            </dt>
            <dd>{historyEntity.desc}</dd>
            <dt>
              <span id="part">Part</span>
            </dt>
            <dd>{historyEntity.part}</dd>
            <dt>
              <span id="creationDate">Creation Date</span>
            </dt>
            <dd>
              <TextFormat value={historyEntity.creationDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="lastUpdate">Last Update</span>
            </dt>
            <dd>
              <TextFormat value={historyEntity.lastUpdate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>Issue</dt>
            <dd>{historyEntity.issueId ? historyEntity.issueId : ''}</dd>
            <dt>Arc</dt>
            <dd>{historyEntity.arcId ? historyEntity.arcId : ''}</dd>
            <dt>Original Issue</dt>
            <dd>{historyEntity.originalIssueId ? historyEntity.originalIssueId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/history-cl" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/history-cl/${historyEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ history }: IRootState) => ({
  historyEntity: history.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(HistoryClDetail);
