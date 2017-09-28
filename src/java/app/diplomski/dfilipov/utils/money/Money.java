package app.diplomski.dfilipov.utils.money;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Money implements Serializable {

	private BigDecimal amount;
	private String currency;
	
	public Money(BigDecimal amount, String currency) {
		this.amount = amount.setScale(2, RoundingMode.HALF_EVEN);
		this.currency = currency;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount.setScale(2, RoundingMode.HALF_EVEN);
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return amount.toString() + " " + currency;
	}
}
