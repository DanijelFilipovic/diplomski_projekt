package app.diplomski.dfilipov.beans.managed.gui;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "guiProfile")
@SessionScoped
public final class GUIProfileBean {

	private boolean profilePanelVisible;
	private boolean skillsPanelVisible;
	private boolean licensesPanelVisible;
	private boolean trainingPanelVisible;
	private boolean projectsPanelVisible;
	
	public GUIProfileBean() {
		showProfilePanel();
	}

	public boolean isProfilePanelVisible() {
		return profilePanelVisible;
	}

	public boolean isSkillsPanelVisible() {
		return skillsPanelVisible;
	}

	public boolean isLicensesPanelVisible() {
		return licensesPanelVisible;
	}

	public boolean isTrainingPanelVisible() {
		return trainingPanelVisible;
	}

	public boolean isProjectsPanelVisible() {
		return projectsPanelVisible;
	}
	
	public void showProfilePanel() {
		profilePanelVisible = true;
		skillsPanelVisible = false;
		licensesPanelVisible = false;
		trainingPanelVisible = false;
		projectsPanelVisible = false;
	}
	
	public void showSkillsPanel() {
		profilePanelVisible = false;
		skillsPanelVisible = true;
		licensesPanelVisible = false;
		trainingPanelVisible = false;
		projectsPanelVisible = false;
	}
	
	public void showLicensesPanel() {
		profilePanelVisible = false;
		skillsPanelVisible = false;
		licensesPanelVisible = true;
		trainingPanelVisible = false;
		projectsPanelVisible = false;
	}
	
	public void showTrainingPanel() {
		profilePanelVisible = false;
		skillsPanelVisible = false;
		licensesPanelVisible = false;
		trainingPanelVisible = true;
		projectsPanelVisible = false;
	}
	
	public void showProjectsPanel() {
		profilePanelVisible = false;
		skillsPanelVisible = false;
		licensesPanelVisible = false;
		trainingPanelVisible = false;
		projectsPanelVisible = true;
	}
}
