package app.diplomski.dfilipov.mappers;

import app.diplomski.dfilipov.beans.session.EntityMap;
import app.diplomski.dfilipov.entities.EmploymentStatus;
import app.diplomski.dfilipov.maps.IdentityMap;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EmploymentStatusMapper extends AbstractMapper<EmploymentStatus> {

	private final EntityMap entityMap;
	
	private static final String CREATE_STATEMENT	= "INSERT INTO employment_status VALUES (DEFAULT, ?, ?)";
	private static final String UPDATE_STATEMENT	= "UPDATE employment_status SET name=?, description=? WHERE id=?";
	private static final String DELETE_STATEMENT	= "DELETE FROM employment_status WHERE id=?";
	private static final String FIND_STATEMENT		= "SELECT * FROM employment_status WHERE id=?";
	private static final String FIND_ALL_STATEMENT	= "SELECT * FROM employment_status";

	public EmploymentStatusMapper() {
		this.entityMap = lookupEntityMapBean();
	}

	@Override
	protected IdentityMap<EmploymentStatus> map() {
		return entityMap.getMap(EmploymentStatus.class);
	}
	
	@Override
	protected EmploymentStatus doLoad(ResultSet rs) throws SQLException {
		EmploymentStatus employmentStatus = new EmploymentStatus();
		employmentStatus.setId(rs.getLong("id"));
		employmentStatus.setName(rs.getString("name"));
		employmentStatus.setDescription(rs.getString("description"));
		return employmentStatus;
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
	protected void setCreateStatementParameters(PreparedStatement ps, EmploymentStatus entity) throws SQLException {
		ps.setString(1, entity.getName());
		ps.setString(2, entity.getDescription());
	}

	@Override
	protected void setUpdateStatementParameters(PreparedStatement ps, EmploymentStatus entity) throws SQLException {
		ps.setString(1, entity.getName());
		ps.setString(2, entity.getDescription());
		ps.setLong(3, entity.getId());
	}

	@Override
	protected void setDeleteStatementParameters(PreparedStatement ps, EmploymentStatus entity) throws SQLException {
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
