package bank;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Bank {//Singleton with static factory 
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

	/*
	 * La fonction save ici, lors de son appel, sauvegarde les comptes existants dans la liste accounts dans
	 * le fichier bank.dta sans écraser son contenu. 
	 * Si le fichier n'existe pas au préalable, l'instanciation FileOutputStream(fileName) va le créer
	 *
	*/
	public void save(String fileName) {
	    try {
	        ArrayList<Account> existingAccounts = new ArrayList<>();

	        // Vérifier si le fichier existe
	        File file = new File(fileName);
	        if (file.exists()) {
	          // Charger les données existantes depuis le fichier en utilisant la fonction load
	            existingAccounts = load(fileName);
	        }

	        // Ajouter les nouvelles données au tableau existant
	        existingAccounts.addAll(accounts);

	        // Écrire l'ensemble des données (anciennes + nouvelles) dans le fichier
	        OutputStream fos = new BufferedOutputStream(new FileOutputStream(fileName));	
	        ObjectOutputStream oos = new ObjectOutputStream(fos);
	        oos.writeObject(existingAccounts);
	        oos.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	/* cette nouvelle fonction save, au contraire de la première écrase le 
	*  contenu initial du fichier et lui ajoute la liste passée en paramètres 
	*/
	
	public  void save_avec_ecrasement(String fileName,ArrayList<Account> nouvelle_liste) {
		try {
			OutputStream fos = new BufferedOutputStream(
					new FileOutputStream(fileName));
			var oos = new ObjectOutputStream (fos);
			oos.writeObject(nouvelle_liste);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	/*Fonction pour récupérer les comptes à partir du bank.dta.*/
	public ArrayList<Account> load(String fileName) {
	    ArrayList<Account> loadedAccounts = new ArrayList<>();

	    try {
	        File file = new File(fileName);
	        if (file.length() == 0) {
	            System.out.println("Le fichier est vide.");
	            return loadedAccounts;   	/*Si le fichier est vide ou n'existe pas 
	             							  on retourne une liste vide.	*/
	        }

	        InputStream fis = new BufferedInputStream(new FileInputStream(fileName));
	        ObjectInputStream ois = new ObjectInputStream(fis);
	        loadedAccounts = (ArrayList<Account>) ois.readObject();
	        ois.close();
	    } catch (IOException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }

	    return loadedAccounts;
	}

	 
	
	public  void makeSimulation() {
		Random random = new Random();
		final int NBOFOPERATIONS = 10;  // on va effecter 10 opérations sur nos 5 comptes
		//create 5 accounts
		this.accounts = new ArrayList<>(Arrays.asList(
			    new CurrentAccount("Anie LeRoy", 4000, 500),
			    new CurrentAccount("Karim Baazzi", 7000, 100),
			    new CurrentAccount("Paul Jacard", 5000, 100),
			    new SavingAccount("Nora Zouin", 6000, 0.05),
			    new SavingAccount("Omar Amir", 9000, 0.05)
			));

		for (int i = 0; i < NBOFOPERATIONS; i++) {
			//get an account from accounts
			int i1 = random.nextInt(accounts.size()); // on choisit un indice aléatoire
			int i2 =0;
			for (int j = 0; j < accounts.size(); j++) {
				i2 = random.nextInt(accounts.size());
				if(i2 != i1) break;			 // on s'assure qu'on a un nouvel indice aléatoire différent du premier
			}
			var account1 = accounts.get(i1);  
			var account2 = accounts.get(i2);
			int operation = random.nextInt(4);
			double amount = random.nextDouble(1000.0);
			switch(operation){
			case 0: case 1: //DEPOSIT
				account1.deposit(amount);
				break;
			case 2: //WITHDRAW
				try {
					account1.withdraw(amount);
				} catch (InsufficientFundException  e) {
					System.out.println(e);
				}
				break;
			case 3: //TRANSFER 
				account1.transfer(amount,account2);
				break;
			default :
				throw new IllegalArgumentException("Operation not considered!");
			}
		}
	
	}
	

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
