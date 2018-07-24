import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './issue-in-collection-cl.reducer';
import { IIssueInCollectionCl } from 'app/shared/model/issue-in-collection-cl.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IIssueInCollectionClDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class IssueInCollectionClDetail extends React.Component<IIssueInCollectionClDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { issueInCollectionEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            IssueInCollection [<b>{issueInCollectionEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="price">Price</span>
            </dt>
            <dd>{issueInCollectionEntity.price}</dd>
            <dt>
              <span id="amount">Amount</span>
            </dt>
            <dd>{issueInCollectionEntity.amount}</dd>
            <dt>
              <span id="notes">Notes</span>
            </dt>
            <dd>{issueInCollectionEntity.notes}</dd>
            <dt>
              <span id="creationDate">Creation Date</span>
            </dt>
            <dd>
              <TextFormat value={issueInCollectionEntity.creationDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="lastUpdate">Last Update</span>
            </dt>
            <dd>
              <TextFormat value={issueInCollectionEntity.lastUpdate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>Issue</dt>
            <dd>{issueInCollectionEntity.issueId ? issueInCollectionEntity.issueId : ''}</dd>
            <dt>Issue Status</dt>
            <dd>{issueInCollectionEntity.issueStatusId ? issueInCollectionEntity.issueStatusId : ''}</dd>
            <dt>Reading Status</dt>
            <dd>{issueInCollectionEntity.readingStatusId ? issueInCollectionEntity.readingStatusId : ''}</dd>
            <dt>Collection</dt>
            <dd>{issueInCollectionEntity.collectionId ? issueInCollectionEntity.collectionId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/issue-in-collection-cl" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/issue-in-collection-cl/${issueInCollectionEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ issueInCollection }: IRootState) => ({
  issueInCollectionEntity: issueInCollection.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(IssueInCollectionClDetail);
