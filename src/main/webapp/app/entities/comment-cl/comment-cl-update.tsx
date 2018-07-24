import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IIssueCl } from 'app/shared/model/collector/issue-cl.model';
import { getEntities as getIssues } from 'app/entities/collector/issue-cl/issue-cl.reducer';
import { getEntity, updateEntity, createEntity, reset } from './comment-cl.reducer';
import { ICommentCl } from 'app/shared/model/comment-cl.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface ICommentClUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface ICommentClUpdateState {
  isNew: boolean;
  issueId: number;
}

export class CommentClUpdate extends React.Component<ICommentClUpdateProps, ICommentClUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      issueId: 0,
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getIssues();
  }

  saveEntity = (event, errors, values) => {
    values.date = new Date(values.date);

    if (errors.length === 0) {
      const { commentEntity } = this.props;
      const entity = {
        ...commentEntity,
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
    this.props.history.push('/entity/comment-cl');
  };

  issueUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        issueId: -1
      });
    } else {
      for (const i in this.props.issues) {
        if (id === this.props.issues[i].id.toString()) {
          this.setState({
            issueId: this.props.issues[i].id
          });
        }
      }
    }
  };

  render() {
    const { commentEntity, issues, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="collectorApp.comment.home.createOrEditLabel">Create or edit a Comment</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : commentEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="comment-cl-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="dateLabel" for="date">
                    Date
                  </Label>
                  <AvInput
                    id="comment-cl-date"
                    type="datetime-local"
                    className="form-control"
                    name="date"
                    value={isNew ? null : convertDateTimeFromServer(this.props.commentEntity.date)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="commentLabel" for="comment">
                    Comment
                  </Label>
                  <AvField id="comment-cl-comment" type="text" name="comment" />
                </AvGroup>
                <AvGroup>
                  <Label for="issue.id">Issue</Label>
                  <AvInput id="comment-cl-issue" type="select" className="form-control" name="issueId" onChange={this.issueUpdate}>
                    <option value="" key="0" />
                    {issues
                      ? issues.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/comment-cl" replace color="info">
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
  issues: storeState.issue.entities,
  commentEntity: storeState.comment.entity,
  loading: storeState.comment.loading,
  updating: storeState.comment.updating
});

const mapDispatchToProps = {
  getIssues,
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
)(CommentClUpdate);
