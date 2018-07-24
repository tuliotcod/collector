import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './licensor-cl.reducer';
import { ILicensorCl } from 'app/shared/model/licensor-cl.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ILicensorClDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class LicensorClDetail extends React.Component<ILicensorClDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { licensorEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Licensor [<b>{licensorEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{licensorEntity.name}</dd>
            <dt>
              <span id="website">Website</span>
            </dt>
            <dd>{licensorEntity.website}</dd>
            <dt>
              <span id="info">Info</span>
            </dt>
            <dd>{licensorEntity.info}</dd>
            <dt>
              <span id="image">Image</span>
            </dt>
            <dd>
              {licensorEntity.image ? (
                <div>
                  <a onClick={openFile(licensorEntity.imageContentType, licensorEntity.image)}>
                    <img src={`data:${licensorEntity.imageContentType};base64,${licensorEntity.image}`} style={{ maxHeight: '30px' }} />
                  </a>
                  <span>
                    {licensorEntity.imageContentType}, {byteSize(licensorEntity.image)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="creationDate">Creation Date</span>
            </dt>
            <dd>
              <TextFormat value={licensorEntity.creationDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="lastUpdate">Last Update</span>
            </dt>
            <dd>
              <TextFormat value={licensorEntity.lastUpdate} type="date" format={APP_DATE_FORMAT} />
            </dd>
          </dl>
          <Button tag={Link} to="/entity/licensor-cl" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/licensor-cl/${licensorEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ licensor }: IRootState) => ({
  licensorEntity: licensor.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(LicensorClDetail);
