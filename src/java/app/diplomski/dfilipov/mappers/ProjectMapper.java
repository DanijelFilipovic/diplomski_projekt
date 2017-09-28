package app.diplomski.dfilipov.mappers;

import app.diplomski.dfilipov.beans.session.EntityMap;
import app.diplomski.dfilipov.beans.session.EntityMapper;
import app.diplomski.dfilipov.entities.Customer;
import app.diplomski.dfilipov.entities.Employee;
import app.diplomski.dfilipov.entities.Project;
import app.diplomski.dfilipov.maps.IdentityMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ProjectMapper extends AbstractMapper<Project> {

	private final EntityMap entityMap;
	private final EntityMapper entityMapper;

	private static final String CREATE_STATEMENT	= "INSERT INTO project VALUES (DEFAULT, ?, ?)";
	private static final String UPDATE_STATEMENT	= "UPDATE project SET name=?, customer=? WHERE id=?";
	private static final String DELETE_STATEMENT	= "DELETE FROM project WHERE id=?";
	private static final String FIND_STATEMENT		= "SELECT * FROM project WHERE id=?";
	private static final String FIND_ALL_STATEMENT	= "SELECT * FROM project";
	
	private static final String FIND_FOR_EMPLOYEE_STATEMENT = "SELECT p.* FROM project p, employee_project ep "
															+ "WHERE p.id=ep.project AND ep.employee=?";
	
	public ProjectMapper() {
		this.entityMapper = lookupEntityMapperBean();
		this.entityMap = lookupEntityMapBean();
	}	
	
	public void loadForEmployee(Employee employee) {
		List<Project> projects = new ArrayList<>();
		try (Connection conn = createConnection(); PreparedStatement ps = conn.prepareStatement(FIND_FOR_EMPLOYEE_STATEMENT)) {
			ps.setLong(1, employee.getId());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Project project = load(rs);
					projects.add(project);
				}
			}
		} catch (SQLException ex) {
			System.out.println("Unable to find projects for an employee (" + getClass().getSimpleName() + "): " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		employee.setProjects(projects);
	}
	
	@Override
	protected IdentityMap<Project> map() {
		return entityMap.getMap(Project.class);
	}

	@Override
	protected Project doLoad(ResultSet rs) throws SQLException {
		Project project = new Project();
		project.setId(rs.getLong("id"));
		project.setName(rs.getString("name"));
		
		long customerID = rs.getLong("customer");
		if (customerID != 0) {
			project.setCustomer(entityMapper.find(Customer.class, customerID));
		}
		
		return project;
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
	protected void setCreateStatementParameters(PreparedStatement ps, Project entity) throws SQLException {
		ps.setString(1, entity.getName());
		if (entity.getCustomer() != null) {
			ps.setLong(2, entity.getCustomer().getId());
		} else {
			ps.setNull(2, Types.INTEGER);
		}
	}

	@Override
	protected void setUpdateStatementParameters(PreparedStatement ps, Project entity) throws SQLException {
		ps.setString(1, entity.getName());
		if (entity.getCustomer() != null) {
			ps.setLong(2, entity.getCustomer().getId());
		} else {
			ps.setNull(2, Types.INTEGER);
		}
		ps.setLong(3, entity.getId());
	}

	@Override
	protected void setDeleteStatementParameters(PreparedStatement ps, Project entity) throws SQLException {
		ps.setLong(1, entity.getId());
	}

	@Override
	protected void setFindStatementParameters(PreparedStatement ps, long id) throws SQLException {
		ps.setLong(1, id);
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
