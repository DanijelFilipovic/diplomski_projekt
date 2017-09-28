package app.diplomski.dfilipov.utils.time;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class CustomTime implements Serializable {
	
	private final LocalTime time;
	
	public CustomTime(String timeString) {
		this.time = LocalTime.parse(timeString);
	}
	
	public CustomTime(java.sql.Time timeSql) {
		this.time = timeSql.toLocalTime();
	}
	
	public java.sql.Time toSqlTime() {
		return java.sql.Time.valueOf(time);
	}

	@Override
	public String toString() {
		return time.format(CUSTOM_TIME_FORMATTER);
	}
	
	private static final DateTimeFormatter CUSTOM_TIME_FORMATTER;
	static {
		DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
		builder.appendValue(ChronoField.HOUR_OF_DAY, 2);
		builder.appendLiteral(':');
		builder.appendValue(ChronoField.MINUTE_OF_HOUR, 2);
		CUSTOM_TIME_FORMATTER = builder.toFormatter();
	}
}
