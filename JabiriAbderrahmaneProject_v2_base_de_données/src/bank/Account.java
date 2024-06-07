// Data base version
package bank;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public abstract class Account {
	/*
	 * Dans cette version Account n'est pas nécessairement sérialisable.
	 * Notre processus de sauvegarde sera une base de données.
	 * Pour tester le mécanisme, il vous faudra créer une base de données avec les propriétés 
	 * spécifiés dans la classe ConnexionSQL ou changer les propriétés dans tout les codes par un ctrl+F et replace pour correspondre à votre propre base de données
	 * qui doit dans tout les cas avoir les mêmes propriétés (les mêmes colonnes et les mêmes types)
	 */
	private String number;
	private String name;
	private double balance;
	private List<Operation> operations;
	public Account(String name, double balance) {
		if(name == null) {
			System.out.println("Name :");
			var scanner = new Scanner(System.in);
			this.name  = scanner.next();
			scanner.close();
		}else this.name = name;
		this.balance = balance;
		//Java Unique Number Generator
		//Generates 16 bytes that contain hexadecimal values
		String [] st = java.util.UUID.randomUUID().toString().split("-");
		this.number = st[0].substring(4);

		//this.number = createUniqueNumber(6);

		operations = new LinkedList<>(); //easy to update
	}
	public Account() {
		this(null,0.0);
	}
	public Account(Account original) {
		if(original == null ) {
			System.out.println("Fatal Error.");
			System.exit(0);
		}
		this.name = original.name;
		this.balance = original.balance;
		this.number = original.number;
		this.operations = List.copyOf(original.operations);//java 10
	}
	public boolean add(Operation e) {
		return operations.add(e);
	}
	public boolean remove(Operation o) {
		return operations.remove(o);
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {

		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance){
		this.balance = balance;
	}
	public List<Operation> getOperations() {
		return operations;
	}
	@Override
	public String toString() {
		return String.format("IBAN=%s\t%s\tBalance=%.2f\t", number,name,balance);
	}
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj instanceof Account) {
			Account compte = (Account)obj;
			return (this.number == compte.number) && (this.name.equals(compte.name));
		}
		else return false;
	}
	/**
	 * Deposit an amount of money 
	 * @param amount to deposit
	 */
	public void deposit (final double amount) {
		if(amount <0) return;
		else {
			this.balance +=  amount;
			add(new Operation(Operation.DEPOSIT,amount,LocalDate.now()));
		} 
	}
	/**
	 * Withdraw an amount of money 
	 * @param amount
	 * @throws InsufficientFundException
	 * @throws NegativeAmountException 
	 */
	public void  withdraw(final double amount) throws InsufficientFundException {
		if(amount <0) return;
		if(amount > balance) {
			throw new InsufficientFundException(this,amount);
		}
		else {
			this.balance -= amount;
			add(new Operation(Operation.WITHDRAW,amount,LocalDate.now()));
		}
	}
	/**
	 * Transfer an amount of money to an other account
	 * @param amount to  transfer
	 * @param other	account
	 * @throws InsufficientFundException
	 */
	public void transfer(double amount, Account other) {
		//withdraw
		try {
			this.withdraw(amount);
		} catch (InsufficientFundException e) {
			System.out.println(e);
		}
		//... and deposit
		other.deposit(amount);
		
		return;
	}
	/**
	 *  Bank statement : from the past month
	 */
	public String bankStatement (){
		LocalDate fromLastMonth = LocalDate.now().minusMonths(1);
		return bankStatement (fromLastMonth);
	}
	/**
	 * Bank statement : from a date
	 * @param fromADate
	 * @return
	 */
	public String bankStatement (LocalDate fromADate){ // Bank statement
		String res = ""+this+"Operations ";
		for (Operation operation : operations) {
			if(operation.getDate().isAfter(fromADate))
				res += operation;
		}
		res+="\n";
		return res;
	}
}
