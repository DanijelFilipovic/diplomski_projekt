package app.diplomski.dfilipov.entities;

import app.diplomski.dfilipov.beans.session.EntityMapper;
import app.diplomski.dfilipov.mappers.LicenseMapper;
import app.diplomski.dfilipov.mappers.ProjectMapper;
import app.diplomski.dfilipov.mappers.SkillMapper;
import app.diplomski.dfilipov.mappers.TrainingMapper;
import app.diplomski.dfilipov.utils.money.Money;
import app.diplomski.dfilipov.utils.time.CustomTime;
import app.diplomski.dfilipov.utils.wrappers.AcquiredLicense;
import app.diplomski.dfilipov.utils.wrappers.ScheduledTraining;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Employee extends Entity {

	private final EntityMapper entityMapper;
	
	private String firstName;
	private String lastName;
	private Money salary;
	private CustomTime workingHourStart;
	private CustomTime workingHourEnd;
	private EmploymentStatus employmentStatus;
	private OrganizationalUnit organizationalUnit;
	private Job job;
	private List<Skill> skills;
	private List<AcquiredLicense> acquiredLicenses;
	private List<ScheduledTraining> scheduledTrainings;
	private List<Project> projects;

	public Employee() {
		this.entityMapper = lookupEntityMapperBean();
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Money getSalary() {
		return salary;
	}

	public CustomTime getWorkingHourStart() {
		return workingHourStart;
	}

	public CustomTime getWorkingHourEnd() {
		return workingHourEnd;
	}

	public EmploymentStatus getEmploymentStatus() {
		return employmentStatus;
	}

	public OrganizationalUnit getOrganizationalUnit() {
		return organizationalUnit;
	}

	public Job getJob() {
		return job;
	}

	public List<Skill> getSkills() {
		if (skills == null) {
			SkillMapper skillMapper = (SkillMapper) entityMapper.getMapper(Skill.class);
			skills = skillMapper.findForEmployee(this);
		}
		return skills;
	}

	public List<AcquiredLicense> getAcquiredLicenses() {
		if (acquiredLicenses == null) {
			LicenseMapper licenseMapper = (LicenseMapper) entityMapper.getMapper(License.class);
			licenseMapper.loadForEmployee(this);
		}
		return acquiredLicenses;
	}

	public List<ScheduledTraining> getScheduledTrainings() {
		if (scheduledTrainings == null) {
			TrainingMapper trainingMapper = (TrainingMapper) entityMapper.getMapper(Training.class);
			trainingMapper.loadForEmployee(this);
		}
		return scheduledTrainings;
	}

	public List<Project> getProjects() {
		if (projects == null) {
			ProjectMapper projectMapper = (ProjectMapper) entityMapper.getMapper(Project.class);
			projectMapper.loadForEmployee(this);
		}
		return projects;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setSalary(Money salary) {
		this.salary = salary;
	}

	public void setWorkingHourStart(CustomTime workingHourStart) {
		this.workingHourStart = workingHourStart;
	}

	public void setWorkingHourEnd(CustomTime workingHourEnd) {
		this.workingHourEnd = workingHourEnd;
	}

	public void setEmploymentStatus(EmploymentStatus employmentStatus) {
		this.employmentStatus = employmentStatus;
	}

	public void setOrganizationalUnit(OrganizationalUnit organizationalUnit) {
		this.organizationalUnit = organizationalUnit;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}

	public void setAcquiredLicenses(List<AcquiredLicense> acquiredLicenses) {
		this.acquiredLicenses = acquiredLicenses;
	}

	public void setScheduledTrainings(List<ScheduledTraining> scheduledTrainings) {
		this.scheduledTrainings = scheduledTrainings;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	private EntityMapper lookupEntityMapperBean() {
		try {
			Context c = new InitialContext();
			return (EntityMapper) c.lookup("java:global/dfilipov_diplomski_app/EntityMapper!app.diplomski.dfilipov.beans.session.EntityMapper");
		} catch (NamingException ne) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
			throw new RuntimeException(ne);
		}
	}
}