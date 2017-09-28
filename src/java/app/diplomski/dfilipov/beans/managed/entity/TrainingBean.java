package app.diplomski.dfilipov.beans.managed.entity;

import app.diplomski.dfilipov.beans.session.EntityMapper;
import app.diplomski.dfilipov.entities.Training;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "trainingBean")
@ViewScoped
public class TrainingBean extends AbstractEntityBean<Training> {

	@EJB private EntityMapper entityMapper;
	
	public TrainingBean() {
		super(Training.class);
	}

	@Override
	protected EntityMapper entityMapper() {
		return entityMapper;
	}

	@Override
	protected Training newEntity() {
		return new Training();
	}

	@Override
	protected void populateEntity(Training entity) {
		entity.setName(getName());
		entity.setDescription(getDescription());
	}

	@Override
	protected void setBeanData(Training entity) {
		setName(entity.getName());
		setDescription(entity.getDescription());
	}

	@Override
	protected void clearBeanData() {
		setName(null);
		setDescription(null);
	}
	
	// Following properties are used for getting and setting data for adding or updating trainings.
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
