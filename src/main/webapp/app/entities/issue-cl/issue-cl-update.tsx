import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ITitleCl } from 'app/shared/model/collector/title-cl.model';
import { getEntities as getTitles } from 'app/entities/collector/title-cl/title-cl.reducer';
import { ICollectorUserCl } from 'app/shared/model/collector/collector-user-cl.model';
import { getEntities as getCollectorUsers } from 'app/entities/collector/collector-user-cl/collector-user-cl.reducer';
import { IFormatCl } from 'app/shared/model/collector/format-cl.model';
import { getEntities as getFormats } from 'app/entities/collector/format-cl/format-cl.reducer';
import { IFinishingCl } from 'app/shared/model/collector/finishing-cl.model';
import { getEntities as getFinishings } from 'app/entities/collector/finishing-cl/finishing-cl.reducer';
import { ICurrencyCl } from 'app/shared/model/collector/currency-cl.model';
import { getEntities as getCurrencies } from 'app/entities/collector/currency-cl/currency-cl.reducer';
import { ICountryCl } from 'app/shared/model/collector/country-cl.model';
import { getEntities as getCountries } from 'app/entities/collector/country-cl/country-cl.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './issue-cl.reducer';
import { IIssueCl } from 'app/shared/model/issue-cl.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface IIssueClUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface IIssueClUpdateState {
  isNew: boolean;
  titleId: number;
  coverCollectorUserId: number;
  formatId: number;
  finishingId: number;
  currencyId: number;
  countryId: number;
}

export class IssueClUpdate extends React.Component<IIssueClUpdateProps, IIssueClUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      titleId: 0,
      coverCollectorUserId: 0,
      formatId: 0,
      finishingId: 0,
      currencyId: 0,
      countryId: 0,
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getTitles();
    this.props.getCollectorUsers();
    this.props.getFormats();
    this.props.getFinishings();
    this.props.getCurrencies();
    this.props.getCountries();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    values.creationDate = new Date(values.creationDate);
    values.lastUpdate = new Date(values.lastUpdate);

    if (errors.length === 0) {
      const { issueEntity } = this.props;
      const entity = {
        ...issueEntity,
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
    this.props.history.push('/entity/issue-cl');
  };

  titleUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        titleId: -1
      });
    } else {
      for (const i in this.props.titles) {
        if (id === this.props.titles[i].id.toString()) {
          this.setState({
            titleId: this.props.titles[i].id
          });
        }
      }
    }
  };

  coverCollectorUserUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        coverCollectorUserId: -1
      });
    } else {
      for (const i in this.props.collectorUsers) {
        if (id === this.props.collectorUsers[i].id.toString()) {
          this.setState({
            coverCollectorUserId: this.props.collectorUsers[i].id
          });
        }
      }
    }
  };

  formatUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        formatId: -1
      });
    } else {
      for (const i in this.props.formats) {
        if (id === this.props.formats[i].id.toString()) {
          this.setState({
            formatId: this.props.formats[i].id
          });
        }
      }
    }
  };

  finishingUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        finishingId: -1
      });
    } else {
      for (const i in this.props.finishings) {
        if (id === this.props.finishings[i].id.toString()) {
          this.setState({
            finishingId: this.props.finishings[i].id
          });
        }
      }
    }
  };

  currencyUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        currencyId: -1
      });
    } else {
      for (const i in this.props.currencies) {
        if (id === this.props.currencies[i].id.toString()) {
          this.setState({
            currencyId: this.props.currencies[i].id
          });
        }
      }
    }
  };

  countryUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        countryId: -1
      });
    } else {
      for (const i in this.props.countries) {
        if (id === this.props.countries[i].id.toString()) {
          this.setState({
            countryId: this.props.countries[i].id
          });
        }
      }
    }
  };

  render() {
    const { issueEntity, titles, collectorUsers, formats, finishings, currencies, countries, loading, updating } = this.props;
    const { isNew } = this.state;

    const { cover, coverContentType } = issueEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="collectorApp.issue.home.createOrEditLabel">Create or edit a Issue</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : issueEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="issue-cl-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="numberLabel" for="number">
                    Number
                  </Label>
                  <AvField id="issue-cl-number" type="number" className="form-control" name="number" />
                </AvGroup>
                <AvGroup>
                  <Label id="infoLabel" for="info">
                    Info
                  </Label>
                  <AvField id="issue-cl-info" type="text" name="info" />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="coverLabel" for="cover">
                      Cover
                    </Label>
                    <br />
                    {cover ? (
                      <div>
                        <a onClick={openFile(coverContentType, cover)}>
                          <img src={`data:${coverContentType};base64,${cover}`} style={{ maxHeight: '100px' }} />
                        </a>
                        <br />
                        <Row>
                          <Col md="11">
                            <span>
                              {coverContentType}, {byteSize(cover)}
                            </span>
                          </Col>
                          <Col md="1">
                            <Button color="danger" onClick={this.clearBlob('cover')}>
                              <FontAwesomeIcon icon="times-circle" />
                            </Button>
                          </Col>
                        </Row>
                      </div>
                    ) : null}
                    <input id="file_cover" type="file" onChange={this.onBlobChange(true, 'cover')} accept="image/*" />
                    <AvInput type="hidden" name="cover" value={cover} />
                  </AvGroup>
                </AvGroup>
                <AvGroup>
                  <Label id="monthLabel" for="month">
                    Month
                  </Label>
                  <AvField id="issue-cl-month" type="number" className="form-control" name="month" />
                </AvGroup>
                <AvGroup>
                  <Label id="yearLabel" for="year">
                    Year
                  </Label>
                  <AvField id="issue-cl-year" type="number" className="form-control" name="year" />
                </AvGroup>
                <AvGroup>
                  <Label id="dayLabel" for="day">
                    Day
                  </Label>
                  <AvField id="issue-cl-day" type="number" className="form-control" name="day" />
                </AvGroup>
                <AvGroup>
                  <Label id="pagesLabel" for="pages">
                    Pages
                  </Label>
                  <AvField id="issue-cl-pages" type="number" className="form-control" name="pages" />
                </AvGroup>
                <AvGroup>
                  <Label id="sameFormatAllIssuesLabel" check>
                    <AvInput id="issue-cl-sameFormatAllIssues" type="checkbox" className="form-control" name="sameFormatAllIssues" />
                    Same Format All Issues
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="coverPriceLabel" for="coverPrice">
                    Cover Price
                  </Label>
                  <AvField id="issue-cl-coverPrice" type="text" name="coverPrice" />
                </AvGroup>
                <AvGroup>
                  <Label id="creationDateLabel" for="creationDate">
                    Creation Date
                  </Label>
                  <AvInput
                    id="issue-cl-creationDate"
                    type="datetime-local"
                    className="form-control"
                    name="creationDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.issueEntity.creationDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="lastUpdateLabel" for="lastUpdate">
                    Last Update
                  </Label>
                  <AvInput
                    id="issue-cl-lastUpdate"
                    type="datetime-local"
                    className="form-control"
                    name="lastUpdate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.issueEntity.lastUpdate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="title.id">Title</Label>
                  <AvInput id="issue-cl-title" type="select" className="form-control" name="titleId" onChange={this.titleUpdate}>
                    <option value="" key="0" />
                    {titles
                      ? titles.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="coverCollectorUser.id">Cover Collector User</Label>
                  <AvInput
                    id="issue-cl-coverCollectorUser"
                    type="select"
                    className="form-control"
                    name="coverCollectorUserId"
                    onChange={this.coverCollectorUserUpdate}
                  >
                    <option value="" key="0" />
                    {collectorUsers
                      ? collectorUsers.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="format.id">Format</Label>
                  <AvInput id="issue-cl-format" type="select" className="form-control" name="formatId" onChange={this.formatUpdate}>
                    <option value="" key="0" />
                    {formats
                      ? formats.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="finishing.id">Finishing</Label>
                  <AvInput
                    id="issue-cl-finishing"
                    type="select"
                    className="form-control"
                    name="finishingId"
                    onChange={this.finishingUpdate}
                  >
                    <option value="" key="0" />
                    {finishings
                      ? finishings.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="currency.id">Currency</Label>
                  <AvInput id="issue-cl-currency" type="select" className="form-control" name="currencyId" onChange={this.currencyUpdate}>
                    <option value="" key="0" />
                    {currencies
                      ? currencies.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="country.id">Country</Label>
                  <AvInput id="issue-cl-country" type="select" className="form-control" name="countryId" onChange={this.countryUpdate}>
                    <option value="" key="0" />
                    {countries
                      ? countries.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/issue-cl" replace color="info">
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
  titles: storeState.title.entities,
  collectorUsers: storeState.collectorUser.entities,
  formats: storeState.format.entities,
  finishings: storeState.finishing.entities,
  currencies: storeState.currency.entities,
  countries: storeState.country.entities,
  issueEntity: storeState.issue.entity,
  loading: storeState.issue.loading,
  updating: storeState.issue.updating
});

const mapDispatchToProps = {
  getTitles,
  getCollectorUsers,
  getFormats,
  getFinishings,
  getCurrencies,
  getCountries,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(IssueClUpdate);
