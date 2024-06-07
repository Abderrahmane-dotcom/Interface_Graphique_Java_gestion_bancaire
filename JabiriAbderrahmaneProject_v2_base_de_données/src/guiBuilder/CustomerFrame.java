// Data base version
package guiBuilder;

import javax.swing.JScrollPane;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.LineBorder;

import bank.Account;
import bank.Bank;
import bank.ConnexionSQL;
import bank.CurrentAccount;
import bank.InsufficientFundException;
import bank.Operation;
import bank.Recuperateur_De_Comptes;
import bank.SavingAccount;

import javax.swing.JComboBox;


@SuppressWarnings({ "serial", "unused" })
public class CustomerFrame extends JFrame {

	private JPanel contentPane;
	private JTextField noField;
	private final JTextField Amount_Field = new JTextField();
	private final JLabel lblEntre = new JLabel("Entre an amount, please");
	private final JLabel lblSelectAnOperation = new JLabel("Select an operation, please");
	
	//declarations
	String newoperation;
	String number;
	String inputamount;
	double amount_of_operation;
	int indice_account ; 

	/**
	 * Create the frame.
	 */
	public CustomerFrame(Bank bank) {
		
		addWindowListener(new WindowAdapter() {//ce WindowListener permet de fermer l'application une fois que l'utilisateur sort de la fenêtre 

	        @Override
	        public void windowClosing(WindowEvent e) {
	            dispose();
	            JOptionPane.showMessageDialog(null, "données sauvegardés dans la base de données");
	            System.exit(0); 
	        }
	    });
		
		bank.setName(bank.getName());
		
		setBounds(100, 100, 896, 359);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("SimBank(Costumer)");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel.setBounds(168, 11, 160, 32);
		contentPane.add(lblNewLabel);
		
		JTextArea textArea = new JTextArea();
		textArea.setBackground(Color.WHITE); // Définir l'arrière-plan en blanc
		textArea.setBounds(360, 76, 512, 235);
		contentPane.add(textArea);
		
		// ajout du scroll pane pour pouvoir visualiser les elements si on en insère beaucoup
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(358, 71, 518, 241);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		contentPane.add(scrollPane);
		scrollPane.setViewportView(textArea);
		
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(30, 144, 255))); // Encadrer le panel avec une ligne bleue fine
		panel.setBounds(358, 71, 518, 241);
		contentPane.add(panel);
		
		JLabel lblNewLabel_1 = new JLabel("Statement");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 15));
		lblNewLabel_1.setBounds(372, 47, 96, 24);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Account");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 15));
		lblNewLabel_2.setBounds(39, 47, 96, 24);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Enter your account No");
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.BOLD, 15));
		lblNewLabel_3.setBounds(60, 82, 160, 32);
		contentPane.add(lblNewLabel_3);
		
		noField = new JTextField(); 
		noField.setBounds(231, 86, 97, 24);
		contentPane.add(noField);
		noField.setColumns(10);
		
		Recuperateur_De_Comptes Récupérateur = new Recuperateur_De_Comptes();
		
		/*ici on récupére tout les comptes existants de notre base de données, l'historique des 
		 *opérations de chaque compte est intégralement stockée dans chaque Account dans sa liste operations 
		 **/
		ArrayList<Account> tout_les_comptes; 
		tout_les_comptes = Récupérateur.recuperer_all_accounts("bank_table"); // arraylist qui contient tout les comptes 

				
		
		newoperation = "no operation done";
		// le combo box
		JComboBox<String> comboBox = new JComboBox<>();
		comboBox.setModel(new DefaultComboBoxModel<String>(
				new String[] {"","DEPOSIT", "WITHDRAW","TRANSFER"})
				);
		comboBox.setBounds(231, 250, 95, 24);
		
		comboBox.addActionListener((ActionListener) new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        newoperation = (String) comboBox.getSelectedItem();
		        
		        
		        if(newoperation.equals("DEPOSIT")) { // si l'opération est égale à "DEPOSIT"
		            number = noField.getText();
		            inputamount = Amount_Field.getText();
		            amount_of_operation = Double.parseDouble(inputamount);
		            System.out.println(amount_of_operation);
		            //on doit chercher si le compte est bien existant
		            indice_account = -1;
		            for (int i = 0; i < tout_les_comptes.size(); i++) {
		                if (tout_les_comptes.get(i).getNumber().equals(number)) {
		                    indice_account = i; // Compte trouvé
		                    break;
		                }
		            }
		            if (indice_account == -1) { // compte non trouvé
		                textArea.setText("inexisting account, impossible operation");
		            } else {
		            	tout_les_comptes.get(indice_account).deposit(amount_of_operation);
		                JOptionPane.showMessageDialog(null, "operation done ");
		                textArea.setText(tout_les_comptes.get(indice_account).bankStatement()); // on affiche ses informations
		                comboBox.setSelectedIndex(0);
		            }
		            // maintenant je vais ajouter les modifications dans ma base de donnée,ici on a une opération de dépôt
		            
		            String url = "jdbc:mysql://localhost:3305/database_bank?serverTimezone=UTC";
		            String username = "root";
		            String password = "abderrahmane2003";

		            Connection connection = null;
		            try {
		                // Établir une connexion à la base de données
		                connection = DriverManager.getConnection(url, username, password);

		                String query = "INSERT INTO bank_table (number, name, balance, operation, date, amount, details_of_account) VALUES (?, ?, ?, ?, ?, ?, ?)";
		                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
		                    preparedStatement.setString(1, tout_les_comptes.get(indice_account).getNumber());
		                    preparedStatement.setString(2, tout_les_comptes.get(indice_account).getName());
		                    preparedStatement.setDouble(3, Math.round(tout_les_comptes.get(indice_account).getBalance()*100.0)/100.0);// pour ne garder que 2 chiffres après le virgule
		                    preparedStatement.setString(4, "DEPOSIT");
		                    LocalDate localDate = tout_les_comptes.get(indice_account).getOperations().get(tout_les_comptes.get(indice_account).getOperations().size() - 1).getDate();
		                    java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
		                    preparedStatement.setDate(5, sqlDate);
		                    preparedStatement.setDouble(6, Math.round(tout_les_comptes.get(indice_account).getOperations().get(tout_les_comptes.get(indice_account).getOperations().size() - 1).getAmount()*100.0)/100.0);
		                    if(tout_les_comptes.get(indice_account) instanceof CurrentAccount) {              
		                    preparedStatement.setString(7, "100 (authorized)");}
		                    else if(tout_les_comptes.get(indice_account) instanceof SavingAccount) {              
			                    preparedStatement.setString(7, "rate %5");}
		                    
		                    
		                    int rowsAffected = preparedStatement.executeUpdate();

		                    System.out.println(rowsAffected + " ligne insérée avec succès.");
		                }
		            } catch (SQLException e1) {
		                e1.printStackTrace();
		            } finally {
		                // Fermer la connexion à la base de données
		                if (connection != null) {
		                    try {
		                        connection.close();
		                    } catch (SQLException e2) {
		                        e2.printStackTrace();
		                    }
		                }
		            }
		            // la partie du deposit est finie
		        }
		        
		        
				//l'operation est "WITHDRAW"
		        else if(newoperation.equals("WITHDRAW") && !newoperation.equals("no operation done")) {
					number = noField.getText();
					inputamount = Amount_Field.getText();
					amount_of_operation = Double.parseDouble(inputamount);
					System.out.println(amount_of_operation);
					
					indice_account = -1;
					for (int i = 0; i < tout_les_comptes.size(); i++) {
					        if (tout_les_comptes.get(i).getNumber().equals(number)) {
					            indice_account = i; // Compte trouvé
					            break;
					        }
					    }
					if (indice_account == -1) {  // compte non trouvé
						textArea.setText("inexisting account, impossible operation");
						}
					else {
						int test = 0;
						try {
							tout_les_comptes.get(indice_account).withdraw(amount_of_operation);
						} catch (InsufficientFundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null, "impossible operation : insufficient fund ");
							test = 1;  // pour tester si on a passé par l'exception ou pas
						}
					if(test==0) {		//si on a pas passé par l'exception InsufficientFundException
						JOptionPane.showMessageDialog(null, "operation done ");
						// ce n'est que si on ne passe pas par l'exception qu'on doit effectuer la modification dans notre base de données
			            String url = "jdbc:mysql://localhost:3305/database_bank?serverTimezone=UTC";
			            String username = "root";
			            String password = "abderrahmane2003";

			            Connection connection = null;
			            try {
			                // Établir une connexion à la base de données
			                connection = DriverManager.getConnection(url, username, password);

			                String query = "INSERT INTO bank_table (number, name, balance, operation, date, amount, details_of_account) VALUES (?, ?, ?, ?, ?, ?, ?)";
			                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			                    preparedStatement.setString(1, tout_les_comptes.get(indice_account).getNumber());
			                    preparedStatement.setString(2, tout_les_comptes.get(indice_account).getName());
			                    preparedStatement.setDouble(3, Math.round(tout_les_comptes.get(indice_account).getBalance()*100.0)/100.0);// pour ne garder que 2 chiffres après le virgule
			                    preparedStatement.setString(4, "WITHDRAW");
			                    LocalDate localDate = tout_les_comptes.get(indice_account).getOperations().get(tout_les_comptes.get(indice_account).getOperations().size() - 1).getDate();
			                    java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
			                    preparedStatement.setDate(5, sqlDate);
			                    preparedStatement.setDouble(6, Math.round(tout_les_comptes.get(indice_account).getOperations().get(tout_les_comptes.get(indice_account).getOperations().size() - 1).getAmount()*100.0)/100.0);// pour ne garder que 2 chiffres après le virgule
			                    if(tout_les_comptes.get(indice_account) instanceof CurrentAccount) {              
			                    preparedStatement.setString(7, "100 (authorized)");}
			                    else if(tout_les_comptes.get(indice_account) instanceof SavingAccount) {              
				                    preparedStatement.setString(7, "rate %5");}
			                    
			                    
			                    int rowsAffected = preparedStatement.executeUpdate();

			                    System.out.println(rowsAffected + " ligne insérée avec succès.");
			                }
			            } catch (SQLException e1) {
			                e1.printStackTrace();
			            } finally {
			                // Fermer la connexion à la base de données
			                if (connection != null) {
			                    try {
			                        connection.close();
			                    } catch (SQLException e2) {
			                        e2.printStackTrace();
			                    }
			                }
			            }
					
					}
						textArea.setText(tout_les_comptes.get(indice_account).bankStatement()); // on affiche ses informations
						newoperation = "no operation done";
						comboBox.setSelectedIndex(0);//pour le comboBox ne selectionne plus aucune opération
					}
					
		          }// l'opération du withdraw est traitée
		        
		        
		        
		        //lorseque l'opération est transfer	
		        //nous allons dans ce cas ajouter une nouvelle petite fenêtre pour prendre en considération le compte de reception
		        else if(newoperation.equals("TRANSFER") && !newoperation.equals("no operation done")) {
		        	/**/
		        	number = noField.getText();
					inputamount = Amount_Field.getText();
					amount_of_operation = Double.parseDouble(inputamount);
		  
		        	// Création de la nouvelle JFrame pour le transfert
		        	JFrame transferFrame = new JFrame("Transfer");
		        	transferFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		        	// Définir la taille de la fenêtre à 5 cm * 4 cm
		        	int width = 5 * 80; // 1 cm = 30 pixels
		        	int height = 4 * 40;
		        	transferFrame.setSize(width, height);

		        	// Calculer la position au milieu de l'écran
		        	int screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		        	int screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
		        	int x = (screenWidth - width) / 2;
		        	int y = (screenHeight - height) / 2;

		        	// Définir la position de la fenêtre
		        	transferFrame.setLocation(x, y);

		        	// Création des composants pour le transfert
		        	JLabel recipientLabel = new JLabel("Recipient Account:");
		        	recipientLabel.setBounds(20, 20, 150, 30);

		        	JTextField recipientField = new JTextField();
		        	recipientField.setBounds(180, 20, 200, 30);

		        	JButton okButton = new JButton("TRANSFERT");
		        	okButton.setBounds(180, 70, 130, 30);
		        	okButton.addActionListener(new ActionListener() {
		        	    public void actionPerformed(ActionEvent e) {
		        	       
		        	        String recipient = recipientField.getText(); //recuperation du compte de reçu
		        	        indice_account = -1;
		        	        int indice_recipient;
		        	        indice_recipient = -1;
		        	        
		        	        for (int i = 0; i < tout_les_comptes.size(); i++) {
							        if (tout_les_comptes.get(i).getNumber().equals(number)) {
							            indice_account = i; // Compte donneur trouvé
							            break;
							        }
							    }
							
							// il faut que le compte de reçu existe
							for (int i = 0; i < tout_les_comptes.size(); i++) {
							        if (tout_les_comptes.get(i).getNumber().equals(recipient)) {
							            indice_recipient= i; // Compte destinataire trouvé
							            break;
							        }
							    }
							// si on a trouvé les comptes
							if(indice_recipient != -1 && indice_account !=-1) {
									// si on a un solde insuffisant, un message doit être signalé à l'utilisateur et l'opération ne doit être faite
									if(tout_les_comptes.get(indice_account) instanceof SavingAccount && amount_of_operation> tout_les_comptes.get(indice_account).getBalance()) {
										JOptionPane.showMessageDialog(null, "impossible operation");
										comboBox.setSelectedIndex(0);
									}	
									//même chose ici sauf que dans ce cas on traite le cas ou le compte émetteur est un compte courrant avec un creditlimit, par défaut tout les comptes courants on un credit limit 100
									else if(tout_les_comptes.get(indice_account) instanceof CurrentAccount && amount_of_operation> tout_les_comptes.get(indice_account).getBalance()+100) {
										JOptionPane.showMessageDialog(null, "impossible operation");
										comboBox.setSelectedIndex(0);
									}
									//aussi il faut éliminer le cas ou on essaie de transférer de l'argent d'un compte vers le même compte, car cela n'a aucun sens et va consommer des ressources sans intérêt
									else if(indice_recipient==indice_account) {
										JOptionPane.showMessageDialog(null, "impossible operation");
										comboBox.setSelectedIndex(0);
									}
									else {
									// si tout les conditions sont vérifiés on procède au transfert
									tout_les_comptes.get(indice_account).transfer(amount_of_operation, tout_les_comptes.get(indice_recipient));
										JOptionPane.showMessageDialog(null, "Transfert complete ");
										textArea.setText(tout_les_comptes.get(indice_account).bankStatement()); 
										comboBox.setSelectedIndex(0);
										
										
							        	//pour enegistrer mon operation dans la base de données
							            String url = "jdbc:mysql://localhost:3305/database_bank?serverTimezone=UTC";
							            String username = "root";
							            String password = "abderrahmane2003";
	
							            Connection connection = null;
							            try {
							                // Établir une connexion à la base de données
							                connection = DriverManager.getConnection(url, username, password);
	
							                String query = "INSERT INTO bank_table (number, name, balance, operation, date, amount, details_of_account) VALUES (?, ?, ?, ?, ?, ?, ?)";
							                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
							                    preparedStatement.setString(1, tout_les_comptes.get(indice_account).getNumber());
							                    preparedStatement.setString(2, tout_les_comptes.get(indice_account).getName());
							                    preparedStatement.setDouble(3, Math.round(tout_les_comptes.get(indice_account).getBalance()*100.0)/100.0); // pour ne garder que 2 chiffres après le virgule
							                    preparedStatement.setString(4, "WITHDRAW");
							                    LocalDate localDate = tout_les_comptes.get(indice_account).getOperations().get(tout_les_comptes.get(indice_account).getOperations().size() - 1).getDate();
							                    java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
							                    preparedStatement.setDate(5, sqlDate);
							                    preparedStatement.setDouble(6, Math.round(tout_les_comptes.get(indice_account).getOperations().get(tout_les_comptes.get(indice_account).getOperations().size() - 1).getAmount()*100.0)/100.0);
							                    if(tout_les_comptes.get(indice_account) instanceof CurrentAccount) {              
							                    preparedStatement.setString(7, "100 (authorized)");}
							                    else if(tout_les_comptes.get(indice_account) instanceof SavingAccount) {              
								                    preparedStatement.setString(7, "rate %5");}
							                    
							                    
							                    int rowsAffected = preparedStatement.executeUpdate();
	
							                    System.out.println(rowsAffected + " ligne insérée avec succès.");//un petit test
							                }
							            } catch (SQLException e1) {
							                e1.printStackTrace();
							            } finally {
							                // Fermer la connexion à la base de données
							                if (connection != null) {
							                    try {
							                        connection.close();
							                    } catch (SQLException e2) {
							                        e2.printStackTrace();
							                    }
							                }
							            }
							            
							            
							            // pour la partie receveur, on doit enregisrer dans la base de données que la reception est bien faite
							            String url1 = "jdbc:mysql://localhost:3305/database_bank?serverTimezone=UTC";
							            String username1 = "root";
							            String password1 = "abderrahmane2003";
	
							            Connection connection1 = null;
							            try {
							                // Établir une connexion à la base de données
							                connection1 = DriverManager.getConnection(url1, username1, password1);
	
							                String query = "INSERT INTO bank_table (number, name, balance, operation, date, amount, details_of_account) VALUES (?, ?, ?, ?, ?, ?, ?)";
							                try (PreparedStatement preparedStatement = connection1.prepareStatement(query)) {
							                    preparedStatement.setString(1, tout_les_comptes.get(indice_recipient).getNumber());
							                    preparedStatement.setString(2, tout_les_comptes.get(indice_recipient).getName());
							                    preparedStatement.setDouble(3, Math.round(tout_les_comptes.get(indice_recipient).getBalance()*100.0)/100.0);// pour ne garder que 2 chiffres après le virgule
							                    preparedStatement.setString(4, "DEPOSIT");
							                    LocalDate localDate = tout_les_comptes.get(indice_recipient).getOperations().get(tout_les_comptes.get(indice_recipient).getOperations().size() - 1).getDate();
							                    java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
							                    preparedStatement.setDate(5, sqlDate);
							                    preparedStatement.setDouble(6, Math.round(tout_les_comptes.get(indice_recipient).getOperations().get(tout_les_comptes.get(indice_recipient).getOperations().size() - 1).getAmount()*100.0)/100.0);
							                    if(tout_les_comptes.get(indice_recipient) instanceof CurrentAccount) {              
							                    preparedStatement.setString(7, "100 (authorized)");}
							                    else if(tout_les_comptes.get(indice_recipient) instanceof SavingAccount) {              
								                    preparedStatement.setString(7, "rate %5");}
							                    
							                    
							                    int rowsAffected = preparedStatement.executeUpdate();
	
							                    System.out.println(rowsAffected + " ligne insérée avec succès.");
							                }
							            } catch (SQLException e1) {
							                e1.printStackTrace();
							            } finally {
							                // Fermer la connexion à la base de données
							                if (connection1 != null) {
							                    try {
							                        connection1.close();
							                    } catch (SQLException e2) {
							                        e2.printStackTrace();
							                    }
							                }
							            }
									}//ici finit la partie de test de validité de transfert, le else de validité de transfert
							            																		
								}// ici finit mon if du test d'existance 
							//on arrive ici si on ne trouve pas soit le compte destinataire soit le compte émetteur
							else{JOptionPane.showMessageDialog(null, "impossible operation");
							}
     
		        	        transferFrame.dispose();
		        	        // Fermeture de la JFrame de transfert après avoir cliqué sur OK
							comboBox.setSelectedIndex(0);//pour que le comboBox ne selectionne plus aucune opération
		        	    }
		        	});

		        	// Ajout des composants à la nouvelle JFrame de transfert
		        	transferFrame.add(recipientLabel);
		        	transferFrame.add(recipientField);
		        	transferFrame.add(okButton);

		        	// Réglage de la disposition et de la visibilité de la JFrame de transfert
		        	transferFrame.setLayout(null);
		        	transferFrame.setVisible(true);
		        	
		        
		        	
		        }
		        
			}});
		
		//création d'un label Client
		JLabel lblNewLabel_5 = new JLabel("Costumer : ");
		lblNewLabel_5.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblNewLabel_5.setBounds(419, 11, 294, 25);
		contentPane.add(lblNewLabel_5);
		
		// le boutton details
		
		JButton detailsButton = new JButton("Details");
		detailsButton.setFont(new Font("Times New Roman", Font.BOLD, 13));
		detailsButton.setBounds(230, 119, 96, 23);
		contentPane.add(detailsButton);
		detailsButton.addActionListener((evt) -> {
			
			String number = noField.getText();
			int indice_account ; //l'indice du compte
			indice_account = -1;
			for (int i = 0; i < tout_les_comptes.size(); i++) {
			        if (tout_les_comptes.get(i).getNumber().equals(number)) {
			            indice_account = i; // Compte trouvé
			            break;
			        }
			    }
			if (indice_account == -1) {  // compte non trouvé 
				textArea.setText("inexisting account");
				}
			if (indice_account != -1 ){ // compte trouvé
				textArea.setText(tout_les_comptes.get(indice_account).bankStatement()); // on affiche ses imformations
				lblNewLabel_5.setText("Costumer : "+tout_les_comptes.get(indice_account).getName());
			}
		}
	);
		 
		/**/ // modification de font
		textArea.setFont(new Font("Verdana", Font.PLAIN, 12));
		textArea.setEditable(false); // pour rendre la zone de détails non modifiable à travers la fenetre CustumerFrame


		JLabel lblNewLabel_4 = new JLabel("Make Operation");
		lblNewLabel_4.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
		lblNewLabel_4.setBounds(39, 159, 128, 14);
		contentPane.add(lblNewLabel_4);
		Amount_Field.setBounds(71, 210, 107, 24);
		contentPane.add(Amount_Field);
		Amount_Field.setColumns(10);
		
		
		/// pour éviter que le client tappe quelque chose autre que des nombres dans Amount_Field
		Amount_Field.addKeyListener(new KeyListener() {
		    public void keyTyped(KeyEvent e) {
		        char c = e.getKeyChar();
		        if (!Character.isDigit(c) && c != '.') {
		            e.consume(); // Ignorer les caractères non numériques
		        }
		    }public void keyPressed(KeyEvent e) {}
		    public void keyReleased(KeyEvent e) {}
		});
		lblEntre.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblEntre.setBounds(40, 173, 175, 32);
		contentPane.add(lblEntre);
		lblSelectAnOperation.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblSelectAnOperation.setBounds(40, 246, 180, 32);
		contentPane.add(lblSelectAnOperation);
		contentPane.add(comboBox);
		
		JButton btnNewButton_1 = new JButton("Exit");
		btnNewButton_1.addActionListener((e)->{
			this.setVisible(false);
			JOptionPane.showMessageDialog(null, "données sauvegardées dans la base de données");
			 System.exit(0);
		}
		);		
		btnNewButton_1.setBounds(239, 288, 89, 23);
		contentPane.add(btnNewButton_1);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(30, 144, 255)));
		panel_1.setBounds(28, 177, 306, 104);
		contentPane.add(panel_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(30, 144, 255)));
		panel_2.setBounds(10, 68, 328, 244);
		contentPane.add(panel_2);
	}
}