package app.diplomski.dfilipov.utils.wrappers;

import app.diplomski.dfilipov.entities.Entity;
import java.io.Serializable;

public class EditableEntity<T extends Entity> implements Serializable {

	private final T entity;
	private boolean changeable;
	private boolean removable;
	
	public EditableEntity(T entity) {
		this.entity = entity;
		this.changeable = false;
		this.removable = false;
	}

	public T getEntity() {
		return entity;
	}

	public boolean isChangeable() {
		return changeable;
	}

	public boolean isRemovable() {
		return removable;
	}

	public void setChangeable(boolean changeable) {
		this.changeable = changeable;
	}

	public void setRemovable(boolean removable) {
		this.removable = removable;
	}
}
