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
import java.util.ArrayList;

import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.LineBorder;

import bank.Account;
import bank.Bank;
import bank.CurrentAccount;
import bank.InsufficientFundException;
import bank.SavingAccount;

import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class CustomerFrame extends JFrame {

	private JPanel contentPane;
	private JTextField noField;
	private final JTextField Amount_Field = new JTextField();
	private final JLabel lblEntre = new JLabel("Entre an amount, please");
	private final JLabel lblSelectAnOperation = new JLabel("Select an operation, please");
	
	//déclarations
	String newoperation;
	String number;
	String inputamount;
	double amount_of_operation;
	int indice_account ; 

	/*
	 * Créons le frame.
	 */
	public CustomerFrame(Bank bank) {

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
		
		// ajout du scroll pane pour pouvoir visualiser les opérations si on en effectue beaucoup
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
		
		/* 
		  * Pour prendre en considération les données des comptes existants dans le fichier
		  * bank.dta , nous allons prendre tous  ses données et les stocker dans une liste accountsLoaded , on effectuera ensuite,
		  * nos modifications et puis on va sauvgarder le tout dans le fichier bank.dta. 
		  * Il est à noter que si le fichier bank.dta n'existe pas , il va être crée, et s'il est vide , il va être
		  * rempli comme on a déjà expliqué dans la definition de la fonction save dans la classe Bank.
		*/
 		
		ArrayList<Account> accountsLoaded ;
		accountsLoaded = bank.load(Bank.FILENAME);
		accountsLoaded.addAll(bank.getAccounts());
		ArrayList<Account> accounts_Loaded = bank.load(Bank.FILENAME);
		accounts_Loaded.forEach(
				(c) -> System.out.println(c. bankStatement ())
				);
				
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
		            /*
		             * on doit chercher si le compte saisi est bien existant
		             */
		            indice_account = -1;
		            for (int i = 0; i < accountsLoaded.size(); i++) {
		                if (accountsLoaded.get(i).getNumber().equals(number)) {
		                    indice_account = i; // Compte trouvé
		                    break;
		                }
		            }
		            if (indice_account == -1) { // compte non trouvé
		                textArea.setText("inexisting account, impossible operation");
		            } else {
		                accountsLoaded.get(indice_account).deposit(amount_of_operation);
		                JOptionPane.showMessageDialog(null, "operation done ");
		                textArea.setText(accountsLoaded.get(indice_account).bankStatement()); // on affiche ses informations
		                comboBox.setSelectedIndex(0);
		            }
		        }
				//l'operation est "WITHDRAW"
		        else if(newoperation.equals("WITHDRAW") && !newoperation.equals("no operation done")) {
					number = noField.getText();
					inputamount = Amount_Field.getText();
					amount_of_operation = Double.parseDouble(inputamount);
					System.out.println(amount_of_operation);
					// de même, on cherche l'existence du compte
 					indice_account = -1;
					for (int i = 0; i < accountsLoaded.size(); i++) {
					        if (accountsLoaded.get(i).getNumber().equals(number)) {
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
							accountsLoaded.get(indice_account).withdraw(amount_of_operation);
						} catch (InsufficientFundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null, "impossible operation : insufficient fund ");
							test = 1;  // pour tester si on a passé par l'exception ou pas
						}
						if(test==0) {
						JOptionPane.showMessageDialog(null, "operation done ");}
						textArea.setText(accountsLoaded.get(indice_account).bankStatement()); // on affiche ses imformations
						newoperation = "no operation done";
						comboBox.setSelectedIndex(0);
					}
				}
		        //lorseque l'opération est transfer
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

		        	/*
		        	 * Pour traiter le cas spécial du transfert ou on doit savoir le compte destinnatire
		        	 * et s'il existe vraiment , et puis effectuer l'opération, pour cela on va afficher une nouvelle fenêtre qui permet à l'utilisatur de rentrer
		        	 * le compte destinnataire.
		        	 */
		        	
		        	JButton okButton = new JButton("TRANSFERT");
		        	okButton.setBounds(180, 70, 130, 30);
		        	okButton.addActionListener(new ActionListener() {
		        	    public void actionPerformed(ActionEvent e) {
		        	       
		        	        String recipient = recipientField.getText(); //recuperation du compte de recu
		        	        indice_account = -1;
		        	        int indice_recipient;
		        	        indice_recipient = -1;
		        	        
		        	        for (int i = 0; i < accountsLoaded.size(); i++) {
							        if (accountsLoaded.get(i).getNumber().equals(number)) {
							            indice_account = i; // Compte donneur trouvé, on récupère son indice dans accountsLoaded.
							            break;
							        }
							    }

							for (int i = 0; i < accountsLoaded.size(); i++) {
							        if (accountsLoaded.get(i).getNumber().equals(recipient)) {
							            indice_recipient= i; // Compte destinataire trouvé, on récupère son indice dans accountsLoaded.
							            break;
							        }
							    }
							if(indice_recipient != -1 && indice_account !=-1 && indice_recipient!=indice_account) {  //si on a trouvé les deux comptes, on procède au transfert mais avant on teste toujours s'il est 
																				 //possible. Un transfert vers le même compte n'a aucun sens pour cela on va l'interdire
								if (accountsLoaded.get(indice_account) instanceof SavingAccount && amount_of_operation > accountsLoaded.get(indice_account).getBalance()) {
									JOptionPane.showMessageDialog(null, "impossible operation");
									comboBox.setSelectedIndex(0);
								}
								else if (accountsLoaded.get(indice_account) instanceof CurrentAccount && amount_of_operation > accountsLoaded.get(indice_account).getBalance()+100)
								{
									JOptionPane.showMessageDialog(null, "impossible operation");
									comboBox.setSelectedIndex(0);
								}
								else{
									accountsLoaded.get(indice_account).transfer(amount_of_operation, accountsLoaded.get(indice_recipient));
									JOptionPane.showMessageDialog(null, "Transfert complete ");
									textArea.setText(accountsLoaded.get(indice_account).bankStatement()); 
									comboBox.setSelectedIndex(0);}
									
							}
							else{JOptionPane.showMessageDialog(null, "impossible operation");}
		        	        transferFrame.dispose();
		        	        // Fermeture de  la JFrame de transfert après avoir cliqué sur OK
		        	        comboBox.setSelectedIndex(0);
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
			for (int i = 0; i < accountsLoaded.size(); i++) {
			        if (accountsLoaded.get(i).getNumber().equals(number)) {
			            indice_account = i; // Compte trouvé
			            break;
			        }
			    }
			
			
			if (indice_account == -1) {  // compte non trouvé et aucune opération réalisée
				textArea.setText("inexisting account");
				}
			if (indice_account != -1 ){
				textArea.setText(accountsLoaded.get(indice_account).bankStatement()); // on affiche ses imformations
				lblNewLabel_5.setText("Costumer : "+accountsLoaded.get(indice_account).getName());
			}

		}
			
			
		);
		 
		// modification de font
		textArea.setFont(new Font("Verdana", Font.PLAIN, 12));
		textArea.setEditable(false); // pour rendre la zone de détails non modifiable à travers la fenetre CustumerFrame


		JLabel lblNewLabel_4 = new JLabel("Make Operation");
		lblNewLabel_4.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
		lblNewLabel_4.setBounds(39, 159, 128, 14);
		contentPane.add(lblNewLabel_4);
		Amount_Field.setBounds(71, 210, 107, 24);
		contentPane.add(Amount_Field);
		Amount_Field.setColumns(10);
		
		
		// pour éviter que le client tappe quelque chose autre que des nombres dans Amount_Field
		Amount_Field.addKeyListener(new KeyListener() {
		    public void keyTyped(KeyEvent e) {
		        char c = e.getKeyChar();
		        if (!Character.isDigit(c) && c != '.') {
		            e.consume(); // Ignorer les caractères non numériques cad autre que les nombres et le point
		        }
		    }

		    public void keyPressed(KeyEvent e) { 
		    }
		    public void keyReleased(KeyEvent e) {
		       }
		});
		
		
		
		lblEntre.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblEntre.setBounds(40, 173, 175, 32);
		contentPane.add(lblEntre);
		lblSelectAnOperation.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblSelectAnOperation.setBounds(40, 246, 180, 32);
		contentPane.add(lblSelectAnOperation);
		
		
		
		contentPane.add(comboBox);
		
		JButton btnNewButton_1 = new JButton("Exit");
		btnNewButton_1.addActionListener((e) -> {
		    // Cacher la fenêtre
		    this.setVisible(false);

		    // Sauvegarde des données
		    bank.save_avec_ecrasement(Bank.FILENAME,accountsLoaded);
		    /*saving in the bank.dta, puisque ici accounts loaded contient déja tout les comptes existants
		    La sauvegarde sera par ecrasement pour ne pas dupliquer les comptes .*/ 

		    // Affichage d'un message avec JOptionPane
		    JOptionPane.showMessageDialog(null, "données sauvegardées dans : bank.dta");

		    // Affichage d'un message dans la console
		    System.out.println();
		    System.out.println("-------------------Les données sont sérialisées-------------------");     
		    System.exit(0);
		});
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
		
		// on sérialise les données dés la fermeture de la fenetre pour qu'elles soit accessibles après 
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	
            	bank.save_avec_ecrasement(Bank.FILENAME,accountsLoaded);// saving in the bank.dta
            	JOptionPane.showMessageDialog(null, "données sauvegardés dans : bank.dta");
            	System.out.println();
     		    System.out.println("-------------------Les données sont sérialisées-------------------");     
            	// ici est un simple test pour tester dans le console si tout les comptes sont sauvegardés
        		ArrayList<Account> accounts_Loaded = bank.load(Bank.FILENAME);
        		accounts_Loaded.forEach(
        				(c) -> System.out.println(c. bankStatement ())
        				);
        		// 
	            dispose();
	            System.exit(0); 
                
            }
        });
		
	}
}