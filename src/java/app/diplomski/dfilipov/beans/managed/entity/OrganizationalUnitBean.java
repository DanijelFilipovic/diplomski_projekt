package app.diplomski.dfilipov.beans.managed.entity;

import app.diplomski.dfilipov.beans.session.EntityMapper;
import app.diplomski.dfilipov.entities.OrganizationalUnit;
import app.diplomski.dfilipov.utils.wrappers.EditableEntity;
import java.util.StringJoiner;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "orgUnitBean")
@ViewScoped
public class OrganizationalUnitBean extends AbstractEntityBean<OrganizationalUnit> {

	@EJB EntityMapper entityMapper;
	
	public OrganizationalUnitBean() {
		super(OrganizationalUnit.class);
	}
	
	public String printSubordinates(EditableEntity<OrganizationalUnit> editableOrganizationalUnit) {
		StringJoiner sj = new StringJoiner(", ");
		editableOrganizationalUnit.getEntity().getSubordinates().forEach((sub) -> sj.add(sub.getName()));
		return sj.toString();
	}
	
	public boolean equalbyId(EditableEntity<OrganizationalUnit> eou1, EditableEntity<OrganizationalUnit> eou2) {
		boolean result = eou1.getEntity().getId() == eou2.getEntity().getId();
		System.out.println(">> " + result + " <<");
		return result;
	}

	@Override
	protected EntityMapper entityMapper() {
		return entityMapper;
	}

	@Override
	protected OrganizationalUnit newEntity() {
		return new OrganizationalUnit();
	}

	@Override
	protected void populateEntity(OrganizationalUnit entity) {
		entity.setName(getName());
		entity.setDescription(getDescription());
		if (!getSuperordinateID().equals("0")) {
			long _superordinateID = Long.parseLong(getSuperordinateID());
			entity.setSuperordinate(entityMapper.find(OrganizationalUnit.class, _superordinateID));
		} else {
			entity.setSuperordinate(null);
		}
	}

	@Override
	protected void setBeanData(OrganizationalUnit entity) {
		setName(entity.getName());
		setDescription(entity.getDescription());
		if (entity.getSuperordinate() != null) {
			long _superordinateID = entity.getSuperordinate().getId();
			setSuperordinateID(Long.toString(_superordinateID));
		} else {
			setSuperordinateID(null);
		}
	}

	@Override
	protected void clearBeanData() {
		setName(null);
		setDescription(null);
		setSuperordinateID(null);
	}
	
	// Following properties are used for getting and setting data for adding or updating organizational units.
	private String name;
	private String description;
	private String superordinateID;

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getSuperordinateID() {
		return superordinateID;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setSuperordinateID(String superordinateID) {
		this.superordinateID = superordinateID;
	}
}
