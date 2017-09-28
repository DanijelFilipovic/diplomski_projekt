package app.diplomski.dfilipov.beans.managed.entity;

import app.diplomski.dfilipov.beans.session.EntityMapper;
import app.diplomski.dfilipov.entities.Customer;
import app.diplomski.dfilipov.entities.Project;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "projectBean")
@ViewScoped
public class ProjectBean extends AbstractEntityBean<Project> {

	@EJB private EntityMapper entityMapper;
	
	public ProjectBean() {
		super(Project.class);
	}

	@Override
	protected EntityMapper entityMapper() {
		return entityMapper;
	}

	@Override
	protected Project newEntity() {
		return new Project();
	}

	@Override
	protected void populateEntity(Project entity) {
		entity.setName(getName());
		if (!getCustomerID().equals("0")) {
			long _customerID = Long.parseLong(getCustomerID());
			entity.setCustomer(entityMapper.find(Customer.class, _customerID));
		} else {
			entity.setCustomer(null);
		}
	}

	@Override
	protected void setBeanData(Project entity) {
		setName(entity.getName());
		if (entity.getCustomer() != null) {
			long _customerID = entity.getCustomer().getId();
			setCustomerID(Long.toString(_customerID));
		}
	}

	@Override
	protected void clearBeanData() {
		setName(null);
		setCustomerID(null);
	}
	
	// Following properties are used for getting and setting data for adding or updating organizational units.
	private String name;
	private String customerID;

	public String getName() {
		return name;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
}
