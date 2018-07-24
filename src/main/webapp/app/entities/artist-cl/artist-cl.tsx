import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { openFile, byteSize, ICrudSearchAction, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './artist-cl.reducer';
import { IArtistCl } from 'app/shared/model/artist-cl.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IArtistClProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IArtistClState {
  search: string;
}

export class ArtistCl extends React.Component<IArtistClProps, IArtistClState> {
  state: IArtistClState = {
    search: ''
  };

  componentDidMount() {
    this.props.getEntities();
  }

  search = () => {
    if (this.state.search) {
      this.props.getSearchEntities(this.state.search);
    }
  };

  clear = () => {
    this.props.getEntities();
    this.setState({
      search: ''
    });
  };

  handleSearch = event => this.setState({ search: event.target.value });

  render() {
    const { artistList, match } = this.props;
    return (
      <div>
        <h2 id="artist-cl-heading">
          Artists
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp; Create new Artist
          </Link>
        </h2>
        <Row>
          <Col sm="12">
            <AvForm onSubmit={this.search}>
              <AvGroup>
                <InputGroup>
                  <AvInput type="text" name="search" value={this.state.search} onChange={this.handleSearch} placeholder="Search" />
                  <Button className="input-group-addon">
                    <FontAwesomeIcon icon="search" />
                  </Button>
                  <Button type="reset" className="input-group-addon" onClick={this.clear}>
                    <FontAwesomeIcon icon="trash" />
                  </Button>
                </InputGroup>
              </AvGroup>
            </AvForm>
          </Col>
        </Row>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Last Name</th>
                <th>Nickname</th>
                <th>Birthday</th>
                <th>Date Of Death</th>
                <th>Bio</th>
                <th>Image</th>
                <th>Creation Date</th>
                <th>Last Update</th>
                <th>Country</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {artistList.map((artist, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${artist.id}`} color="link" size="sm">
                      {artist.id}
                    </Button>
                  </td>
                  <td>{artist.name}</td>
                  <td>{artist.lastName}</td>
                  <td>{artist.nickname}</td>
                  <td>
                    <TextFormat type="date" value={artist.birthday} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={artist.dateOfDeath} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{artist.bio}</td>
                  <td>
                    {artist.image ? (
                      <div>
                        <a onClick={openFile(artist.imageContentType, artist.image)}>
                          <img src={`data:${artist.imageContentType};base64,${artist.image}`} style={{ maxHeight: '30px' }} />
                          &nbsp;
                        </a>
                        <span>
                          {artist.imageContentType}, {byteSize(artist.image)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>
                    <TextFormat type="date" value={artist.creationDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={artist.lastUpdate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{artist.countryId ? <Link to={`country/${artist.countryId}`}>{artist.countryId}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${artist.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${artist.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${artist.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ artist }: IRootState) => ({
  artistList: artist.entities
});

const mapDispatchToProps = {
  getSearchEntities,
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ArtistCl);
