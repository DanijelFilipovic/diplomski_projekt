package app.diplomski.dfilipov.mappers;

import app.diplomski.dfilipov.entities.Entity;
import app.diplomski.dfilipov.listeners.ApplicationListener;
import app.diplomski.dfilipov.maps.IdentityMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public abstract class AbstractMapper<T extends Entity> {
	
	public void create(T entity) {
		try (Connection connection = createConnection()) {
			doCreate(connection, entity);
			map().add(entity);
		} catch (SQLException ex) {
			System.out.println("Unable to create entity (" + getClass().getSimpleName() + "): " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
	}
	
	public void update(T entity) {
		try (Connection connection = createConnection()) {
			doUpdate(connection, entity);
		} catch (SQLException ex) {
			System.out.println("Unable to update entity (" + getClass().getSimpleName() + "): " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
	}
	
	public void delete(T entity) {
		try (Connection connection = createConnection()) {
			doDelete(connection, entity);
			map().remove(entity.getId());
		} catch (SQLException ex) {
			System.out.println("Unable to delete entity (" + getClass().getSimpleName() + "): " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
	}
	
	public T load(ResultSet rs) {
		try {
			long id = rs.getLong("id");
			if (map().contains(id)) {
				return map().get(id);
			}
			T entity = doLoad(rs);
			return entity;
		} catch (SQLException ex) {
			System.out.println("Unable to load entity (" + getClass().getSimpleName() + "): " + ex.getMessage());
			ex.printStackTrace(System.out);
			return null;
		}
	}
	
	public T find(long id) {
		if (map().contains(id)) {
			return map().get(id);
		}
		try (Connection connection = createConnection(); PreparedStatement ps = connection.prepareStatement(findStatement())) {
			setFindStatementParameters(ps, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				T entity = load(rs);
				return entity;
			} else {
				return null;
			}
		} catch (SQLException ex) {
			System.out.println("Unable to find entity (" + getClass().getSimpleName() + "): " + ex.getMessage());
			ex.printStackTrace(System.out);
			return null;
		}
	}
	
	public List<T> findAll() {
		List<T> entities = new ArrayList<>();
		try (Connection connection = createConnection(); 
			 PreparedStatement ps = connection.prepareStatement(findAllStatement()); 
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				T entity = load(rs);
				entities.add(entity);
			}
		} catch (SQLException ex) {
			System.out.println("Unable to find all entities (" + getClass().getSimpleName() + "): " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		return entities;
	}

	protected void doCreate(Connection connection, T entity) throws SQLException {
		try (PreparedStatement ps = connection.prepareStatement(createStatement(), Statement.RETURN_GENERATED_KEYS)) {
			setCreateStatementParameters(ps, entity);
			ps.execute();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				rs.next();
				entity.setId(rs.getLong(1));
			}
		}
	}

	protected void doUpdate(Connection connection, T entity) throws SQLException {
		try (PreparedStatement ps = connection.prepareStatement(updateStatement())) {
			setUpdateStatementParameters(ps, entity);
			ps.execute();
		}
	}

	protected void doDelete(Connection connection, T entity) throws SQLException {
		try (PreparedStatement ps = connection.prepareStatement(deleteStatement())) {
			setDeleteStatementParameters(ps, entity);
			ps.execute();
		}
	}
	
	protected Connection createConnection() {
		try {
			DataSource dataSource = (DataSource) ApplicationListener.getServletContext().getAttribute("DerbyDB");
			return dataSource.getConnection();
		} catch (SQLException ex) {
			ex.printStackTrace(System.out);
			throw new RuntimeException(ex);
		}
	}
	
	protected abstract IdentityMap<T> map();
	protected abstract T doLoad(ResultSet rs) throws SQLException;
	protected abstract String createStatement();
	protected abstract String updateStatement();
	protected abstract String deleteStatement();
	protected abstract String findStatement();
	protected abstract String findAllStatement();
	protected abstract void setCreateStatementParameters(PreparedStatement ps, T entity) throws SQLException;
	protected abstract void setUpdateStatementParameters(PreparedStatement ps, T entity) throws SQLException;
	protected abstract void setDeleteStatementParameters(PreparedStatement ps, T entity) throws SQLException;
	protected abstract void setFindStatementParameters(PreparedStatement ps, long id) throws SQLException;

}
