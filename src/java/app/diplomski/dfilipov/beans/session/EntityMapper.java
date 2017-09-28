package app.diplomski.dfilipov.beans.session;

import app.diplomski.dfilipov.entities.Customer;
import app.diplomski.dfilipov.entities.Employee;
import app.diplomski.dfilipov.entities.EmploymentStatus;
import app.diplomski.dfilipov.entities.Entity;
import app.diplomski.dfilipov.entities.Job;
import app.diplomski.dfilipov.entities.License;
import app.diplomski.dfilipov.entities.OrganizationalUnit;
import app.diplomski.dfilipov.entities.Project;
import app.diplomski.dfilipov.entities.Skill;
import app.diplomski.dfilipov.entities.Training;
import app.diplomski.dfilipov.mappers.AbstractMapper;
import app.diplomski.dfilipov.mappers.CustomerMapper;
import app.diplomski.dfilipov.mappers.EmployeeMapper;
import app.diplomski.dfilipov.mappers.EmploymentStatusMapper;
import app.diplomski.dfilipov.mappers.JobMapper;
import app.diplomski.dfilipov.mappers.LicenseMapper;
import app.diplomski.dfilipov.mappers.OrganizationalUnitMapper;
import app.diplomski.dfilipov.mappers.ProjectMapper;
import app.diplomski.dfilipov.mappers.SkillMapper;
import app.diplomski.dfilipov.mappers.TrainingMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.ejb.Stateless;

@Stateless
@Startup
@LocalBean
public class EntityMapper {

	private final Map<Class<? extends Entity>, AbstractMapper> mappers = new HashMap<>();
	
	public EntityMapper() {
		mappers.put(Skill.class, new SkillMapper());
		mappers.put(License.class, new LicenseMapper());
		mappers.put(EmploymentStatus.class, new EmploymentStatusMapper());
		mappers.put(OrganizationalUnit.class, new OrganizationalUnitMapper());
		mappers.put(Job.class, new JobMapper());
		mappers.put(Customer.class, new CustomerMapper());
		mappers.put(Project.class, new ProjectMapper());
		mappers.put(Training.class, new TrainingMapper());
		mappers.put(Employee.class, new EmployeeMapper());
	}
	
	public <T extends Entity> AbstractMapper<T> getMapper(Class<T> entityClass) {
		return mappers.get(entityClass);
	}
	
	public <T extends Entity> void create(Class<T> entityClass, T entity) {
		mappers.get(entityClass).create(entity);
	}
	
	public <T extends Entity> void update(Class<T> entityClass, T entity) {
		mappers.get(entityClass).update(entity);
	}
	
	public <T extends Entity> void delete(Class<T> entityClass, T entity) {
		mappers.get(entityClass).delete(entity);
	}
	
	public <T extends Entity> T find(Class<T> entityClass, long id) {
		return (T) mappers.get(entityClass).find(id);
	}
	
	public <T extends Entity> List<T> findAll(Class<T> entityClass) {
		return (List<T>) mappers.get(entityClass).findAll();
	}
}
