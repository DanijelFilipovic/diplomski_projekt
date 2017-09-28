package app.diplomski.dfilipov.mappers;

import app.diplomski.dfilipov.beans.session.EntityMap;
import app.diplomski.dfilipov.entities.Employee;
import app.diplomski.dfilipov.entities.Training;
import app.diplomski.dfilipov.maps.IdentityMap;
import app.diplomski.dfilipov.utils.time.CustomDate;
import app.diplomski.dfilipov.utils.wrappers.ScheduledTraining;
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

public class TrainingMapper extends AbstractMapper<Training> {

	private final EntityMap entityMap;

	private static final String CREATE_STATEMENT	= "INSERT INTO training VALUES (DEFAULT, ?, ?)";
	private static final String UPDATE_STATEMENT	= "UPDATE training SET name=?, description=? WHERE id=?";
	private static final String DELETE_STATEMENT	= "DELETE FROM training WHERE id=?";
	private static final String FIND_STATEMENT		= "SELECT * FROM training WHERE id=?";
	private static final String FIND_ALL_STATEMENT	= "SELECT * FROM training";
	
	private static final String FIND_FOR_EMPLOYEE_STATEMENT = "SELECT t.*, et.start_date, et.end_date "
															+ "FROM training t, employee_training et "
															+ "WHERE t.id=et.training AND et.employee=?";
	
	public TrainingMapper() {
		this.entityMap = lookupEntityMapBean();
	}
	
	public void loadForEmployee(Employee employee) {
		List<ScheduledTraining> scheduledTrainings = new ArrayList<>();
		try (Connection conn = createConnection(); PreparedStatement ps = conn.prepareStatement(FIND_FOR_EMPLOYEE_STATEMENT)) {
			ps.setLong(1, employee.getId());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Training training = load(rs);
					CustomDate startDate = null;
					if (rs.getDate("start_date") != null) {
						startDate = new CustomDate(rs.getDate("start_date"));
					}
					CustomDate endDate = null;
					if (rs.getDate("end_date") != null) {
						endDate = new CustomDate(rs.getDate("end_date"));
					}
					ScheduledTraining scheduledTraining = new ScheduledTraining();
					scheduledTraining.setTraining(training);
					scheduledTraining.setStartDate(startDate);
					scheduledTraining.setEndDate(endDate);
					scheduledTrainings.add(scheduledTraining);
				}
			}
		} catch (SQLException ex) {
			System.out.println("Unable to find trainings for an employee (" + getClass().getSimpleName() + "): " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		employee.setScheduledTrainings(scheduledTrainings);
	}

	@Override
	protected IdentityMap<Training> map() {
		return entityMap.getMap(Training.class);
	}

	@Override
	protected Training doLoad(ResultSet rs) throws SQLException {
		Training training = new Training();
		training.setId(rs.getLong("id"));
		training.setName(rs.getString("name"));
		training.setDescription(rs.getString("description"));
		return training;
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
	protected void setCreateStatementParameters(PreparedStatement ps, Training entity) throws SQLException {
		ps.setString(1, entity.getName());
		ps.setString(2, entity.getDescription());
	}

	@Override
	protected void setUpdateStatementParameters(PreparedStatement ps, Training entity) throws SQLException {
		ps.setString(1, entity.getName());
		ps.setString(2, entity.getDescription());
		ps.setLong(3, entity.getId());
	}

	@Override
	protected void setDeleteStatementParameters(PreparedStatement ps, Training entity) throws SQLException {
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
