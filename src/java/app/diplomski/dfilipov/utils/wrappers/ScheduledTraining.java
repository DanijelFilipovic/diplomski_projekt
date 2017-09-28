package app.diplomski.dfilipov.utils.wrappers;

import app.diplomski.dfilipov.entities.Training;
import app.diplomski.dfilipov.utils.time.CustomDate;
import java.io.Serializable;

public class ScheduledTraining implements Serializable {
	
	private Training training;
	private CustomDate startDate;
	private CustomDate endDate;

	public Training getTraining() {
		return training;
	}

	public CustomDate getStartDate() {
		return startDate;
	}

	public CustomDate getEndDate() {
		return endDate;
	}

	public void setTraining(Training training) {
		this.training = training;
	}

	public void setStartDate(CustomDate startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(CustomDate endDate) {
		this.endDate = endDate;
	}
}
