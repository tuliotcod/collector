import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './artist-cl.reducer';
import { IArtistCl } from 'app/shared/model/artist-cl.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IArtistClDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class ArtistClDetail extends React.Component<IArtistClDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { artistEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Artist [<b>{artistEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{artistEntity.name}</dd>
            <dt>
              <span id="lastName">Last Name</span>
            </dt>
            <dd>{artistEntity.lastName}</dd>
            <dt>
              <span id="nickname">Nickname</span>
            </dt>
            <dd>{artistEntity.nickname}</dd>
            <dt>
              <span id="birthday">Birthday</span>
            </dt>
            <dd>
              <TextFormat value={artistEntity.birthday} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="dateOfDeath">Date Of Death</span>
            </dt>
            <dd>
              <TextFormat value={artistEntity.dateOfDeath} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="bio">Bio</span>
            </dt>
            <dd>{artistEntity.bio}</dd>
            <dt>
              <span id="image">Image</span>
            </dt>
            <dd>
              {artistEntity.image ? (
                <div>
                  <a onClick={openFile(artistEntity.imageContentType, artistEntity.image)}>
                    <img src={`data:${artistEntity.imageContentType};base64,${artistEntity.image}`} style={{ maxHeight: '30px' }} />
                  </a>
                  <span>
                    {artistEntity.imageContentType}, {byteSize(artistEntity.image)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="creationDate">Creation Date</span>
            </dt>
            <dd>
              <TextFormat value={artistEntity.creationDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="lastUpdate">Last Update</span>
            </dt>
            <dd>
              <TextFormat value={artistEntity.lastUpdate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>Country</dt>
            <dd>{artistEntity.countryId ? artistEntity.countryId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/artist-cl" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/artist-cl/${artistEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ artist }: IRootState) => ({
  artistEntity: artist.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ArtistClDetail);
