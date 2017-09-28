package app.diplomski.dfilipov.mappers;

import app.diplomski.dfilipov.beans.session.EntityMap;
import app.diplomski.dfilipov.entities.Employee;
import app.diplomski.dfilipov.entities.License;
import app.diplomski.dfilipov.maps.IdentityMap;
import app.diplomski.dfilipov.utils.time.CustomDate;
import app.diplomski.dfilipov.utils.wrappers.AcquiredLicense;
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

public class LicenseMapper extends AbstractMapper<License> {

	private final EntityMap entityMap;

	private static final String CREATE_STATEMENT	= "INSERT INTO license VALUES (DEFAULT, ?, ?)";
	private static final String UPDATE_STATEMENT	= "UPDATE license SET name=?, description=? WHERE id=?";
	private static final String DELETE_STATEMENT	= "DELETE FROM license WHERE id=?";
	private static final String FIND_STATEMENT		= "SELECT * FROM license WHERE id=?";
	private static final String FIND_ALL_STATEMENT	= "SELECT * FROM license";

	private static final String FIND_FOR_EMPLOYEE_STATEMENT = "SELECT l.*, el.acquisition_date FROM license l, employee_license el "
															+ "WHERE l.id=el.license AND el.employee=?";
	
	public LicenseMapper() {
		this.entityMap = lookupEntityMapBean();
	}
	
	public void loadForEmployee(Employee employee) {
		List<AcquiredLicense> acquiredLicenses = new ArrayList<>();
		try (Connection conn = createConnection(); PreparedStatement ps = conn.prepareStatement(FIND_FOR_EMPLOYEE_STATEMENT)) {
			ps.setLong(1, employee.getId());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					License license = load(rs);
					CustomDate acquisitionDate = null;
					if (rs.getDate("acquisition_date") != null) {
						acquisitionDate = new CustomDate(rs.getDate("acquisition_date"));
					}
					AcquiredLicense acquiredLicense = new AcquiredLicense();
					acquiredLicense.setLicense(license);
					acquiredLicense.setAcquisitionDate(acquisitionDate);
					acquiredLicenses.add(acquiredLicense);
				}
			}
		} catch (SQLException ex) {
			System.out.println("Unable to find licenses for an employee (" + getClass().getSimpleName() + "): " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		employee.setAcquiredLicenses(acquiredLicenses);
	}
	
	@Override
	protected IdentityMap<License> map() {
		return entityMap.getMap(License.class);
	}

	@Override
	protected License doLoad(ResultSet rs) throws SQLException {
		License license = new License();
		license.setId(rs.getLong("id"));
		license.setName(rs.getString("name"));
		license.setDescription(rs.getString("description"));
		return license;
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
	protected void setCreateStatementParameters(PreparedStatement ps, License entity) throws SQLException {
		ps.setString(1, entity.getName());
		ps.setString(2, entity.getDescription());
	}

	@Override
	protected void setUpdateStatementParameters(PreparedStatement ps, License entity) throws SQLException {
		ps.setString(1, entity.getName());
		ps.setString(2, entity.getDescription());
		ps.setLong(3, entity.getId());
	}

	@Override
	protected void setDeleteStatementParameters(PreparedStatement ps, License entity) throws SQLException {
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
