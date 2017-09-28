package app.diplomski.dfilipov.entities;

public class Project extends Entity {
	
	private String name;
	private Customer customer;

	public String getName() {
		return name;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
