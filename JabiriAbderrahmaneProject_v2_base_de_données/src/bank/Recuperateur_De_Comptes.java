// Cette classe a pour but de récupérer lors de son instanciation et en utilisant recuperer_all_accounts une liste de tout les comptes existants.
// database version 
package bank;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
public class Recuperateur_De_Comptes implements InterfaceDao{


	@Override
	public ArrayList<Account> recuperer_all_accounts(String NomTable) {
        List<List<Object>> all_accounts = new ArrayList<>();
        int columnCount = -1;
        try (Connection connection = ConnexionSQL.getConnection()) { 
        	/* utilisation de la méthode getConnexion() de la classe ConnexionSQL pour établir la connexion */
            String query = "SELECT * FROM " + NomTable;
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                columnCount = metaData.getColumnCount();
                while (resultSet.next()) {
                    List<Object> donnee = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        Object value = resultSet.getObject(i);
                        donnee.add(value);
                    }
                    all_accounts.add(donnee);
                }
            }/*
               l'opération faite dans ce bloc est la récupération de tout mes comptes de ma 
               base de données en tant que liste de listes d'objets */
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            ConnexionSQL.closeConnection();		//ici on ferme la connexion.
        }
        
        
        //// le nombre d'objets dans all_account ou le nombre de lignes de ma table
        int rowCount;
        rowCount = all_accounts.size();
       //ici un simple test pour visualiser au console cette liste de liste d'objets
       for (List<Object> account : all_accounts) {
            for (Object data : account) {
                System.out.print(data + " ");
                System.out.println(); // Saut de ligne après l'affichage de chaque élément qui correspond à une case de ma table 
            }
        }
        
        
        // jusque là all_accounts est une liste de liste où chaque liste contient les informations d'une opération 
        ArrayList<Account> tout_les_comptes = new ArrayList<>(); // c'est cette liste qui va etre utilisée après dans l'AdminFrame et CustomerFrame
        //on va ajouter ensuite à notre array liste tout les comptes qui existent mais cette fois il seront des objets Account. 
        
        ArrayList<String> mes_numeros_de_comptes = new ArrayList<>();  //cette liste contiendra tout les numéros de comptes distincts 
        mes_numeros_de_comptes.add(String.valueOf(all_accounts.get(0).get(0)));  // simple initialisation par le premier numero de compte dans ma base

        for (int j = 1; j < rowCount; j++) {					
        	// boucle pour chercher tout les numéros distincts des comptes
            String numeroCompte = String.valueOf(all_accounts.get(j).get(0));
            if (!mes_numeros_de_comptes.contains(numeroCompte)) {
                mes_numeros_de_comptes.add(numeroCompte);
            }
        }
       
        // on va ici traiter chaque numéro de compte à part, on va boucler sur chaque numéro de compte
        String Numero_en_traitement;
        String son_type_compte;
        String son_Nom_propriétaire;
        double sa_premiere_balance;
        
        for (int i = 0 ; i < mes_numeros_de_comptes.size() ; i++) { 
        	//pour le numero de compte d'indice i
			son_type_compte = null;   // simple initialisation de variable
			son_Nom_propriétaire = null;
			sa_premiere_balance = 0;  //celle à la création du compte, simple init.
			Numero_en_traitement = mes_numeros_de_comptes.get(i);  //le numéro de compte en traitement
			for (int j = 0; j < rowCount; j++) {
				if(Numero_en_traitement.equals(all_accounts.get(j).get(0))) {
					son_Nom_propriétaire =  String.valueOf(all_accounts.get(j).get(1));
					son_type_compte = String.valueOf(all_accounts.get(j).get(6));
					sa_premiere_balance = (double )all_accounts.get(j).get(2);
					break;
				}	
			} 
			/* Nous avons récupéré le num du compte, son type et sa première balance .
			 * L'idée maintenant est de recréer ce compte cette fois en tant qu'objet Account.
			 * à partir des informations dans la base de données.
			 * */
			if (son_type_compte.equals("rate %5")){
				SavingAccount Account_with_current_no = new SavingAccount(son_Nom_propriétaire, sa_premiere_balance, 0.05);
				Account_with_current_no.setNumber(Numero_en_traitement); //pour qu'il prenne le numero de compte correspandant dans la base 
				//on reproduit ensuite tout les opérations ayant été effectués sur notre compte
				for(int j = 0; j < rowCount; j++) {
					if(String.valueOf(all_accounts.get(j).get(3)).equals("WITHDRAW") && all_accounts.get(j).get(0).equals(Numero_en_traitement)) {    // ici !!!! il faut compter juste les withdraw du compte traité maintenant
						try {
							Account_with_current_no.withdraw((double) all_accounts.get(j).get(5));
							java.sql.Date sqlDate = (Date) all_accounts.get(j).get(4);

							LocalDate localdate = sqlDate.toLocalDate();
								// ce qu'on fait ici est de changer après avoir fait l'opération sa date pour correspondre à celle de la base de données						
							Account_with_current_no.getOperations().get(Account_with_current_no.getOperations().size()-1).setDate(localdate);
						} catch (NumberFormatException | InsufficientFundException e) {							
							InsufficientFundException y = new InsufficientFundException(Account_with_current_no,(Double) all_accounts.get(j).get(5));
							y.printStackTrace();
						}
					}
						// l'opreration withdraw est faite
				// passons maintenant à l'operation de depot
					if(String.valueOf(all_accounts.get(j).get(3)).equals("DEPOSIT") && all_accounts.get(j).get(0).equals(Numero_en_traitement)) {
						Account_with_current_no.deposit((double) all_accounts.get(j).get(5)/1.05);   // pour prendre en considération le premier montant deposé, pas celui affecté par le pourcentage de bénéfice 5%
						java.sql.Date sqlDate = (Date) all_accounts.get(j).get(4); // Votre objet java.sql.Date

						LocalDate localdate = sqlDate.toLocalDate();
							// ce qu'on fait ici est de changer après avoir fait l'opération sa date pour correspondre à celle de la base de données						
						Account_with_current_no.getOperations().get(Account_with_current_no.getOperations().size()-1).setDate(localdate);
					}				
				}
				// une fois restitué notre compte en tant qu'objet Account, ajoutons le dans notre arraylist.
				tout_les_comptes.add(Account_with_current_no);
				
			}//nous avons traité les comptes d'épargne, passons maintenant aux comptes courants
			else if(son_type_compte.equals("100 (authorized)")){
				CurrentAccount Account_with_current_no = new CurrentAccount(son_Nom_propriétaire, sa_premiere_balance, 100); //tjrs on a 100 comme credit limit par défault
				// on a creer maintenant son compte, effectuons sur lui maintenant tous les opérations qui ont été enregistrés
				
				Account_with_current_no.setNumber(Numero_en_traitement); //pour qu'il prenne le numero de compte correspandant dans la base 
				for(int j = 0; j < rowCount; j++) {
					if(String.valueOf(all_accounts.get(j).get(3)).equals("WITHDRAW") && all_accounts.get(j).get(0).equals(Numero_en_traitement)) {
						try {
							Account_with_current_no.withdraw((double) all_accounts.get(j).get(5));
							// il faut régler le problème des dates par la modification selon la date dans la base de donnée
							java.sql.Date sqlDate = (Date) all_accounts.get(j).get(4);

							LocalDate localdate = sqlDate.toLocalDate();
								// ce qu'on fait ici est de changer après avoir fait l'opération sa date pour correspondre à celle de la base de données						
							Account_with_current_no.getOperations().get(Account_with_current_no.getOperations().size()-1).setDate(localdate);
						} catch (NumberFormatException | InsufficientFundException e) {
							// TODO Auto-generated catch block
							InsufficientFundException y = new InsufficientFundException(Account_with_current_no,Double.parseDouble((String) all_accounts.get(j).get(5)));
							y.printStackTrace();
						}
					}
						// l'opreration withraw est faite
				// passons maintenant à l'operation de dépôt
					if(String.valueOf(all_accounts.get(j).get(3)).equals("DEPOSIT") && all_accounts.get(j).get(0).equals(Numero_en_traitement)) {
						Account_with_current_no.deposit((double) all_accounts.get(j).get(5));
						java.sql.Date sqlDate = (Date) all_accounts.get(j).get(4);

						LocalDate localdate = sqlDate.toLocalDate();
							// ce qu'on fait ici est de changer après avoir fait l'opération sa date pour correspondre à celle de la base de données						
						Account_with_current_no.getOperations().get(Account_with_current_no.getOperations().size()-1).setDate(localdate);
					}				
				}
				tout_les_comptes.add(Account_with_current_no); // le compte doit être ajouté
			}// on a traité ici les comptes courants
			
		}//ici termine notre boucle
		
		// maintenant on doit renvoyer notre array liste des comptes
		return tout_les_comptes;
	}	 
}
