// Data base version
package bank;


/*Exception thrown if the current account balance is insufficient
*/

@SuppressWarnings("serial")
public class InsufficientFundException extends Exception {
	Account account;
	Double amount;
	//
	public InsufficientFundException(Account account, Double amount) {
		super();
		this.account = account;
		this.amount = amount;
	}
	@Override
	public String toString() {
		return 	String.format(
				"InsufficientFundException : credit limit exceeded (%s %.2f)", 
				account.getName(),amount);
	}
}
