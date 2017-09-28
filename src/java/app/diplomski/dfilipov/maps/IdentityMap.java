package app.diplomski.dfilipov.maps;

import app.diplomski.dfilipov.entities.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class IdentityMap<T extends Entity> {
	
	protected Map<Long, T> entities = new HashMap<>();
	
	public void add(T entity) {
		entities.put(entity.getId(), entity);
	}
	
	public T get(long id) {
		return entities.get(id);
	}
	
	public void replace(long id, T entity) {
		entities.replace(id, entity);
	}
	
	public void remove(long id) {
		entities.remove(id);
	}
	
	public boolean contains(long id) {
		return entities.containsKey(id);
	}
	
	public void clear() {
		entities.clear();
	}
	
	public List<T> filter(Predicate<T> predicate) {
		List<T> filteredEntities = new ArrayList<>();
		entities.forEach((id, entity) -> {
			if (predicate.test(entity)) {
				filteredEntities.add(entity);
			}
		});
		return filteredEntities;
	}
}
