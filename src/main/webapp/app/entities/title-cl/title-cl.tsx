import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudSearchAction, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './title-cl.reducer';
import { ITitleCl } from 'app/shared/model/title-cl.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITitleClProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface ITitleClState {
  search: string;
}

export class TitleCl extends React.Component<ITitleClProps, ITitleClState> {
  state: ITitleClState = {
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
    const { titleList, match } = this.props;
    return (
      <div>
        <h2 id="title-cl-heading">
          Titles
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp; Create new Title
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
                <th>Serie</th>
                <th>Start Date</th>
                <th>End Date</th>
                <th>Info</th>
                <th>Creation Date</th>
                <th>Last Update</th>
                <th>Category</th>
                <th>Genre</th>
                <th>Status</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {titleList.map((title, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${title.id}`} color="link" size="sm">
                      {title.id}
                    </Button>
                  </td>
                  <td>{title.name}</td>
                  <td>{title.serie}</td>
                  <td>
                    <TextFormat type="date" value={title.startDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={title.endDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{title.info}</td>
                  <td>
                    <TextFormat type="date" value={title.creationDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={title.lastUpdate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{title.categoryId ? <Link to={`category/${title.categoryId}`}>{title.categoryId}</Link> : ''}</td>
                  <td>{title.genreId ? <Link to={`genre/${title.genreId}`}>{title.genreId}</Link> : ''}</td>
                  <td>{title.statusId ? <Link to={`status/${title.statusId}`}>{title.statusId}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${title.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${title.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${title.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ title }: IRootState) => ({
  titleList: title.entities
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
)(TitleCl);
