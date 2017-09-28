package app.diplomski.dfilipov.beans.managed.entity;

import app.diplomski.dfilipov.beans.session.EntityMapper;
import app.diplomski.dfilipov.entities.Employee;
import app.diplomski.dfilipov.utils.wrappers.EditableEntity;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "employeeBean")
@ViewScoped
public class EmployeeBean extends AbstractEntityBean<Employee> {

	@EJB private EntityMapper entityMapper;
	
	public EmployeeBean() {
		super(Employee.class);
	}

	@Override
	protected EntityMapper entityMapper() {
		return entityMapper;
	}

	@Override
	protected Employee newEntity() {
		return new Employee();
	}

	@Override
	protected void populateEntity(Employee entity) {
		entity.setFirstName(getFirstName());
		entity.setLastName(getLastName());
	}

	@Override
	public void updateEntity(EditableEntity<Employee> editableEntity) {
	}
	
	@Override
	protected void setBeanData(Employee entity) {
	}

	@Override
	protected void clearBeanData() {
		setFirstName(null);
		setLastName(null);
	}
	
	// Following properties are used for getting and setting data for adding or updating customers.
	private String firstName;
	private String lastName;

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
