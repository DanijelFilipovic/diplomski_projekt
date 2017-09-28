package app.diplomski.dfilipov.mappers;

import app.diplomski.dfilipov.beans.session.EntityMap;
import app.diplomski.dfilipov.entities.Job;
import app.diplomski.dfilipov.entities.OrganizationalUnit;
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

public class OrganizationalUnitMapper extends AbstractMapper<OrganizationalUnit> {

	private final EntityMap entityMap;

	private static final String CREATE_STATEMENT		= "INSERT INTO organizational_unit VALUES (DEFAULT, ?, ?, ?)";
	private static final String UPDATE_STATEMENT		= "UPDATE organizational_unit SET name=?, description=?, superordinate=? WHERE id=?";
	private static final String DELETE_STATEMENT		= "DELETE FROM organizational_unit WHERE id=?";
	private static final String FIND_STATEMENT			= "SELECT * FROM organizational_unit WHERE id=?";
	private static final String FIND_ALL_STATEMENT		= "SELECT * FROM organizational_unit";
	private static final String FIND_SUBS_STATEMENT		= "SELECT * FROM organizational_unit WHERE superordinate=?";	

	public OrganizationalUnitMapper() {
		this.entityMap = lookupEntityMapBean();
	}
	
	public void loadSubordinates(OrganizationalUnit entity) {
		List<OrganizationalUnit> subordinates = new ArrayList<>();
		try (Connection conn = createConnection(); PreparedStatement ps = conn.prepareStatement(FIND_SUBS_STATEMENT)) {
			ps.setLong(1, entity.getId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				subordinates.add(load(rs));
			}
		} catch (SQLException ex) {
			System.out.println("Unable to load subordinates (" + getClass().getSimpleName() + "): " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		entity.setSubordinates(subordinates);
	}
	
	@Override
	public void create(OrganizationalUnit entity) {
		super.create(entity);
		clearReferencesFromOrganizationalUnits(entity);
		saveReferencesToOrganizationalUnits(entity);
	}

	@Override
	public void update(OrganizationalUnit entity) {
		super.update(entity);
		clearReferencesFromOrganizationalUnits(entity);
		saveReferencesToOrganizationalUnits(entity);
	}
	
	@Override
	public void delete(OrganizationalUnit entity) {
		clearReferencesFromOrganizationalUnits(entity);
		super.delete(entity);
	}
	
	@Override
	protected IdentityMap<OrganizationalUnit> map() {
		return entityMap.getMap(OrganizationalUnit.class);
	}

	@Override
	protected OrganizationalUnit doLoad(ResultSet rs) throws SQLException {
		OrganizationalUnit organizationalUnit = new OrganizationalUnit();
		organizationalUnit.setId(rs.getLong("id"));
		organizationalUnit.setName(rs.getString("name"));
		organizationalUnit.setDescription(rs.getString("description"));
		
		long superordinateID = rs.getLong("superordinate");
		if (superordinateID != 0) {
			organizationalUnit.setSuperordinate(find(rs.getLong("superordinate")));
		}
		
		return organizationalUnit;
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
	protected void setCreateStatementParameters(PreparedStatement ps, OrganizationalUnit entity) throws SQLException {
		ps.setString(1, entity.getName());
		ps.setString(2, entity.getDescription());
		
		if (entity.getSuperordinate() != null) {
			ps.setLong(3, entity.getSuperordinate().getId());
		} else {
			ps.setNull(3, Types.INTEGER);
		}
	}

	@Override
	protected void setUpdateStatementParameters(PreparedStatement ps, OrganizationalUnit entity) throws SQLException {
		ps.setString(1, entity.getName());
		ps.setString(2, entity.getDescription());
		
		if (entity.getSuperordinate() != null) {
			ps.setLong(3, entity.getSuperordinate().getId());
		} else {
			ps.setNull(3, Types.INTEGER);
		}
		
		ps.setLong(4, entity.getId());
	}

	@Override
	protected void setDeleteStatementParameters(PreparedStatement ps, OrganizationalUnit entity) throws SQLException {
		ps.setLong(1, entity.getId());
	}

	@Override
	protected void setFindStatementParameters(PreparedStatement ps, long id) throws SQLException {
		ps.setLong(1, id);
	}
	
	private void clearReferencesFromOrganizationalUnits(OrganizationalUnit entity) {
		entityMap.filter(OrganizationalUnit.class, (sub) -> sub.getSuperordinate().equals(entity)).forEach((sub) -> {
			sub.setSuperordinate(null);
		});
		entityMap.filter(OrganizationalUnit.class, (sup) -> sup.getSubordinates().contains(entity)).forEach((sup) -> {
			sup.getSubordinates().remove(entity);
		});
	}
	
	private void saveReferencesToOrganizationalUnits(OrganizationalUnit entity) {
		OrganizationalUnit superordinate = entity.getSuperordinate();
		if (superordinate != null) {
			superordinate.getSubordinates().add(entity);
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
}
