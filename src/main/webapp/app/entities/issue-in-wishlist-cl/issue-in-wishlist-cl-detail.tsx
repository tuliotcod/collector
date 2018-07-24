import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './issue-in-wishlist-cl.reducer';
import { IIssueInWishlistCl } from 'app/shared/model/issue-in-wishlist-cl.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IIssueInWishlistClDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class IssueInWishlistClDetail extends React.Component<IIssueInWishlistClDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { issueInWishlistEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            IssueInWishlist [<b>{issueInWishlistEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="creationDate">Creation Date</span>
            </dt>
            <dd>
              <TextFormat value={issueInWishlistEntity.creationDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="lastUpdate">Last Update</span>
            </dt>
            <dd>
              <TextFormat value={issueInWishlistEntity.lastUpdate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>Wishlist</dt>
            <dd>{issueInWishlistEntity.wishlistId ? issueInWishlistEntity.wishlistId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/issue-in-wishlist-cl" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/issue-in-wishlist-cl/${issueInWishlistEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ issueInWishlist }: IRootState) => ({
  issueInWishlistEntity: issueInWishlist.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(IssueInWishlistClDetail);
