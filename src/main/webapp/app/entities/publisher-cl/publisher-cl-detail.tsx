import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './publisher-cl.reducer';
import { IPublisherCl } from 'app/shared/model/publisher-cl.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPublisherClDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class PublisherClDetail extends React.Component<IPublisherClDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { publisherEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Publisher [<b>{publisherEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{publisherEntity.name}</dd>
            <dt>
              <span id="website">Website</span>
            </dt>
            <dd>{publisherEntity.website}</dd>
            <dt>
              <span id="info">Info</span>
            </dt>
            <dd>{publisherEntity.info}</dd>
            <dt>
              <span id="image">Image</span>
            </dt>
            <dd>
              {publisherEntity.image ? (
                <div>
                  <a onClick={openFile(publisherEntity.imageContentType, publisherEntity.image)}>
                    <img src={`data:${publisherEntity.imageContentType};base64,${publisherEntity.image}`} style={{ maxHeight: '30px' }} />
                  </a>
                  <span>
                    {publisherEntity.imageContentType}, {byteSize(publisherEntity.image)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="creationDate">Creation Date</span>
            </dt>
            <dd>
              <TextFormat value={publisherEntity.creationDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="lastUpdate">Last Update</span>
            </dt>
            <dd>
              <TextFormat value={publisherEntity.lastUpdate} type="date" format={APP_DATE_FORMAT} />
            </dd>
          </dl>
          <Button tag={Link} to="/entity/publisher-cl" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/publisher-cl/${publisherEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ publisher }: IRootState) => ({
  publisherEntity: publisher.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PublisherClDetail);
