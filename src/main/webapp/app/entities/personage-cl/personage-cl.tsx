import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudSearchAction, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './personage-cl.reducer';
import { IPersonageCl } from 'app/shared/model/personage-cl.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPersonageClProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IPersonageClState {
  search: string;
}

export class PersonageCl extends React.Component<IPersonageClProps, IPersonageClState> {
  state: IPersonageClState = {
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
    const { personageList, match } = this.props;
    return (
      <div>
        <h2 id="personage-cl-heading">
          Personages
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp; Create new Personage
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
                <th>Code Name</th>
                <th>Original Name</th>
                <th>Bio</th>
                <th>Creation Date</th>
                <th>Last Update</th>
                <th>Country</th>
                <th>Licensor</th>
                <th>History</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {personageList.map((personage, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${personage.id}`} color="link" size="sm">
                      {personage.id}
                    </Button>
                  </td>
                  <td>{personage.name}</td>
                  <td>{personage.lastName}</td>
                  <td>{personage.codeName}</td>
                  <td>{personage.originalName}</td>
                  <td>{personage.bio}</td>
                  <td>
                    <TextFormat type="date" value={personage.creationDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={personage.lastUpdate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{personage.countryId ? <Link to={`country/${personage.countryId}`}>{personage.countryId}</Link> : ''}</td>
                  <td>{personage.licensorId ? <Link to={`licensor/${personage.licensorId}`}>{personage.licensorId}</Link> : ''}</td>
                  <td>{personage.historyId ? <Link to={`history/${personage.historyId}`}>{personage.historyId}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${personage.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${personage.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${personage.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ personage }: IRootState) => ({
  personageList: personage.entities
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
)(PersonageCl);
