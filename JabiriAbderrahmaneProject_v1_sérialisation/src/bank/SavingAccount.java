package bank;
/*
 * - We can get an interest, but
 * - We can't withdraw  money until the end of the term (1 year)
*/

@SuppressWarnings("serial")
public class SavingAccount extends Account {
	private double rate=0.05;// on va fixer l'interêt à 5% 
	//constructors .... are not inherited !
	public SavingAccount(String name, double balance, double rate) {
		super(name, balance);
		this.rate = rate;
	}
	public SavingAccount(String name, double balance) {
		this(name, balance,0.0);
	}
	public SavingAccount(String name) {
		this(name, 0.0,0.0);
	}
	@Override
	public String toString() {
		return super.toString() +"(rate="+rate*100+"%)\n";
	}
	@Override
	/**
	 * Deposit an amount of money 
	 * @param amount to  deposit 
	 */
	public void deposit (final double amount){
		super.deposit(amount*(1+rate));
	}
}
