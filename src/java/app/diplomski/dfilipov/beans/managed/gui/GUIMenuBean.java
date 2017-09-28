package app.diplomski.dfilipov.beans.managed.gui;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "guiMenu")
@SessionScoped
public final class GUIMenuBean {

	private boolean organizationMenuShowing;
	private boolean employeeMenuShowing;
	private boolean customerMenuShowing;
	
	public GUIMenuBean() {
		closeMenus();
	}

	public boolean isOrganizationMenuShowing() {
		return organizationMenuShowing;
	}

	public boolean isEmployeeMenuShowing() {
		return employeeMenuShowing;
	}

	public boolean isCustomerMenuShowing() {
		return customerMenuShowing;
	}
	
	public void showOrganizationMenu() {
		organizationMenuShowing = true;
		employeeMenuShowing = false;
		customerMenuShowing = false;
	}
	
	public void showEmployeeMenu() {
		organizationMenuShowing = false;
		employeeMenuShowing = true;
		customerMenuShowing = false;
	}
	
	public void showCustomerMenu() {
		organizationMenuShowing = false;
		employeeMenuShowing = false;
		customerMenuShowing = true;
	}
	
	public void closeMenus() {
		organizationMenuShowing = false;
		employeeMenuShowing = false;
		customerMenuShowing = false;
	}
	
}
