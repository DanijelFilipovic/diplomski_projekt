package app.diplomski.dfilipov.beans.managed.entity;

import app.diplomski.dfilipov.beans.session.EntityMapper;
import app.diplomski.dfilipov.entities.Job;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "jobBean")
@ViewScoped
public class JobBean extends AbstractEntityBean<Job> {

	@EJB private EntityMapper entityMapper;
	
	public JobBean() {
		super(Job.class);
	}

	@Override
	protected EntityMapper entityMapper() {
		return entityMapper;
	}

	@Override
	protected Job newEntity() {
		return new Job();
	}

	@Override
	protected void populateEntity(Job entity) {
		entity.setName(getName());
		entity.setDescription(getDescription());
	}

	@Override
	protected void setBeanData(Job entity) {
		setName(entity.getName());
		setDescription(entity.getDescription());
	}

	@Override
	protected void clearBeanData() {
		setName(null);
		setDescription(null);
	}
	
	// Following properties are used for getting and setting data for adding or updating jobs.
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
