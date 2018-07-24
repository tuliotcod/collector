import React from 'react';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from '../header-components';

export const EntitiesMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="th-list" name="Entities" id="entity-menu">
    <DropdownItem tag={Link} to="/entity/collector-user-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Collector User Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/address-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Address Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/country-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Country Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/state-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;State Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/publisher-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Publisher Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/licensor-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Licensor Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/category-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Category Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/genre-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Genre Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/status-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Status Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/title-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Title Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/format-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Format Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/finishing-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Finishing Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/currency-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Currency Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/action-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Action Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/contribution-type-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Contribution Type Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/contribution-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Contribution Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/issue-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Issue Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/comment-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Comment Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/arc-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Arc Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/team-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Team Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/personage-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Personage Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/history-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;History Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/artist-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Artist Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/role-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Role Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/collaborator-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Collaborator Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/issue-status-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Issue Status Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/reading-status-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Reading Status Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/issue-in-collection-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Issue In Collection Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/issue-in-wishlist-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Issue In Wishlist Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/wishlist-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Wishlist Cl
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/collection-cl">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Collection Cl
    </DropdownItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
