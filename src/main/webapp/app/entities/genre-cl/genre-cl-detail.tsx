import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './genre-cl.reducer';
import { IGenreCl } from 'app/shared/model/genre-cl.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGenreClDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class GenreClDetail extends React.Component<IGenreClDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { genreEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Genre [<b>{genreEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="desc">Desc</span>
            </dt>
            <dd>{genreEntity.desc}</dd>
          </dl>
          <Button tag={Link} to="/entity/genre-cl" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/genre-cl/${genreEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ genre }: IRootState) => ({
  genreEntity: genre.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GenreClDetail);
