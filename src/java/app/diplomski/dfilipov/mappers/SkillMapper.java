package app.diplomski.dfilipov.mappers;

import app.diplomski.dfilipov.beans.session.EntityMap;
import app.diplomski.dfilipov.entities.Employee;
import app.diplomski.dfilipov.entities.Skill;
import app.diplomski.dfilipov.maps.IdentityMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SkillMapper extends AbstractMapper<Skill> {

	private final EntityMap entityMap;
	
	private static final String CREATE_STATEMENT	= "INSERT INTO skill VALUES (DEFAULT, ?, ?)";
	private static final String UPDATE_STATEMENT	= "UPDATE skill SET name=?, description=? WHERE id=?";
	private static final String DELETE_STATEMENT	= "DELETE FROM skill WHERE id=?";
	private static final String FIND_STATEMENT		= "SELECT * FROM skill WHERE id=?";
	private static final String FIND_ALL_STATEMENT	= "SELECT * FROM skill";

	private static final String FIND_FOR_EMPLOYEE_STATEMENT = "SELECT s.* FROM skill s, employee_skill es "
															+ "WHERE s.id=es.skill AND es.employee=?";
	
	public SkillMapper() {
		this.entityMap = lookupEntityMapBean();
	}
	
	public List<Skill> findForEmployee(Employee employee) {
		List<Skill> skills = new ArrayList<>();
		try (Connection conn = createConnection(); PreparedStatement ps = conn.prepareStatement(FIND_FOR_EMPLOYEE_STATEMENT)) {
			ps.setLong(1, employee.getId());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Skill skill = load(rs);
					skills.add(skill);
				}
			}
		} catch (SQLException ex) {
			System.out.println("Unable to find skills for an employee (" + getClass().getSimpleName() + "): " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		return skills;
	}

	@Override
	protected IdentityMap<Skill> map() {
		return entityMap.getMap(Skill.class);
	}
	
	@Override
	protected Skill doLoad(ResultSet rs) throws SQLException {
		Skill skill = new Skill();
		skill.setId(rs.getLong("id"));
		skill.setName(rs.getString("name"));
		skill.setDescription(rs.getString("description"));
		return skill;
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
	protected void setCreateStatementParameters(PreparedStatement ps, Skill entity) throws SQLException {
		ps.setString(1, entity.getName());
		ps.setString(2, entity.getDescription());
	}

	@Override
	protected void setUpdateStatementParameters(PreparedStatement ps, Skill entity) throws SQLException {
		ps.setString(1, entity.getName());
		ps.setString(2, entity.getDescription());
		ps.setLong(3, entity.getId());
	}

	@Override
	protected void setDeleteStatementParameters(PreparedStatement ps, Skill entity) throws SQLException {
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
	
}
