package app.diplomski.dfilipov.mappers;

import app.diplomski.dfilipov.beans.session.EntityMap;
import app.diplomski.dfilipov.entities.Customer;
import app.diplomski.dfilipov.entities.Project;
import app.diplomski.dfilipov.maps.IdentityMap;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class CustomerMapper extends AbstractMapper<Customer> {

	private final EntityMap entityMap;

	private static final String CREATE_STATEMENT	= "INSERT INTO customer VALUES (DEFAULT, ?, ?, ?, ?)";
	private static final String UPDATE_STATEMENT	= "UPDATE customer SET name=?, address=?, email=?, phone=? WHERE id=?";
	private static final String DELETE_STATEMENT	= "DELETE FROM customer WHERE id=?";
	private static final String FIND_STATEMENT		= "SELECT * FROM customer WHERE id=?";
	private static final String FIND_ALL_STATEMENT	= "SELECT * FROM customer";
	
	public CustomerMapper() {
		this.entityMap = lookupEntityMapBean();
	}

	@Override
	public void delete(Customer entity) {
		super.delete(entity);
		clearReferencesFromProjects(entity);
	}

	@Override
	protected IdentityMap<Customer> map() {
		return entityMap.getMap(Customer.class);
	}

	@Override
	protected Customer doLoad(ResultSet rs) throws SQLException {
		Customer customer = new Customer();
		customer.setId(rs.getLong("id"));
		customer.setName(rs.getString("name"));
		customer.setAddress(rs.getString("address"));
		customer.setEmailAddress(rs.getString("email"));
		customer.setPhoneNumber(rs.getString("phone"));
		return customer;
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
	protected void setCreateStatementParameters(PreparedStatement ps, Customer entity) throws SQLException {
		ps.setString(1, entity.getName());
		ps.setString(2, entity.getAddress());
		ps.setString(3, entity.getEmailAddress());
		ps.setString(4, entity.getPhoneNumber());
	}

	@Override
	protected void setUpdateStatementParameters(PreparedStatement ps, Customer entity) throws SQLException {
		ps.setString(1, entity.getName());
		ps.setString(2, entity.getAddress());
		ps.setString(3, entity.getEmailAddress());
		ps.setString(4, entity.getPhoneNumber());
		ps.setLong(5, entity.getId());
	}

	@Override
	protected void setDeleteStatementParameters(PreparedStatement ps, Customer entity) throws SQLException {
		ps.setLong(1, entity.getId());
	}

	@Override
	protected void setFindStatementParameters(PreparedStatement ps, long id) throws SQLException {
		ps.setLong(1, id);
	}
	
	private void clearReferencesFromProjects(Customer entity) {
		entityMap
			.filter(Project.class, (project) -> project.getCustomer().equals(entity))
			.forEach((project) -> project.setCustomer(null));
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
