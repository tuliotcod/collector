import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import {
  openFile,
  byteSize,
  ICrudSearchAction,
  ICrudGetAllAction,
  TextFormat,
  getSortState,
  IPaginationBaseState,
  getPaginationItemsNumber,
  JhiPagination
} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './issue-cl.reducer';
import { IIssueCl } from 'app/shared/model/issue-cl.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IIssueClProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IIssueClState extends IPaginationBaseState {
  search: string;
}

export class IssueCl extends React.Component<IIssueClProps, IIssueClState> {
  state: IIssueClState = {
    search: '',
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.getEntities();
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

  sort = prop => () => {
    this.setState(
      {
        activePage: 0,
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => this.sortEntities()
    );
  };

  sortEntities() {
    this.getEntities();
    this.props.history.push(`${this.props.location.pathname}?page=${this.state.activePage}&sort=${this.state.sort},${this.state.order}`);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.sortEntities());

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { issueList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="issue-cl-heading">
          Issues
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp; Create new Issue
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
                <th className="hand" onClick={this.sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('number')}>
                  Number <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('info')}>
                  Info <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('cover')}>
                  Cover <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('month')}>
                  Month <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('year')}>
                  Year <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('day')}>
                  Day <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('pages')}>
                  Pages <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('sameFormatAllIssues')}>
                  Same Format All Issues <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('coverPrice')}>
                  Cover Price <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('creationDate')}>
                  Creation Date <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('lastUpdate')}>
                  Last Update <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Title <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Cover Collector User <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Format <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Finishing <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Currency <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Country <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {issueList.map((issue, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${issue.id}`} color="link" size="sm">
                      {issue.id}
                    </Button>
                  </td>
                  <td>{issue.number}</td>
                  <td>{issue.info}</td>
                  <td>
                    {issue.cover ? (
                      <div>
                        <a onClick={openFile(issue.coverContentType, issue.cover)}>
                          <img src={`data:${issue.coverContentType};base64,${issue.cover}`} style={{ maxHeight: '30px' }} />
                          &nbsp;
                        </a>
                        <span>
                          {issue.coverContentType}, {byteSize(issue.cover)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{issue.month}</td>
                  <td>{issue.year}</td>
                  <td>{issue.day}</td>
                  <td>{issue.pages}</td>
                  <td>{issue.sameFormatAllIssues ? 'true' : 'false'}</td>
                  <td>{issue.coverPrice}</td>
                  <td>
                    <TextFormat type="date" value={issue.creationDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={issue.lastUpdate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{issue.titleId ? <Link to={`title/${issue.titleId}`}>{issue.titleId}</Link> : ''}</td>
                  <td>
                    {issue.coverCollectorUserId ? (
                      <Link to={`collectorUser/${issue.coverCollectorUserId}`}>{issue.coverCollectorUserId}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{issue.formatId ? <Link to={`format/${issue.formatId}`}>{issue.formatId}</Link> : ''}</td>
                  <td>{issue.finishingId ? <Link to={`finishing/${issue.finishingId}`}>{issue.finishingId}</Link> : ''}</td>
                  <td>{issue.currencyId ? <Link to={`currency/${issue.currencyId}`}>{issue.currencyId}</Link> : ''}</td>
                  <td>{issue.countryId ? <Link to={`country/${issue.countryId}`}>{issue.countryId}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${issue.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${issue.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${issue.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
        <Row className="justify-content-center">
          <JhiPagination
            items={getPaginationItemsNumber(totalItems, this.state.itemsPerPage)}
            activePage={this.state.activePage}
            onSelect={this.handlePagination}
            maxButtons={5}
          />
        </Row>
      </div>
    );
  }
}

const mapStateToProps = ({ issue }: IRootState) => ({
  issueList: issue.entities,
  totalItems: issue.totalItems
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
)(IssueCl);
