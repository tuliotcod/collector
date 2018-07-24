import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICategoryCl } from 'app/shared/model/collector/category-cl.model';
import { getEntities as getCategories } from 'app/entities/collector/category-cl/category-cl.reducer';
import { IGenreCl } from 'app/shared/model/collector/genre-cl.model';
import { getEntities as getGenres } from 'app/entities/collector/genre-cl/genre-cl.reducer';
import { IStatusCl } from 'app/shared/model/collector/status-cl.model';
import { getEntities as getStatuses } from 'app/entities/collector/status-cl/status-cl.reducer';
import { getEntity, updateEntity, createEntity, reset } from './title-cl.reducer';
import { ITitleCl } from 'app/shared/model/title-cl.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface ITitleClUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface ITitleClUpdateState {
  isNew: boolean;
  categoryId: number;
  genreId: number;
  statusId: number;
}

export class TitleClUpdate extends React.Component<ITitleClUpdateProps, ITitleClUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      categoryId: 0,
      genreId: 0,
      statusId: 0,
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getCategories();
    this.props.getGenres();
    this.props.getStatuses();
  }

  saveEntity = (event, errors, values) => {
    values.startDate = new Date(values.startDate);
    values.endDate = new Date(values.endDate);
    values.creationDate = new Date(values.creationDate);
    values.lastUpdate = new Date(values.lastUpdate);

    if (errors.length === 0) {
      const { titleEntity } = this.props;
      const entity = {
        ...titleEntity,
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
    this.props.history.push('/entity/title-cl');
  };

  categoryUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        categoryId: -1
      });
    } else {
      for (const i in this.props.categories) {
        if (id === this.props.categories[i].id.toString()) {
          this.setState({
            categoryId: this.props.categories[i].id
          });
        }
      }
    }
  };

  genreUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        genreId: -1
      });
    } else {
      for (const i in this.props.genres) {
        if (id === this.props.genres[i].id.toString()) {
          this.setState({
            genreId: this.props.genres[i].id
          });
        }
      }
    }
  };

  statusUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        statusId: -1
      });
    } else {
      for (const i in this.props.statuses) {
        if (id === this.props.statuses[i].id.toString()) {
          this.setState({
            statusId: this.props.statuses[i].id
          });
        }
      }
    }
  };

  render() {
    const { titleEntity, categories, genres, statuses, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="collectorApp.title.home.createOrEditLabel">Create or edit a Title</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : titleEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="title-cl-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    Name
                  </Label>
                  <AvField id="title-cl-name" type="text" name="name" />
                </AvGroup>
                <AvGroup>
                  <Label id="serieLabel" for="serie">
                    Serie
                  </Label>
                  <AvField id="title-cl-serie" type="text" name="serie" />
                </AvGroup>
                <AvGroup>
                  <Label id="startDateLabel" for="startDate">
                    Start Date
                  </Label>
                  <AvInput
                    id="title-cl-startDate"
                    type="datetime-local"
                    className="form-control"
                    name="startDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.titleEntity.startDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="endDateLabel" for="endDate">
                    End Date
                  </Label>
                  <AvInput
                    id="title-cl-endDate"
                    type="datetime-local"
                    className="form-control"
                    name="endDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.titleEntity.endDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="infoLabel" for="info">
                    Info
                  </Label>
                  <AvField
                    id="title-cl-info"
                    type="text"
                    name="info"
                    validate={{
                      maxLength: { value: 500, errorMessage: 'This field cannot be longer than {{ max }} characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="creationDateLabel" for="creationDate">
                    Creation Date
                  </Label>
                  <AvInput
                    id="title-cl-creationDate"
                    type="datetime-local"
                    className="form-control"
                    name="creationDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.titleEntity.creationDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="lastUpdateLabel" for="lastUpdate">
                    Last Update
                  </Label>
                  <AvInput
                    id="title-cl-lastUpdate"
                    type="datetime-local"
                    className="form-control"
                    name="lastUpdate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.titleEntity.lastUpdate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="category.id">Category</Label>
                  <AvInput id="title-cl-category" type="select" className="form-control" name="categoryId" onChange={this.categoryUpdate}>
                    <option value="" key="0" />
                    {categories
                      ? categories.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="genre.id">Genre</Label>
                  <AvInput id="title-cl-genre" type="select" className="form-control" name="genreId" onChange={this.genreUpdate}>
                    <option value="" key="0" />
                    {genres
                      ? genres.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="status.id">Status</Label>
                  <AvInput id="title-cl-status" type="select" className="form-control" name="statusId" onChange={this.statusUpdate}>
                    <option value="" key="0" />
                    {statuses
                      ? statuses.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/title-cl" replace color="info">
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
  categories: storeState.category.entities,
  genres: storeState.genre.entities,
  statuses: storeState.status.entities,
  titleEntity: storeState.title.entity,
  loading: storeState.title.loading,
  updating: storeState.title.updating
});

const mapDispatchToProps = {
  getCategories,
  getGenres,
  getStatuses,
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
)(TitleClUpdate);
