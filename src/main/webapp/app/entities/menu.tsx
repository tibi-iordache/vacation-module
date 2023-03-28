import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/project">
        Project
      </MenuItem>
      <MenuItem icon="asterisk" to="/team">
        Team
      </MenuItem>
      <MenuItem icon="asterisk" to="/national-day">
        National Day
      </MenuItem>
      <MenuItem icon="asterisk" to="/vacation-request">
        Vacation Request
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
