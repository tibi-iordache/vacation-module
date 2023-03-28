import project from 'app/entities/project/project.reducer';
import team from 'app/entities/team/team.reducer';
import nationalDay from 'app/entities/national-day/national-day.reducer';
import vacationRequest from 'app/entities/vacation-request/vacation-request.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  project,
  team,
  nationalDay,
  vacationRequest,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
