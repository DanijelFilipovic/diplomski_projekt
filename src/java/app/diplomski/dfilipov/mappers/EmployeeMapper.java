package app.diplomski.dfilipov.mappers;

import app.diplomski.dfilipov.beans.session.EntityMap;
import app.diplomski.dfilipov.beans.session.EntityMapper;
import app.diplomski.dfilipov.entities.Employee;
import app.diplomski.dfilipov.entities.EmploymentStatus;
import app.diplomski.dfilipov.entities.Job;
import app.diplomski.dfilipov.entities.OrganizationalUnit;
import app.diplomski.dfilipov.entities.Project;
import app.diplomski.dfilipov.entities.Skill;
import app.diplomski.dfilipov.maps.IdentityMap;
import app.diplomski.dfilipov.utils.money.Money;
import app.diplomski.dfilipov.utils.time.CustomTime;
import app.diplomski.dfilipov.utils.wrappers.AcquiredLicense;
import app.diplomski.dfilipov.utils.wrappers.ScheduledTraining;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EmployeeMapper extends AbstractMapper<Employee> {

	private final EntityMapper entityMapper;
	private final EntityMap entityMap;
	
	private static final String CREATE_STATEMENT	= "INSERT INTO employee VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_STATEMENT	= "UPDATE employee SET first_name=?, last_name=?, salary_amount=?, "
													+ "salary_currency=?, working_hour_start=?, working_hour_end=?, "
													+ "employment_status=?, organizational_unit=?, job=? WHERE id=?";
	private static final String DELETE_STATEMENT	= "DELETE FROM employee WHERE id=?";
	private static final String FIND_STATEMENT		= "SELECT * FROM employee WHERE id=?";
	private static final String FIND_ALL_STATEMENT	= "SELECT * FROM employee";

	private static final String CREATE_EMPLOYEE_SKILLS_STATEMENT	= "INSERT INTO employee_skill VALUES (?, ?)";
	private static final String CREATE_EMPLOYEE_LICENSES_STATEMENT	= "INSERT INTO employee_license VALUES (?, ?, ?)";
	private static final String CREATE_EMPLOYEE_TRAININGS_STATEMENT	= "INSERT INTO employee_training VALUES (?, ?, ?, ?)";
	private static final String CREATE_EMPLOYEE_PROJECTS_STATEMENT	= "INSERT INTO employee_project VALUES (?, ?)";
	
	private static final String DELETE_EMPLOYEE_SKILLS_STATEMENT	= "DELETE FROM employee_skill WHERE employee=?";
	private static final String DELETE_EMPLOYEE_LICENSES_STATEMENT	= "DELETE FROM employee_license WHERE employee=?";
	private static final String DELETE_EMPLOYEE_TRAININGS_STATEMENT	= "DELETE FROM employee_training WHERE employee=?";
	private static final String DELETE_EMPLOYEE_PROJECT_STATEMENT	= "DELETE FROM employee_project WHERE employee=?";
	
	public EmployeeMapper() {
		this.entityMapper = lookupEntityMapperBean();
		this.entityMap = lookupEntityMapBean();
	}
	
	@Override
	protected IdentityMap<Employee> map() {
		return entityMap.getMap(Employee.class);
	}
	
	@Override
	protected void doUpdate(Connection connection, Employee entity) throws SQLException {
		super.doUpdate(connection, entity);
		deleteEmployeeSkills(connection, entity);
		deleteEmployeeLicenses(connection, entity);
		deleteEmployeeTrainings(connection, entity);
		deleteEmployeeProjects(connection, entity);
		createEmployeeSkills(connection, entity);
		createEmployeeLicenses(connection, entity);
		createEmployeeTrainings(connection, entity);
		createEmployeeProjects(connection, entity);
	}
	
	@Override
	protected Employee doLoad(ResultSet rs) throws SQLException {
		Employee employee = new Employee();
		employee.setId(rs.getLong("id"));
		employee.setFirstName(rs.getString("first_name"));
		employee.setLastName(rs.getString("last_name"));
		
		BigDecimal salaryAmount = rs.getBigDecimal("salary_amount");
		String salaryCurrency = rs.getString("salary_currency");
		if (salaryAmount != null && salaryCurrency != null) {
			employee.setSalary(new Money(salaryAmount, salaryCurrency));
		}
		
		java.sql.Time sqlWorkingHourStart = rs.getTime("working_hour_start");
		if (sqlWorkingHourStart != null) {
			employee.setWorkingHourStart(new CustomTime(sqlWorkingHourStart));
		}
		
		java.sql.Time sqlWorkingHourEnd = rs.getTime("working_hour_end");
		if (sqlWorkingHourEnd != null) {
			employee.setWorkingHourEnd(new CustomTime(sqlWorkingHourEnd));
		}
		
		long employmentStatusID = rs.getLong("employment_status");
		if (employmentStatusID != 0) {
			employee.setEmploymentStatus(entityMapper.find(EmploymentStatus.class, employmentStatusID));
		}
		
		long organizationalUnitID = rs.getLong("organizational_unit");
		if (organizationalUnitID != 0) {
			employee.setOrganizationalUnit(entityMapper.find(OrganizationalUnit.class, organizationalUnitID));
		}
		
		long jobID = rs.getLong("job");
		if (jobID != 0) {
			employee.setJob(entityMapper.find(Job.class, jobID));
		}
		
		return employee;
	}

	@Override
	protected String createStatement() {
		return CREATE_STATEMENT;
	}

	@Override
	protected String updateStatement() {
		return UPDATE_STATEMENT;
	}

	@Override
	protected String deleteStatement() {
		return DELETE_STATEMENT;
	}

	@Override
	protected String findStatement() {
		return FIND_STATEMENT;
	}

	@Override
	protected String findAllStatement() {
		return FIND_ALL_STATEMENT;
	}

	@Override
	protected void setCreateStatementParameters(PreparedStatement ps, Employee entity) throws SQLException {
		ps.setString(1, entity.getFirstName());
		ps.setString(2, entity.getLastName());
		
		if (entity.getSalary() != null) {
			ps.setBigDecimal(3, entity.getSalary().getAmount());
			ps.setString(4, entity.getSalary().getCurrency());
		} else {
			ps.setNull(3, Types.DECIMAL);
			ps.setNull(4, Types.CHAR);
		}
		
		if (entity.getWorkingHourStart() != null) {
			ps.setTime(5, entity.getWorkingHourStart().toSqlTime());
		} else {
			ps.setNull(5, Types.TIME);
		}
		
		if (entity.getWorkingHourEnd() != null) {
			ps.setTime(6, entity.getWorkingHourEnd().toSqlTime());
		} else {
			ps.setNull(6, Types.TIME);
		}
		
		if (entity.getEmploymentStatus() != null) {
			ps.setLong(7, entity.getEmploymentStatus().getId());
		} else {
			ps.setNull(7, Types.INTEGER);
		}
		
		if (entity.getOrganizationalUnit() != null) {
			ps.setLong(8, entity.getOrganizationalUnit().getId());
		} else {
			ps.setNull(8, Types.INTEGER);
		}
		
		if (entity.getJob() != null) {
			ps.setLong(9, entity.getJob().getId());
		} else {
			ps.setNull(9, Types.INTEGER);
		}
	}

	@Override
	protected void setUpdateStatementParameters(PreparedStatement ps, Employee entity) throws SQLException {
		ps.setString(1, entity.getFirstName());
		ps.setString(2, entity.getLastName());
		
		if (entity.getSalary() != null) {
			ps.setBigDecimal(3, entity.getSalary().getAmount());
			ps.setString(4, entity.getSalary().getCurrency());
		} else {
			ps.setNull(3, Types.DECIMAL);
			ps.setNull(4, Types.CHAR);
		}
		
		if (entity.getWorkingHourStart() != null) {
			ps.setTime(5, entity.getWorkingHourStart().toSqlTime());
		} else {
			ps.setNull(5, Types.TIME);
		}
		
		if (entity.getWorkingHourEnd() != null) {
			ps.setTime(6, entity.getWorkingHourEnd().toSqlTime());
		} else {
			ps.setNull(6, Types.TIME);
		}
		
		if (entity.getEmploymentStatus() != null) {
			ps.setLong(7, entity.getEmploymentStatus().getId());
		} else {
			ps.setNull(7, Types.INTEGER);
		}
		
		if (entity.getOrganizationalUnit() != null) {
			ps.setLong(8, entity.getOrganizationalUnit().getId());
		} else {
			ps.setNull(8, Types.INTEGER);
		}
		
		if (entity.getJob() != null) {
			ps.setLong(9, entity.getJob().getId());
		} else {
			ps.setNull(9, Types.INTEGER);
		}
		
		ps.setLong(10, entity.getId());
	}

	@Override
	protected void setDeleteStatementParameters(PreparedStatement ps, Employee entity) throws SQLException {
		ps.setLong(1, entity.getId());
	}

	@Override
	protected void setFindStatementParameters(PreparedStatement ps, long id) throws SQLException {
		ps.setLong(1, id);
	}

	private void createEmployeeSkills(Connection connection, Employee entity) throws SQLException {
		try (PreparedStatement ps = connection.prepareStatement(CREATE_EMPLOYEE_SKILLS_STATEMENT)) {
			ps.setLong(1, entity.getId());
			for (Skill skill : entity.getSkills()) {
				ps.setLong(2, skill.getId());
				ps.execute();
			}
		}
	}

	private void createEmployeeLicenses(Connection connection, Employee entity) throws SQLException {
		try (PreparedStatement ps = connection.prepareStatement(CREATE_EMPLOYEE_LICENSES_STATEMENT)) {
			ps.setLong(1, entity.getId());
			for (AcquiredLicense acquiredLicense : entity.getAcquiredLicenses()) {
				ps.setLong(2, acquiredLicense.getLicense().getId());
				if (acquiredLicense.getAcquisitionDate() != null) {
					ps.setDate(3, acquiredLicense.getAcquisitionDate().toSqlDate());
				} else {
					ps.setNull(3, Types.DATE);
				}
				ps.execute();
			}
		}
	}

	private void createEmployeeTrainings(Connection connection, Employee entity) throws SQLException {
		try (PreparedStatement ps = connection.prepareStatement(CREATE_EMPLOYEE_TRAININGS_STATEMENT)) {
			ps.setLong(1, entity.getId());
			for (ScheduledTraining scheduledTraining : entity.getScheduledTrainings()) {
				ps.setLong(2, scheduledTraining.getTraining().getId());
				if (scheduledTraining.getStartDate() != null) {
					ps.setDate(3, scheduledTraining.getStartDate().toSqlDate());
				} else {
					ps.setNull(3, Types.DATE);
				}
				if (scheduledTraining.getEndDate() != null) {
					ps.setDate(4, scheduledTraining.getEndDate().toSqlDate());
				} else {
					ps.setNull(4, Types.DATE);
				}
				ps.execute();
			}
		}
	}
	
	private void createEmployeeProjects(Connection connection, Employee entity) throws SQLException {
		try (PreparedStatement ps = connection.prepareStatement(CREATE_EMPLOYEE_PROJECTS_STATEMENT)) {
			ps.setLong(1, entity.getId());
			for (Project project : entity.getProjects()) {
				ps.setLong(2, project.getId());
				ps.execute();
			}
		}
	}
	
	private void deleteEmployeeSkills(Connection connection, Employee entity) throws SQLException {
		try (PreparedStatement ps = connection.prepareStatement(DELETE_EMPLOYEE_SKILLS_STATEMENT)) {
			ps.setLong(1, entity.getId());
			ps.execute();
		}
	}

	private void deleteEmployeeLicenses(Connection connection, Employee entity) throws SQLException {
		try (PreparedStatement ps = connection.prepareStatement(DELETE_EMPLOYEE_LICENSES_STATEMENT)) {
			ps.setLong(1, entity.getId());
			ps.execute();
		}
	}

	private void deleteEmployeeTrainings(Connection connection, Employee entity) throws SQLException {
		try (PreparedStatement ps = connection.prepareStatement(DELETE_EMPLOYEE_TRAININGS_STATEMENT)) {
			ps.setLong(1, entity.getId());
			ps.execute();
		}
	}
	
	private void deleteEmployeeProjects(Connection connection, Employee entity) throws SQLException {
		try (PreparedStatement ps = connection.prepareStatement(DELETE_EMPLOYEE_PROJECT_STATEMENT)) {
			ps.setLong(1, entity.getId());
			ps.execute();
		}
	}
	
	private EntityMap lookupEntityMapBean() {
		try {
			Context c = new InitialContext();
			return (EntityMap) c.lookup("java:global/dfilipov_diplomski_app/EntityMap!app.diplomski.dfilipov.beans.session.EntityMap");
		} catch (NamingException ne) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
			throw new RuntimeException(ne);
		}
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