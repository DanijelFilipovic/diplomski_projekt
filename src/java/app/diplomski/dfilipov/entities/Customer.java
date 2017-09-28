package app.diplomski.dfilipov.entities;

public class Customer extends Entity {

	private String name;
	private String address;
	private String emailAddress;
	private String phoneNumber;

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
