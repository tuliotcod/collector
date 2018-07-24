import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudSearchAction, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './issue-in-collection-cl.reducer';
import { IIssueInCollectionCl } from 'app/shared/model/issue-in-collection-cl.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IIssueInCollectionClProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IIssueInCollectionClState {
  search: string;
}

export class IssueInCollectionCl extends React.Component<IIssueInCollectionClProps, IIssueInCollectionClState> {
  state: IIssueInCollectionClState = {
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
    const { issueInCollectionList, match } = this.props;
    return (
      <div>
        <h2 id="issue-in-collection-cl-heading">
          Issue In Collections
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp; Create new Issue In Collection
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
                <th>Price</th>
                <th>Amount</th>
                <th>Notes</th>
                <th>Creation Date</th>
                <th>Last Update</th>
                <th>Issue</th>
                <th>Issue Status</th>
                <th>Reading Status</th>
                <th>Collection</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {issueInCollectionList.map((issueInCollection, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${issueInCollection.id}`} color="link" size="sm">
                      {issueInCollection.id}
                    </Button>
                  </td>
                  <td>{issueInCollection.price}</td>
                  <td>{issueInCollection.amount}</td>
                  <td>{issueInCollection.notes}</td>
                  <td>
                    <TextFormat type="date" value={issueInCollection.creationDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={issueInCollection.lastUpdate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    {issueInCollection.issueId ? <Link to={`issue/${issueInCollection.issueId}`}>{issueInCollection.issueId}</Link> : ''}
                  </td>
                  <td>
                    {issueInCollection.issueStatusId ? (
                      <Link to={`issueStatus/${issueInCollection.issueStatusId}`}>{issueInCollection.issueStatusId}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {issueInCollection.readingStatusId ? (
                      <Link to={`readingStatus/${issueInCollection.readingStatusId}`}>{issueInCollection.readingStatusId}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {issueInCollection.collectionId ? (
                      <Link to={`collection/${issueInCollection.collectionId}`}>{issueInCollection.collectionId}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${issueInCollection.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${issueInCollection.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${issueInCollection.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ issueInCollection }: IRootState) => ({
  issueInCollectionList: issueInCollection.entities
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
)(IssueInCollectionCl);
