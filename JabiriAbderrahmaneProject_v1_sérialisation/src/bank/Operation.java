/**
 * 
 */
package bank;

import java.io.Serializable; 
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Operation implements Serializable{
	 //Puisque les comptes vont être sérialisées il est nécessaire que la classe Operation soit aussi sérialisabe 
	 
	private static final long serialVersionUID = -3409649457456078079L;
	public static final String DEPOSIT = "DEPOSIT";
	public static final String WITHDRAW = "WITHDRAW";
	public static final String TRANSFER = "TRANSFER";
	//attributes
	private String type;
	private double amount;
	private LocalDate date;
	//constructor
	public Operation(String type, double amount, LocalDate date) {
		super();
		this.type = type;
		this.amount = amount;
		this.date = date;
	}
	//methods
	@Override
	public String toString() {
		String res = "\n "
				+ date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
				+"\t"+ type + String.format("\t%.1f", amount);
		return res;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null) 	return false;
		if (this == obj) 	return true;
		if(obj instanceof Operation ) {
			Operation operation = (Operation) obj;
			return (type == operation.type &&  
					amount == operation.amount && 
					date.equals(operation.date) );
		}else 	return false;
	}
	//accessors
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}


}
