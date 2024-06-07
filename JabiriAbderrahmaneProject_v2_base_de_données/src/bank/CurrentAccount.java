// Data base version
package bank;
/*
 * You can withdraw money even when the balance is negative 
 * and at the limit of the authorized overdraft (creditLimit)
*/

public class CurrentAccount extends Account {
	private double creditLimit; //authorized overdraft
	//constructors .... are not inherited !
	public CurrentAccount(String name, double balance, double creditLimit) {
		super(name,balance);
		this.creditLimit=creditLimit;
	}
	public CurrentAccount(String name, double balance) {
		this(name,balance,0.0);
	}
	public CurrentAccount(String name) {
		this(name,0.0);
	}
	@Override
	public String toString() {
		return super.toString() +"("+creditLimit+" authorized)\n";
	}
	
	public double getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(double creditLimit) {
		this.creditLimit = creditLimit;
	}
	/**
	 * Withdraw money from the account without exceeding 
	 * the authorized overdraft (creditLimit)
	 * @param amount to  withdraw 
	 */
	@Override	
	public void  withdraw(final double amount) throws InsufficientFundException {
		if (amount <= getBalance() + creditLimit) {
			super.withdraw(amount);
		} else {
			throw new InsufficientFundException(this,amount);
		}
	}
}
