import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vacation-request.reducer';

export const VacationRequestDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vacationRequestEntity = useAppSelector(state => state.vacationRequest.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vacationRequestDetailsHeading">Vacation Request</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{vacationRequestEntity.id}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{vacationRequestEntity.description}</dd>
          <dt>
            <span id="date">Date</span>
          </dt>
          <dd>
            {vacationRequestEntity.date ? <TextFormat value={vacationRequestEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>User</dt>
          <dd>{vacationRequestEntity.user ? vacationRequestEntity.user.login : ''}</dd>
          <dt>Approbed By</dt>
          <dd>
            {vacationRequestEntity.approbedBies
              ? vacationRequestEntity.approbedBies.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.login}</a>
                    {vacationRequestEntity.approbedBies && i === vacationRequestEntity.approbedBies.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/vacation-request" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vacation-request/${vacationRequestEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default VacationRequestDetail;
