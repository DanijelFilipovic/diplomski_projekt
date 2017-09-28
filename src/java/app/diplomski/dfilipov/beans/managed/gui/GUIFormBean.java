package app.diplomski.dfilipov.beans.managed.gui;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "guiForm")
@SessionScoped
public final class GUIFormBean {

	private boolean formShowing;
	
	public GUIFormBean() {
		closeForm();
	}

	public boolean isFormShowing() {
		return formShowing;
	}

	public void showForm() {
		formShowing = true;
	}
	
	public void closeForm() {
		formShowing = false;
	}	
}
