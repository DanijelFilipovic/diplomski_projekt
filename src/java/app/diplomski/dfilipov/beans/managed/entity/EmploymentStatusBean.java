package app.diplomski.dfilipov.beans.managed.entity;

import app.diplomski.dfilipov.beans.session.EntityMapper;
import app.diplomski.dfilipov.entities.EmploymentStatus;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "employmentStatusBean")
@ViewScoped
public class EmploymentStatusBean extends AbstractEntityBean<EmploymentStatus> {

	@EJB private EntityMapper entityMapper;

	public EmploymentStatusBean() {
		super(EmploymentStatus.class);
	}

	@Override
	protected EntityMapper entityMapper() {
		return entityMapper;
	}

	@Override
	protected EmploymentStatus newEntity() {
		return new EmploymentStatus();
	}

	@Override
	protected void populateEntity(EmploymentStatus entity) {
		entity.setName(getName());
		entity.setDescription(getDescription());
	}

	@Override
	protected void setBeanData(EmploymentStatus entity) {
		setName(entity.getName());
		setDescription(entity.getDescription());
	}

	@Override
	protected void clearBeanData() {
		setName(null);
		setDescription(null);
	}
		
	// Following properties are used for getting and setting data for adding or updating employment statuses.
	private String name;
	private String description;

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
