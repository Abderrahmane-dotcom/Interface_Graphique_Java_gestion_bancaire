// Data base version

package guiBuilder;
import javax.swing.*; 
import javax.swing.border.*;
import java.awt.*;
import bank.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
/**
 * This JFrame was generated using WindowBuilder.
 */
public class AdminFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1000243894532880928L;
	String newAccountType ;
	//GUI
	private JPanel contentPane;
	private JTextField nameTextField;
	private JTextField balanceTextField;
	private JTextArea detailsArea ;


	/**
	 * Create the frame.
	 */
	public AdminFrame(Bank bank) {
		bank.setName(bank.getName());

		
		detailsArea = new JTextArea();
		addWindowListener(new WindowAdapter() {  //ce WindowListener permet de fermer l'application une fois que l'utilisateur sort de la fenêtre 
	        @Override
	        public void windowClosing(WindowEvent e) {
	            dispose();
	            JOptionPane.showMessageDialog(null, "données sauvegardés dans la base de données");
	            System.exit(0); 
	        }
	    });
		setBounds(100, 100, 717, 363);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel newPanel = new JPanel();
		newPanel.setBorder(new TitledBorder(null, "New Account", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		newPanel.setBounds(10, 43, 183, 277);
		contentPane.add(newPanel);
		newPanel.setLayout(null);
		
		nameTextField = new JTextField();
		nameTextField.setBounds(92, 27, 81, 20);
		newPanel.add(nameTextField);
		nameTextField.setColumns(10);
		
		JLabel nameLabel = new JLabel("New name");
		nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		nameLabel.setBounds(10, 27, 56, 20);
		nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		newPanel.add(nameLabel);
		
		JLabel balanceLabel = new JLabel("New Balance");
		balanceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		balanceLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		balanceLabel.setBounds(10, 84, 67, 20);
		newPanel.add(balanceLabel);
		
		balanceTextField = new JTextField();
		balanceTextField.setColumns(10);
		balanceTextField.setBounds(92, 84, 81, 20);
		newPanel.add(balanceTextField);
		
		JButton createButton = new JButton("Create new account");
		createButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		// création d'une liste de tout les comptes existant dans ma base
		Recuperateur_De_Comptes Récupérateur = new Recuperateur_De_Comptes();
		ArrayList<Account> tout_les_comptes; 
		tout_les_comptes = Récupérateur.recuperer_all_accounts("bank_table");
		
		createButton.setBounds(10, 187, 163, 23);
		newPanel.add(createButton);
		createButton.addActionListener((evt) -> {			
			
			String name = nameTextField.getText();
			nameTextField.requestFocus();
			String balanceStr = balanceTextField.getText();
			
			/// partie remplissage des informations du nouveau compte
			Account newAccount = null;   											// une simple initialisation
			if( (newAccountType == null) ||
			(newAccountType.equals("Current")) ) {
			newAccount = new CurrentAccount(name,Double.valueOf(balanceStr),100);	//nous avons fixé pour le compte courant 100 comme credit Limit 
			  //ajout du compte qui vient de se créer dans la liste tout_les_comptes
			tout_les_comptes.add(newAccount);
			  //mais aussi il faut l'ajouter dans la base de donnée 
			String jdbcUrl = "jdbc:mysql://localhost:3305/database_bank?serverTimezone=UTC";		// ma base de données
	        String username = "root";
	        String password = "abderrahmane2003";

	        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
	            String insertQuery = "INSERT INTO bank_table (number, name, balance, operation, date, amount, details_of_account) " +
	                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

	            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
	                // Set values pour chaque parametre
	                preparedStatement.setString(1,newAccount.getNumber()); //le numéro de notre nouveau compte 
	                preparedStatement.setString(2, newAccount.getName()); //Son nom d'utilisateur
	                preparedStatement.setDouble(3, Math.round(newAccount.getBalance()*100.0)/100.0); // Sa balance , en ne gardant que 2 chiffres après la virgule 
	                preparedStatement.setString(4,"DEPOSIT"); //on a représenté l'opération de création de compte par un deposit 0
	                preparedStatement.setDate(5, Date.valueOf(LocalDate.now()));
	                preparedStatement.setDouble(6,0.00); // on a représenté l'opération de création de compte par un deposit 0
	                if
	                (newAccount instanceof SavingAccount) {
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
			}
			
			
			
			else {
			newAccount = new SavingAccount(name,Double.valueOf(balanceStr),0.05);  // par défaut, tous les SavingAccounts ont un intérêt de 5%  
			//ajout du compte qui vient de se créer dans la liste accounts
			tout_les_comptes.add(newAccount);
			
			//mais aussi il faut l'ajouter dans la base de donnée 
			String jdbcUrl = "jdbc:mysql://localhost:3305/database_bank?serverTimezone=UTC";		
	        String username = "root";
	        String password = "abderrahmane2003";

	        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
	            String insertQuery = "INSERT INTO bank_table (number, name, balance, operation, date, amount, details_of_account) " +
	                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

	            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
	                // Set values pour chaque parametre
	                preparedStatement.setString(1,newAccount.getNumber()); 
	                preparedStatement.setString(2, newAccount.getName()); 
	                preparedStatement.setDouble(3, Math.round(newAccount.getBalance()*100.0)/100.0);
	                preparedStatement.setString(4,"DEPOSIT");
	                preparedStatement.setDate(5, Date.valueOf(LocalDate.now()));
	                preparedStatement.setDouble(6,0.00);
	                if
	                (newAccount instanceof SavingAccount) {
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
			}
			
			JOptionPane.showMessageDialog(null, "account added ");
			System.out.println(newAccount.toString());
			///
			
			
			refresh(tout_les_comptes,detailsArea);
			
			// modification de la taille du texte
			Font font = detailsArea.getFont();
			Font newFont = font.deriveFont(10f); // Nouvelle taille de police : 10
			detailsArea.setFont(newFont);
			
		});


		
		
		JLabel lblAccount = new JLabel("Account");
		lblAccount.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAccount.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblAccount.setBounds(10, 138, 67, 20);
		newPanel.add(lblAccount);
		
		JComboBox<String> comboBox = new JComboBox<>();
		comboBox.setModel(new DefaultComboBoxModel<String>(
				new String[] {"Current", "Saving"})
				);
		comboBox.setBounds(92, 138, 81, 22);
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				newAccountType = (String) comboBox.getSelectedItem();			
			}
			
		});
		newPanel.add(comboBox);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener((e)->{
			this.setVisible(false);
			JOptionPane.showMessageDialog(null, "données sauvegardées dans la base de données");
			 System.exit(0);
		}
		);		
		btnExit.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnExit.setBounds(95, 242, 78, 23);
		newPanel.add(btnExit);
		
		
		JLabel bankNameLabel = new JLabel(bank.getName()+" (Admin)");

		bankNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bankNameLabel.setBounds(173, 11, 396, 31);
		bankNameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		contentPane.add(bankNameLabel);
		
		JPanel accountsPanel = new JPanel();
		accountsPanel.setLayout(null);
		accountsPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Accounts List", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		accountsPanel.setBounds(193, 43, 498, 277);
		contentPane.add(accountsPanel);
		
		detailsArea.setFont(new Font("Verdana", Font.PLAIN, 10));
		detailsArea.setBounds(10, 23, 478, 243);
		accountsPanel.add(detailsArea);
		detailsArea.setEditable(false);// annuler toute possibilité d'écriture externe
		
		/// l'ajout de la possibilité de défilement, car il y'aura beaucoup de comptes
		JScrollPane scrollPane = new JScrollPane(detailsArea);
		scrollPane.setBounds(10, 23, 478, 243);
		accountsPanel.add(scrollPane);
		
		refresh(tout_les_comptes,detailsArea);
		
		
	}	
/**
 * 
 * @param accounts
 * @param detailsArea
 */
	private void refresh(List<Account> accounts, JTextArea detailsArea) {
		detailsArea.setText("");
		accounts.forEach((account) -> {
			detailsArea.append(account+"\n");
		});
	}
	
	}

