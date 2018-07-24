import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudSearchAction, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './collaborator-cl.reducer';
import { ICollaboratorCl } from 'app/shared/model/collaborator-cl.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICollaboratorClProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface ICollaboratorClState {
  search: string;
}

export class CollaboratorCl extends React.Component<ICollaboratorClProps, ICollaboratorClState> {
  state: ICollaboratorClState = {
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
    const { collaboratorList, match } = this.props;
    return (
      <div>
        <h2 id="collaborator-cl-heading">
          Collaborators
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp; Create new Collaborator
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
                <th>Creation Date</th>
                <th>Last Update</th>
                <th>Issue</th>
                <th>History</th>
                <th>Artist</th>
                <th>Function</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {collaboratorList.map((collaborator, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${collaborator.id}`} color="link" size="sm">
                      {collaborator.id}
                    </Button>
                  </td>
                  <td>
                    <TextFormat type="date" value={collaborator.creationDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={collaborator.lastUpdate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{collaborator.issueId ? <Link to={`issue/${collaborator.issueId}`}>{collaborator.issueId}</Link> : ''}</td>
                  <td>{collaborator.historyId ? <Link to={`history/${collaborator.historyId}`}>{collaborator.historyId}</Link> : ''}</td>
                  <td>{collaborator.artistId ? <Link to={`artist/${collaborator.artistId}`}>{collaborator.artistId}</Link> : ''}</td>
                  <td>{collaborator.functionId ? <Link to={`role/${collaborator.functionId}`}>{collaborator.functionId}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${collaborator.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${collaborator.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${collaborator.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ collaborator }: IRootState) => ({
  collaboratorList: collaborator.entities
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
)(CollaboratorCl);