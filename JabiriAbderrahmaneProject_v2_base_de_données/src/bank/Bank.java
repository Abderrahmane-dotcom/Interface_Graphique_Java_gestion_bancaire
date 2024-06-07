// Data base version
package bank;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;



public class Bank { 
	private String name;
	private ArrayList<Account> accounts; 
	public static String FILENAME ="bank.dta";

	private static final Bank INSTANCE  = new Bank();
	private Bank(){
		System.out.println("Bank created (single instance)");
		accounts = new ArrayList<>();	
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Account> getAccounts() {
		return  accounts;
	}
	public static Bank getInstance() {
		return INSTANCE;
	}

	public void addAccount(Account e) {  
		 accounts.add(e);
	}

	
	public  void makeSimulation() {
		Random random = new Random();
		final int NBOFOPERATIONS = 10;// on va effectuer 10 opérations sur 5 comptes qu'on va créer
		//create 5 accounts
		this.accounts = new ArrayList<>(Arrays.asList(
			    new CurrentAccount("Anie LeRoy", 4000, 100),
			    new CurrentAccount("Karim Baazzi", 7000, 100),
			    new CurrentAccount("Paul Jacard", 5000, 100),
			    new SavingAccount("Nora Zouin", 6000, 0.05),
			    new SavingAccount("Omar Amir", 9000, 0.05)
			));
		//ici on va insérer chacun de ces comptes dans notre base de données
		accounts.forEach((account) -> {
				String jdbcUrl = "jdbc:mysql://localhost:3305/database_bank?serverTimezone=UTC";		// ma base de données
		        String username = "root";
		        String password = "abderrahmane2003";
		        
		        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
		            String insertQuery = "INSERT INTO bank_table (number, name, balance, operation, date, amount, details_of_account) " +
		                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

		            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
		                // Set values pour chaque parametre
		                preparedStatement.setString(1,account.getNumber()); // récupération du number du compte
		                preparedStatement.setString(2, account.getName()); // récupération du nom du proprétaire du compte
		                preparedStatement.setDouble(3, Math.round(account.getBalance()*100.0)/100.0); // récupération de son solde balance
		                preparedStatement.setString(4,"DEPOSIT"); // il est choisit par défault que l'opération de création de compte sera représentée par un deposit 0.00  
		                preparedStatement.setDate(5, Date.valueOf(LocalDate.now()));
		                preparedStatement.setDouble(6,0.00); //  C'est une création de compte, par défault, on la représentera par deposit 0.00
		                if
		                (account instanceof SavingAccount) {
		                	preparedStatement.setString(7, "rate %5"); 
		                }
		                else {preparedStatement.setString(7, "100 (authorized)");}

		                @SuppressWarnings("unused")
						int rowsAffected = preparedStatement.executeUpdate();
		                if (connection != null) {
		                    try {
		                        connection.close();	                        
		                    } catch (SQLException e) {
		                        e.printStackTrace();
		                    }
		                }
		            }
		        } catch (SQLException e1) {
		            e1.printStackTrace();
		        }
			;
		});
		/*
		 * Dans ce bloc, on va effectuer les 10 opérations, sur nos 5 comptes, et chaque opération va être
		 * enregistrée dans la base de donnée  
		 * */
		for (int i = 0; i < NBOFOPERATIONS; i++) {
			//get an account from accounts
			int i1 = random.nextInt(accounts.size());
			int i2 =0;
			for (int j = 0; j < accounts.size(); j++) {
				i2 = random.nextInt(accounts.size());
				if(i2 != i1) break;
			}
			var account1 = accounts.get(i1);  
			var account2 = accounts.get(i2);
			int operation = random.nextInt(4);
			double amount = random.nextDouble(1000.0);
			switch(operation){					//il faut à chaque opération faite insérer cette dernière dans notre base de données
			case 0: case 1: //DEPOSIT
				account1.deposit(amount);
				
				
				String jdbcUrl = "jdbc:mysql://localhost:3305/database_bank?serverTimezone=UTC";		// ma base de données
		        String username = "root";
		        String password = "abderrahmane2003";

		        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
		            String insertQuery = "INSERT INTO bank_table (number, name, balance, operation, date, amount, details_of_account) " +
		                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

		            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
		               
		                preparedStatement.setString(1,account1.getNumber()); 
		                preparedStatement.setString(2, account1.getName());
		                preparedStatement.setDouble(3, Math.round(account1.getBalance()*100.0)/100.0);
		                preparedStatement.setString(4,"DEPOSIT");
		                preparedStatement.setDate(5, Date.valueOf(LocalDate.now()));
		                preparedStatement.setDouble(6,Math.round(account1.getOperations().get(account1.getOperations().size() - 1).getAmount()*100.0)/100.0);//La valeur de la dernière opération
		                if
		                (account1 instanceof SavingAccount) {
		                	preparedStatement.setString(7, "rate %5"); 
		                }
		                else {preparedStatement.setString(7, "100 (authorized)");}

		                @SuppressWarnings("unused")
						int rowsAffected = preparedStatement.executeUpdate();
		                if (connection != null) {
		                    try {
		                        connection.close();	                        
		                    } catch (SQLException e) {
		                        e.printStackTrace();
		                    }
		                }
		            }
		        } catch (SQLException e1) {
		            e1.printStackTrace();
		        }
				
				break;
				
				
			case 2: //WITHDRAW
				try {
					account1.withdraw(amount);
					
					String jdbcUrl1 = "jdbc:mysql://localhost:3305/database_bank?serverTimezone=UTC";
			        String username1 = "root";
			        String password1 = "abderrahmane2003";

			        try (Connection connection = DriverManager.getConnection(jdbcUrl1, username1, password1)) {
			            String insertQuery = "INSERT INTO bank_table (number, name, balance, operation, date, amount, details_of_account) " +
			                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

			            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
			                
			                preparedStatement.setString(1,account1.getNumber()); 
			                preparedStatement.setString(2, account1.getName());
			                preparedStatement.setDouble(3, Math.round(account1.getBalance()*100.0)/100.0);
			                preparedStatement.setString(4,"WITHDRAW"); 
			                preparedStatement.setDate(5, Date.valueOf(LocalDate.now()));
			                preparedStatement.setDouble(6,Math.round(account1.getOperations().get(account1.getOperations().size() - 1).getAmount()*100.0)/100.0);
			                if
			                (account1 instanceof SavingAccount) {
			                	preparedStatement.setString(7, "rate %5"); 
			                }
			                else {preparedStatement.setString(7, "100 (authorized)");}

			                @SuppressWarnings("unused")
							int rowsAffected = preparedStatement.executeUpdate();
			                if (connection != null) {
			                    try {
			                        connection.close();	                        
			                    } catch (SQLException e) {
			                        e.printStackTrace();
			                    }
			                }

			            }
			        } catch (SQLException e1) {
			            e1.printStackTrace();
			        }					
					
					
					
				} catch (InsufficientFundException  e) {
					System.out.println(e);
				}
				break;
			
			
			
			case 3: //TRANSFER 
				account1.transfer(amount,account2);
				
				String jdbcUrl1 = "jdbc:mysql://localhost:3305/database_bank?serverTimezone=UTC";	
		        String username1 = "root";
		        String password1 = "abderrahmane2003";

		        try (Connection connection = DriverManager.getConnection(jdbcUrl1, username1, password1)) {
		            String insertQuery = "INSERT INTO bank_table (number, name, balance, operation, date, amount, details_of_account) " +
		                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

		            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
		                
		                preparedStatement.setString(1,account1.getNumber());
		                preparedStatement.setString(2, account1.getName());
		                preparedStatement.setDouble(3, Math.round(account1.getBalance()*100.0)/100.0);
		                preparedStatement.setString(4,"WITHDRAW");
		                preparedStatement.setDate(5, Date.valueOf(LocalDate.now()));
		                preparedStatement.setDouble(6,Math.round(account1.getOperations().get(account1.getOperations().size() - 1).getAmount()*100.0)/100.0);
		                if
		                (account1 instanceof SavingAccount) {
		                	preparedStatement.setString(7, "rate %5"); 
		                }
		                else {preparedStatement.setString(7, "100 (authorized)");}

		                @SuppressWarnings("unused")
						int rowsAffected = preparedStatement.executeUpdate();
		                if (connection != null) {
		                    try {
		                        connection.close();	                        
		                    } catch (SQLException e) {
		                        e.printStackTrace();
		                    }
		                }
		            }
		        } catch (SQLException e1) {
		            e1.printStackTrace();
		        }				
		        // la  connexion est toujours fonctionnelle, on va maintenant insérer la modification ds le compte destinataire 
		        try (Connection connection = DriverManager.getConnection(jdbcUrl1, username1, password1)) {
		            String insertQuery = "INSERT INTO bank_table (number, name, balance, operation, date, amount, details_of_account) " +
		                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

		            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
		          
		                preparedStatement.setString(1,account2.getNumber());
		                preparedStatement.setString(2, account2.getName());
		                preparedStatement.setDouble(3, Math.round(account2.getBalance()*100.0)/100.0);
		                preparedStatement.setString(4,"DEPOSIT");
		                preparedStatement.setDate(5, Date.valueOf(LocalDate.now()));
		                preparedStatement.setDouble(6,Math.round(account2.getOperations().get(account2.getOperations().size() - 1).getAmount()*100.0)/100.0); //la derniere valeur d'opération
		                if
		                (account1 instanceof SavingAccount) {
		                	preparedStatement.setString(7, "rate %5"); 
		                }
		                else {preparedStatement.setString(7, "100 (authorized)");}

		                @SuppressWarnings("unused")
						int rowsAffected = preparedStatement.executeUpdate();
		                if (connection != null) {
		                    try {
		                        connection.close();	                        
		                    } catch (SQLException e) {
		                        e.printStackTrace();
		                    }
		                }
		            }
		        } catch (SQLException e1) {
		            e1.printStackTrace();
		        }				
				
				
				break;
			default :
				throw new IllegalArgumentException("Operation not considered!");
			}
		}
	
	}// Ici finit le bloc simulation. 
	
	
	/**
	 * Looks for an account by number in the bank
	 * @param number
	 * @return
	 */
	public Account lookFor(String number) {
		Account account = null;
		for (Account c : accounts) {
			if(c.getNumber().equals(number)) {
				account = c; 
				break;
			}
		}
		return account;
	}

	@Override
	public String toString() {
		String str ="";
		for (Account c : accounts) {
			str += c; 
		}

		return this.getName()+"\n"+str;
	}


}
