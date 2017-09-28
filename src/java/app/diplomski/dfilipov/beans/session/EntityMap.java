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
import app.diplomski.dfilipov.maps.IdentityMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
@LocalBean
public class EntityMap {

	private final Map<Class<? extends Entity>, IdentityMap<?>> maps = new HashMap<>();
	
	public EntityMap() {
		maps.put(Skill.class, new IdentityMap<>());
		maps.put(License.class, new IdentityMap<>());
		maps.put(EmploymentStatus.class, new IdentityMap<>());
		maps.put(OrganizationalUnit.class, new IdentityMap<>());
		maps.put(Job.class, new IdentityMap<>());
		maps.put(Customer.class, new IdentityMap<>());
		maps.put(Project.class, new IdentityMap<>());
		maps.put(Training.class, new IdentityMap<>());
		maps.put(Employee.class, new IdentityMap<>());
	}
	
	public <T extends Entity> IdentityMap<T> getMap(Class<T> entityClass) {
		return (IdentityMap<T>) maps.get(entityClass);
	}
	
	public <T extends Entity> List<T> filter(Class<T> entityClass, Predicate<T> predicate) {
		return ((IdentityMap<T>) maps.get(entityClass)).filter(predicate);
	}
	
	public void clearAll() {
		maps.forEach((entityClass, identityMap) -> identityMap.clear());
	}
}
