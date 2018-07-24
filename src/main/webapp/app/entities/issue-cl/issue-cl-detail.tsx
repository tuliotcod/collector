import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './issue-cl.reducer';
import { IIssueCl } from 'app/shared/model/issue-cl.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IIssueClDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class IssueClDetail extends React.Component<IIssueClDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { issueEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Issue [<b>{issueEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="number">Number</span>
            </dt>
            <dd>{issueEntity.number}</dd>
            <dt>
              <span id="info">Info</span>
            </dt>
            <dd>{issueEntity.info}</dd>
            <dt>
              <span id="cover">Cover</span>
            </dt>
            <dd>
              {issueEntity.cover ? (
                <div>
                  <a onClick={openFile(issueEntity.coverContentType, issueEntity.cover)}>
                    <img src={`data:${issueEntity.coverContentType};base64,${issueEntity.cover}`} style={{ maxHeight: '30px' }} />
                  </a>
                  <span>
                    {issueEntity.coverContentType}, {byteSize(issueEntity.cover)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="month">Month</span>
            </dt>
            <dd>{issueEntity.month}</dd>
            <dt>
              <span id="year">Year</span>
            </dt>
            <dd>{issueEntity.year}</dd>
            <dt>
              <span id="day">Day</span>
            </dt>
            <dd>{issueEntity.day}</dd>
            <dt>
              <span id="pages">Pages</span>
            </dt>
            <dd>{issueEntity.pages}</dd>
            <dt>
              <span id="sameFormatAllIssues">Same Format All Issues</span>
            </dt>
            <dd>{issueEntity.sameFormatAllIssues ? 'true' : 'false'}</dd>
            <dt>
              <span id="coverPrice">Cover Price</span>
            </dt>
            <dd>{issueEntity.coverPrice}</dd>
            <dt>
              <span id="creationDate">Creation Date</span>
            </dt>
            <dd>
              <TextFormat value={issueEntity.creationDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="lastUpdate">Last Update</span>
            </dt>
            <dd>
              <TextFormat value={issueEntity.lastUpdate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>Title</dt>
            <dd>{issueEntity.titleId ? issueEntity.titleId : ''}</dd>
            <dt>Cover Collector User</dt>
            <dd>{issueEntity.coverCollectorUserId ? issueEntity.coverCollectorUserId : ''}</dd>
            <dt>Format</dt>
            <dd>{issueEntity.formatId ? issueEntity.formatId : ''}</dd>
            <dt>Finishing</dt>
            <dd>{issueEntity.finishingId ? issueEntity.finishingId : ''}</dd>
            <dt>Currency</dt>
            <dd>{issueEntity.currencyId ? issueEntity.currencyId : ''}</dd>
            <dt>Country</dt>
            <dd>{issueEntity.countryId ? issueEntity.countryId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/issue-cl" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/issue-cl/${issueEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ issue }: IRootState) => ({
  issueEntity: issue.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(IssueClDetail);
