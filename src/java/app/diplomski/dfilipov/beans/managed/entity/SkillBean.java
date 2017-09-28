package app.diplomski.dfilipov.beans.managed.entity;

import app.diplomski.dfilipov.beans.session.EntityMapper;
import app.diplomski.dfilipov.entities.Skill;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "skillBean")
@ViewScoped
public class SkillBean extends AbstractEntityBean<Skill> {

	@EJB private EntityMapper entityMapper;
	
	public SkillBean() {
		super(Skill.class);
	}

	@Override
	protected EntityMapper entityMapper() {
		return entityMapper;
	}

	@Override
	protected Skill newEntity() {
		return new Skill();
	}

	@Override
	protected void populateEntity(Skill entity) {
		entity.setName(getName());
		entity.setDescription(getDescription());
	}

	@Override
	protected void setBeanData(Skill entity) {
		setName(entity.getName());
		setDescription(entity.getDescription());
	}

	@Override
	protected void clearBeanData() {
		setName(null);
		setDescription(null);
	}
	
	// Following properties are used for getting and setting data for adding or updating skills.
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
