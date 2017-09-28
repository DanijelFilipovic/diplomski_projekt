package app.diplomski.dfilipov.entities;

import app.diplomski.dfilipov.beans.session.EntityMapper;
import app.diplomski.dfilipov.mappers.OrganizationalUnitMapper;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class OrganizationalUnit extends Entity {

	private final EntityMapper entityMapper;
	
	private String name;
	private String description;
	private OrganizationalUnit superordinate;
	private List<OrganizationalUnit> subordinates;

	public OrganizationalUnit() {
		this.entityMapper = lookupEntityMapperBean();
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public OrganizationalUnit getSuperordinate() {
		return superordinate;
	}

	public List<OrganizationalUnit> getSubordinates() {
		if (subordinates == null) {
			((OrganizationalUnitMapper) entityMapper.getMapper(getClass())).loadSubordinates(this);
		}
		return subordinates;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setSuperordinate(OrganizationalUnit superordinate) {
		this.superordinate = superordinate;
	}

	public void setSubordinates(List<OrganizationalUnit> subordinates) {
		this.subordinates = subordinates;
	}

	private EntityMapper lookupEntityMapperBean() {
		try {
			Context c = new InitialContext();
			return (EntityMapper) c.lookup("java:global/dfilipov_diplomski_app/EntityMapper!app.diplomski.dfilipov.beans.session.EntityMapper");
		} catch (NamingException ne) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
			throw new RuntimeException(ne);
		}
	}
}
