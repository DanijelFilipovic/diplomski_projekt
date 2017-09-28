package app.diplomski.dfilipov.utils.wrappers;

import app.diplomski.dfilipov.entities.License;
import app.diplomski.dfilipov.utils.time.CustomDate;
import java.io.Serializable;

public class AcquiredLicense implements Serializable {
	
	private License license;
	private CustomDate acquisitionDate;

	public License getLicense() {
		return license;
	}

	public CustomDate getAcquisitionDate() {
		return acquisitionDate;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	public void setAcquisitionDate(CustomDate acquisitionDate) {
		this.acquisitionDate = acquisitionDate;
	}
}
