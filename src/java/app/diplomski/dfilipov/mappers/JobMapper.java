package app.diplomski.dfilipov.mappers;

import app.diplomski.dfilipov.beans.session.EntityMap;
import app.diplomski.dfilipov.beans.session.EntityMapper;
import app.diplomski.dfilipov.entities.Job;
import app.diplomski.dfilipov.maps.IdentityMap;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JobMapper extends AbstractMapper<Job> {

	private final EntityMap entityMap;
	private final EntityMapper entityMapper;

	public static final String CREATE_STATEMENT		= "INSERT INTO job VALUES (DEFAULT, ?, ?)";
	public static final String UPDATE_STATEMENT		= "UPDATE job SET name=?, description=? WHERE id=?";
	public static final String DELETE_STATEMENT		= "DELETE FROM job WHERE id=?";
	public static final String FIND_STATEMENT		= "SELECT * FROM job WHERE id=?";
	public static final String FIND_ALL_STATEMENT	= "SELECT * FROM job";
	
	public JobMapper() {
		this.entityMapper = lookupEntityMapperBean();
		this.entityMap = lookupEntityMapBean();
	}
	
	@Override
	protected IdentityMap<Job> map() {
		return entityMap.getMap(Job.class);
	}

	@Override
	protected Job doLoad(ResultSet rs) throws SQLException {
		Job job = new Job();
		job.setId(rs.getLong("id"));
		job.setName(rs.getString("name"));
		job.setDescription(rs.getString("description"));		
		return job;
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
	protected void setCreateStatementParameters(PreparedStatement ps, Job entity) throws SQLException {
		ps.setString(1, entity.getName());
		ps.setString(2, entity.getDescription());
	}

	@Override
	protected void setUpdateStatementParameters(PreparedStatement ps, Job entity) throws SQLException {
		ps.setString(1, entity.getName());
		ps.setString(2, entity.getDescription());
		ps.setLong(3, entity.getId());
	}

	@Override
	protected void setDeleteStatementParameters(PreparedStatement ps, Job entity) throws SQLException {
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
