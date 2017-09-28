package app.diplomski.dfilipov.entities;

import java.io.Serializable;

public abstract class Entity implements Serializable {
	
	protected long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean equals(Entity entity) {
		return this.id == entity.id;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Entity && equals((Entity) obj);
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
		return hash;
	}	
}
