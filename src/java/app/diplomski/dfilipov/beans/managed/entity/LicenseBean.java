package app.diplomski.dfilipov.beans.managed.entity;

import app.diplomski.dfilipov.beans.session.EntityMapper;
import app.diplomski.dfilipov.entities.License;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "licenseBean")
@ViewScoped
public class LicenseBean extends AbstractEntityBean<License> {
	
	@EJB private EntityMapper entityMapper;
	
	public LicenseBean() {
		super(License.class);
	}

	@Override
	protected EntityMapper entityMapper() {
		return entityMapper;
	}

	@Override
	protected License newEntity() {
		return new License();
	}

	@Override
	protected void populateEntity(License entity) {
		entity.setName(getName());
		entity.setDescription(getDescription());
	}

	@Override
	protected void setBeanData(License entity) {
		setName(entity.getName());
		setDescription(entity.getDescription());
	}

	@Override
	protected void clearBeanData() {
		setName(null);
		setDescription(null);
	}

	// Following properties are used for getting and setting data for adding or updating licenses.
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
