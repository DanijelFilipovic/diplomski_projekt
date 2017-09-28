package app.diplomski.dfilipov.beans.managed.entity;

import app.diplomski.dfilipov.beans.session.EntityMapper;
import app.diplomski.dfilipov.entities.Entity;
import app.diplomski.dfilipov.utils.wrappers.EditableEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEntityBean<T extends Entity> implements Serializable {
	
	protected Class<T> entityClass;
	protected boolean shouldFetchEntities;
	protected final List<EditableEntity<T>> editableEntities;
	
	public AbstractEntityBean(Class<T> entityClass) {
		this.entityClass = entityClass;
		this.shouldFetchEntities = true;
		this.editableEntities = new ArrayList<>();
	}

	public boolean isShouldFetchEntities() {
		return shouldFetchEntities;
	}

	public void setShouldFetchEntities(boolean shouldFetchEntities) {
		this.shouldFetchEntities = shouldFetchEntities;
	}
	
	public List<EditableEntity<T>> getEditableEntities() {
		if (shouldFetchEntities) {
			editableEntities.clear();
			entityMapper().findAll(entityClass).forEach((entity) -> editableEntities.add(new EditableEntity<>(entity)));
			shouldFetchEntities = false;
		}
		return editableEntities;
	}
	
	public void addEntity() {
		T entity = newEntity();
		populateEntity(entity);
		entityMapper().create(entityClass, entity);
		setShouldFetchEntities(true);
		clearBeanData();
	}
	
	public void updateEntity(EditableEntity<T> editableEntity) {
		T entity = editableEntity.getEntity();
		populateEntity(entity);
		entityMapper().update(entityClass, entity);
		setShouldFetchEntities(true);
		clearBeanData();
	}
	
	public void deleteEntities() {
		editableEntities.stream()
			.filter((ee) -> ee.isRemovable())
			.forEach((ee) -> entityMapper().delete(entityClass, ee.getEntity()));
		setShouldFetchEntities(true);
	}
	
	public void enableEditing(EditableEntity<T> editableEntity) {
		editableEntities.forEach((ee) -> ee.setChangeable(false));
		editableEntity.setChangeable(true);
		setBeanData(editableEntity.getEntity());
	}
	
	protected abstract EntityMapper entityMapper();
	protected abstract T newEntity();
	protected abstract void populateEntity(T entity);
	protected abstract void setBeanData(T entity);
	protected abstract void clearBeanData();
}
