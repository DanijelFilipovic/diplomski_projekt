package app.diplomski.dfilipov.beans.managed;

import app.diplomski.dfilipov.beans.session.EntityMapper;
import app.diplomski.dfilipov.entities.Employee;
import app.diplomski.dfilipov.entities.EmploymentStatus;
import app.diplomski.dfilipov.entities.Job;
import app.diplomski.dfilipov.entities.License;
import app.diplomski.dfilipov.entities.OrganizationalUnit;
import app.diplomski.dfilipov.entities.Project;
import app.diplomski.dfilipov.entities.Skill;
import app.diplomski.dfilipov.entities.Training;
import app.diplomski.dfilipov.utils.money.Money;
import app.diplomski.dfilipov.utils.time.CustomDate;
import app.diplomski.dfilipov.utils.time.CustomTime;
import app.diplomski.dfilipov.utils.wrappers.AcquiredLicense;
import app.diplomski.dfilipov.utils.wrappers.EditableEntity;
import app.diplomski.dfilipov.utils.wrappers.ScheduledTraining;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "profileBean")
@ViewScoped
public class EmployeeProfileBean implements Serializable {

	@EJB private EntityMapper entityMapper;
	private String id;
	private EditableEntity<Employee> editableEmployee;
	
	public EmployeeProfileBean() {
	}

	public EditableEntity<Employee> getEditableEmployee() {
		return editableEmployee;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void updateBasic() {
		Employee employee = editableEmployee.getEntity();
		employee.setFirstName(getFirstName());
		employee.setLastName(getLastName());
		
		if (getSalaryAmount() != null && !getSalaryCurrency().equals("0")) {
			BigDecimal _salaryAmount = BigDecimal.valueOf(Double.parseDouble(getSalaryAmount()));
			Money salary = new Money(_salaryAmount, getSalaryCurrency());
			employee.setSalary(salary);
		} else {
			employee.setSalary(null);
		}
		
		if (!getEmploymentStatusID().equals("0")) {
			long _employmentStatusID = Long.parseLong(getEmploymentStatusID());
			employee.setEmploymentStatus(entityMapper.find(EmploymentStatus.class, _employmentStatusID));
		} else {
			employee.setEmploymentStatus(null);
		}
		
		if (getWorkingHourStart() != null) {
			CustomTime _workingHourStart = new CustomTime(getWorkingHourStart());
			employee.setWorkingHourStart(_workingHourStart);
		} else {
			employee.setWorkingHourStart(null);
		}
		
		if (getWorkingHourEnd() != null) {
			CustomTime _workingHourEnd = new CustomTime(getWorkingHourEnd());
			employee.setWorkingHourEnd(_workingHourEnd);
		} else {
			employee.setWorkingHourEnd(null);
		}
		
		if (!getOrganizationalUnitID().equals("0")) {
			long _organizationalUnitID = Long.parseLong(getOrganizationalUnitID());
			employee.setOrganizationalUnit(entityMapper.find(OrganizationalUnit.class, _organizationalUnitID));
		} else {
			employee.setOrganizationalUnit(null);
		}
		
		if (!getJobID().equals("0")) {
			long _jobID = Long.parseLong(getJobID());
			employee.setJob(entityMapper.find(Job.class, _jobID));
		} else {
			employee.setJob(null);
		}
		
		entityMapper.update(Employee.class, employee);
		cancelEditing();
		editableEmployee = null;
	}
	
	public void addSkill() {
		if (!skillID.equals("0")) {
			Employee employee = editableEmployee.getEntity();
			Skill skill = entityMapper.find(Skill.class, Long.parseLong(skillID));
			employee.getSkills().add(skill);
			entityMapper.update(Employee.class, employee);
			setSkillID(null);
		}
	}
	
	public void addLicense() {
		if (!getLicenseID().equals("0")) {
			Employee employee = editableEmployee.getEntity();
			CustomDate _acquisitonDate = getAcquisitionDate() != null && !getAcquisitionDate().trim().isEmpty()
				? new CustomDate(getAcquisitionDate()) : null;
			AcquiredLicense acquiredLicense = new AcquiredLicense();
			acquiredLicense.setLicense(entityMapper.find(License.class, Long.parseLong(getLicenseID())));
			acquiredLicense.setAcquisitionDate(_acquisitonDate);
			employee.getAcquiredLicenses().add(acquiredLicense);
			entityMapper.update(Employee.class, employee);
			setLicenseID(null);
			setAcquisitionDate(null);
		}
	}
	
	public void addTraining() {
		if (!getTrainingID().equals("0")) {
			Employee employee = editableEmployee.getEntity();
			CustomDate _startDate = getStartDate() != null && !getStartDate().trim().isEmpty()
				? new CustomDate(getStartDate()) : null;
			CustomDate _endDate = getEndDate() != null && !getEndDate().trim().isEmpty()
				? new CustomDate(getEndDate()) : null;
			ScheduledTraining scheduledTraining = new ScheduledTraining();
			scheduledTraining.setTraining(entityMapper.find(Training.class, Long.parseLong(getTrainingID())));
			scheduledTraining.setStartDate(_startDate);
			scheduledTraining.setEndDate(_endDate);
			employee.getScheduledTrainings().add(scheduledTraining);
			entityMapper.update(Employee.class, employee);
			setTrainingID(null);
			setStartDate(null);
			setEndDate(null);
		}
	}
	
	public void addProject() {
		if (!getProjectID().equals("0")) {
			Employee employee = editableEmployee.getEntity();
			Project project = entityMapper.find(Project.class, Long.parseLong(getProjectID()));
			employee.getProjects().add(project);
			entityMapper.update(Employee.class, employee);
			setProjectID(null);
		}
	}
	
	public void removeSkill(Skill skill) {
		Employee employee = editableEmployee.getEntity();
		employee.getSkills().remove(skill);
		entityMapper.update(Employee.class, employee);
	}
	
	public void removeLicense(AcquiredLicense acquiredLicense) {
		Employee employee = editableEmployee.getEntity();
		employee.getAcquiredLicenses().remove(acquiredLicense);
		entityMapper.update(Employee.class, employee);
	}
	
	public void removeTraining(ScheduledTraining scheduledTraining) {
		Employee employee = editableEmployee.getEntity();
		employee.getScheduledTrainings().remove(scheduledTraining);
		entityMapper.update(Employee.class, employee);
	}
	
	public void removeProject(Project project) {
		Employee employee = editableEmployee.getEntity();
		employee.getProjects().remove(project);
		entityMapper.update(Employee.class, employee);
	}
	
	public boolean hasSkill(Skill skill) {
		return editableEmployee.getEntity().getSkills().contains(skill);
	}
	
	public boolean hasLicense(License license) {
		List<AcquiredLicense> acquiredLicenses = editableEmployee.getEntity().getAcquiredLicenses();
		long count = acquiredLicenses.stream()
			.filter((acquiredLicense) -> license.equals(acquiredLicense.getLicense()))
			.count();
		return count > 0;
	}
	
	public boolean hasTraining(Training training) {
		List<ScheduledTraining> scheduledTrainings = editableEmployee.getEntity().getScheduledTrainings();
		long count = scheduledTrainings.stream()
			.filter((scheduledTraining) -> training.equals(scheduledTraining.getTraining()))
			.count();
		return count > 0;
	}
	
	public boolean hasProject(Project project) {
		return editableEmployee.getEntity().getProjects().contains(project);
	}
	
	public void enableEditing() {
		editableEmployee.setChangeable(true);
		setFirstName(editableEmployee.getEntity().getFirstName());
		setLastName(editableEmployee.getEntity().getLastName());
		
		if (editableEmployee.getEntity().getSalary() != null) {
			setSalaryAmount(editableEmployee.getEntity().getSalary().getAmount().toString());
			setSalaryCurrency(editableEmployee.getEntity().getSalary().getCurrency());
		}
		
		if (editableEmployee.getEntity().getWorkingHourStart() != null) {
			setWorkingHourStart(editableEmployee.getEntity().getWorkingHourStart().toString());
		}
		
		if (editableEmployee.getEntity().getWorkingHourEnd() != null) {
			setWorkingHourEnd(editableEmployee.getEntity().getWorkingHourEnd().toString());
		}
		
		if (editableEmployee.getEntity().getEmploymentStatus() != null) {
			setEmploymentStatusID(Long.toString(editableEmployee.getEntity().getEmploymentStatus().getId()));
		}
		
		if (editableEmployee.getEntity().getOrganizationalUnit()!= null) {
			setOrganizationalUnitID(Long.toString(editableEmployee.getEntity().getOrganizationalUnit().getId()));
		}
		
		if (editableEmployee.getEntity().getJob()!= null) {
			setJobID(Long.toString(editableEmployee.getEntity().getJob().getId()));
		}
	}
	
	public void cancelEditing() {
		editableEmployee.setChangeable(false);
		setFirstName(null);
		setLastName(null);
		setSalaryAmount(null);
		setSalaryCurrency(null);
	}
	
	public void loadEmployee() {
		if (editableEmployee == null && id != null) {
			long employeeID = Long.parseLong(id);
			Employee employee = entityMapper.find(Employee.class, employeeID);
			editableEmployee = new EditableEntity<>(employee);
		}
	}
	
	// Following properties and methods are used in updating basic employee data.
	private String firstName;
	private String lastName;
	private String salaryAmount;
	private String salaryCurrency;
	private String workingHourStart;
	private String workingHourEnd;
	private String employmentStatusID;
	private String organizationalUnitID;
	private String jobID;

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getSalaryAmount() {
		return salaryAmount;
	}

	public String getSalaryCurrency() {
		return salaryCurrency;
	}

	public String getWorkingHourStart() {
		return workingHourStart;
	}

	public String getWorkingHourEnd() {
		return workingHourEnd;
	}

	public String getEmploymentStatusID() {
		return employmentStatusID;
	}

	public String getOrganizationalUnitID() {
		return organizationalUnitID;
	}

	public String getJobID() {
		return jobID;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setSalaryAmount(String salaryAmount) {
		this.salaryAmount = salaryAmount;
	}

	public void setSalaryCurrency(String salaryCurrency) {
		this.salaryCurrency = salaryCurrency;
	}

	public void setWorkingHourStart(String workingHourStart) {
		this.workingHourStart = workingHourStart;
	}

	public void setWorkingHourEnd(String workingHourEnd) {
		this.workingHourEnd = workingHourEnd;
	}

	public void setEmploymentStatusID(String employmentStatusID) {
		this.employmentStatusID = employmentStatusID;
	}

	public void setOrganizationalUnitID(String organizationalUnitID) {
		this.organizationalUnitID = organizationalUnitID;
	}

	public void setJobID(String jobID) {
		this.jobID = jobID;
	}
	
	// Following properties and methods are used to update employee's skills.
	private String skillID;

	public String getSkillID() {
		return skillID;
	}

	public void setSkillID(String skillID) {
		this.skillID = skillID;
	}
	
	// Following properties and methods are used to update employee's licenses.
	private String licenseID;
	private String acquisitionDate;

	public String getLicenseID() {
		return licenseID;
	}

	public String getAcquisitionDate() {
		return acquisitionDate;
	}

	public void setLicenseID(String licenseID) {
		this.licenseID = licenseID;
	}

	public void setAcquisitionDate(String acquisitionDate) {
		this.acquisitionDate = acquisitionDate;
	}
	
	// Following properties and methods are used to update employee's training.
	private String trainingID;
	private String startDate;
	private String endDate;

	public String getTrainingID() {
		return trainingID;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setTrainingID(String trainingID) {
		this.trainingID = trainingID;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	// Following properties and methods are used to update employee's project participation.
	private String projectID;

	public String getProjectID() {
		return projectID;
	}

	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}
}
