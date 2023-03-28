import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './national-day.reducer';

export const NationalDayDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const nationalDayEntity = useAppSelector(state => state.nationalDay.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="nationalDayDetailsHeading">National Day</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{nationalDayEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{nationalDayEntity.name}</dd>
          <dt>
            <span id="day">Day</span>
          </dt>
          <dd>{nationalDayEntity.day}</dd>
          <dt>
            <span id="month">Month</span>
          </dt>
          <dd>{nationalDayEntity.month}</dd>
        </dl>
        <Button tag={Link} to="/national-day" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/national-day/${nationalDayEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default NationalDayDetail;
