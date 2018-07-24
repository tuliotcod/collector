import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudSearchAction, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './history-cl.reducer';
import { IHistoryCl } from 'app/shared/model/history-cl.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IHistoryClProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IHistoryClState {
  search: string;
}

export class HistoryCl extends React.Component<IHistoryClProps, IHistoryClState> {
  state: IHistoryClState = {
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
    const { historyList, match } = this.props;
    return (
      <div>
        <h2 id="history-cl-heading">
          Histories
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp; Create new History
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
                <th>Order</th>
                <th>Name</th>
                <th>Pages</th>
                <th>Desc</th>
                <th>Part</th>
                <th>Creation Date</th>
                <th>Last Update</th>
                <th>Issue</th>
                <th>Arc</th>
                <th>Original Issue</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {historyList.map((history, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${history.id}`} color="link" size="sm">
                      {history.id}
                    </Button>
                  </td>
                  <td>{history.order}</td>
                  <td>{history.name}</td>
                  <td>{history.pages}</td>
                  <td>{history.desc}</td>
                  <td>{history.part}</td>
                  <td>
                    <TextFormat type="date" value={history.creationDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={history.lastUpdate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{history.issueId ? <Link to={`issue/${history.issueId}`}>{history.issueId}</Link> : ''}</td>
                  <td>{history.arcId ? <Link to={`arc/${history.arcId}`}>{history.arcId}</Link> : ''}</td>
                  <td>{history.originalIssueId ? <Link to={`issue/${history.originalIssueId}`}>{history.originalIssueId}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${history.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${history.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${history.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ history }: IRootState) => ({
  historyList: history.entities
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
)(HistoryCl);
