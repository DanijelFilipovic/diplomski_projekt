package app.diplomski.dfilipov.utils.time;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class CustomDate implements Serializable {

	private final LocalDate date;
	
	public CustomDate(String dateString) {
		this.date = LocalDate.parse(dateString, CUSTOM_DATE_FORMATTER);
	}
	
	public CustomDate(java.sql.Date dateSql) {
		this.date = dateSql.toLocalDate();
	}
	
	public java.sql.Date toSqlDate() {
		return java.sql.Date.valueOf(date);
	}

	@Override
	public String toString() {
		return date.format(CUSTOM_DATE_FORMATTER);
	}
	
	private static final DateTimeFormatter CUSTOM_DATE_FORMATTER;
	static {
		DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
		builder.appendValue(ChronoField.DAY_OF_MONTH, 2);
		builder.appendLiteral('.');
		builder.appendValue(ChronoField.MONTH_OF_YEAR, 2);
		builder.appendLiteral('.');
		builder.appendValue(ChronoField.YEAR);
		builder.appendLiteral('.');
		CUSTOM_DATE_FORMATTER = builder.toFormatter();
	}
}
