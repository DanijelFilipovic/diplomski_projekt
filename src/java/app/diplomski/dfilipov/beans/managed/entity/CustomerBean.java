package app.diplomski.dfilipov.beans.managed.entity;

import app.diplomski.dfilipov.beans.session.EntityMapper;
import app.diplomski.dfilipov.entities.Customer;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "customerBean")
@ViewScoped
public class CustomerBean extends AbstractEntityBean<Customer> {

	@EJB private EntityMapper entityMapper;
	
	public CustomerBean() {
		super(Customer.class);
	}
	
	@Override
	protected EntityMapper entityMapper() {
		return entityMapper;
	}

	@Override
	protected Customer newEntity() {
		return new Customer();
	}

	@Override
	protected void populateEntity(Customer entity) {
		entity.setName(getName());
		entity.setAddress(getAddress());
		entity.setEmailAddress(getEmailAddress());
		entity.setPhoneNumber(getPhoneNumber());
	}

	@Override
	protected void setBeanData(Customer entity) {
		setName(entity.getName());
		setAddress(entity.getAddress());
		setEmailAddress(entity.getEmailAddress());
		setPhoneNumber(entity.getPhoneNumber());
	}

	@Override
	protected void clearBeanData() {
		setName(null);
		setAddress(null);
		setEmailAddress(null);
		setPhoneNumber(null);
	}
	
	// Following properties are used for getting and setting data for adding or updating customers.
	private String name;
	private String address;
	private String emailAddress;
	private String phoneNumber;

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
