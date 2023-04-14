import React from 'react';

import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="content/images/logo-jhipster.png" alt="Logo" />
  </div>
);

export const Brand = () => (
  <NavbarBrand tag={Link} to="/">
    <span className="brand-title">Vacation Module</span>
  </NavbarBrand>
);

export const Home = () => (
  <NavItem>
    <NavLink tag={Link} to="/" className="d-flex align-items-center">
      <FontAwesomeIcon icon="home" />
      <span>Home</span>
    </NavLink>
  </NavItem>
);

export const NationalDay = () => (
  <NavItem>
    <NavLink tag={Link} to="/national-day" className="d-flex align-items-center">
      <FontAwesomeIcon icon="th-list" />
      <span> National Days </span>
    </NavLink>
  </NavItem>
);

export const VacationRequest = () => (
  <NavItem>
    <NavLink tag={Link} to="/vacation-request" className="d-flex align-items-center">
      <FontAwesomeIcon icon="th-list" />
      <span> Vacation Requests </span>
    </NavLink>
  </NavItem>
);
