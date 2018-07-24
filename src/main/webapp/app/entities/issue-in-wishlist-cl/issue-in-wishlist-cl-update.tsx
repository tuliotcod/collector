import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IWishlistCl } from 'app/shared/model/collector/wishlist-cl.model';
import { getEntities as getWishlists } from 'app/entities/collector/wishlist-cl/wishlist-cl.reducer';
import { getEntity, updateEntity, createEntity, reset } from './issue-in-wishlist-cl.reducer';
import { IIssueInWishlistCl } from 'app/shared/model/issue-in-wishlist-cl.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface IIssueInWishlistClUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface IIssueInWishlistClUpdateState {
  isNew: boolean;
  wishlistId: number;
}

export class IssueInWishlistClUpdate extends React.Component<IIssueInWishlistClUpdateProps, IIssueInWishlistClUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      wishlistId: 0,
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getWishlists();
  }

  saveEntity = (event, errors, values) => {
    values.creationDate = new Date(values.creationDate);
    values.lastUpdate = new Date(values.lastUpdate);

    if (errors.length === 0) {
      const { issueInWishlistEntity } = this.props;
      const entity = {
        ...issueInWishlistEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
      this.handleClose();
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/issue-in-wishlist-cl');
  };

  wishlistUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        wishlistId: -1
      });
    } else {
      for (const i in this.props.wishlists) {
        if (id === this.props.wishlists[i].id.toString()) {
          this.setState({
            wishlistId: this.props.wishlists[i].id
          });
        }
      }
    }
  };

  render() {
    const { issueInWishlistEntity, wishlists, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="collectorApp.issueInWishlist.home.createOrEditLabel">Create or edit a IssueInWishlist</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : issueInWishlistEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="issue-in-wishlist-cl-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="creationDateLabel" for="creationDate">
                    Creation Date
                  </Label>
                  <AvInput
                    id="issue-in-wishlist-cl-creationDate"
                    type="datetime-local"
                    className="form-control"
                    name="creationDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.issueInWishlistEntity.creationDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="lastUpdateLabel" for="lastUpdate">
                    Last Update
                  </Label>
                  <AvInput
                    id="issue-in-wishlist-cl-lastUpdate"
                    type="datetime-local"
                    className="form-control"
                    name="lastUpdate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.issueInWishlistEntity.lastUpdate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="wishlist.id">Wishlist</Label>
                  <AvInput
                    id="issue-in-wishlist-cl-wishlist"
                    type="select"
                    className="form-control"
                    name="wishlistId"
                    onChange={this.wishlistUpdate}
                  >
                    <option value="" key="0" />
                    {wishlists
                      ? wishlists.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/issue-in-wishlist-cl" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />&nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />&nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  wishlists: storeState.wishlist.entities,
  issueInWishlistEntity: storeState.issueInWishlist.entity,
  loading: storeState.issueInWishlist.loading,
  updating: storeState.issueInWishlist.updating
});

const mapDispatchToProps = {
  getWishlists,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(IssueInWishlistClUpdate);
