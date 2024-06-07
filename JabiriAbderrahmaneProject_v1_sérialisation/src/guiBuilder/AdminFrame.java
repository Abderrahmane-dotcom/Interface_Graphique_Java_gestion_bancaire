package guiBuilder;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import bank.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class AdminFrame extends JFrame {

	private static final long serialVersionUID = -1000243894532880928L;
	String newAccountType ;

	private JPanel contentPane;
	private JTextField nameTextField;
	private JTextField balanceTextField;
	private JTextArea detailsArea ;


	/*
	 * Créons le Frame
	 */
	public AdminFrame(Bank bank) {
		bank.setName(bank.getName());
		detailsArea = new JTextArea();

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
		
		
		
		JButton createButton = new JButton("Create new account");// le boutton de création de compte
		createButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		createButton.setBounds(10, 187, 163, 23);
		newPanel.add(createButton);
		createButton.addActionListener((evt) -> {									/*ici on lui associe une action, qui sera de créer un newAccount se basant
		 																			sur les informations dans les champs new name, new balance, et le comboBox  */ 
			
			String name = nameTextField.getText();
			nameTextField.requestFocus();
			String balanceStr = balanceTextField.getText();
			
			/// partie remplissage des informations du nouveau compte
			Account newAccount = null;
			if( (newAccountType == null) ||
			(newAccountType.equals("Current")) ) {										//si la valeur spécifié dans le comboBox est Current, on crée un compte courant
																						//nous avons décidé que par défault, le crédit limite est : 100.																		
				newAccount = new CurrentAccount(name,Double.valueOf(balanceStr),100);
				  //ajout du compte qui vient de se créer dans la liste accounts
				bank.addAccount(newAccount);
			}
			
			else {
				newAccount = new SavingAccount(name,Double.valueOf(balanceStr),0.05);	    //sinon on crée un SavingAccount, par défaut le taux d'intéret est 5%.
				//ajout du compte qui vient de se créer dans la liste accounts
				bank.addAccount(newAccount);
			}
			JOptionPane.showMessageDialog(null, "account added "); 
			System.out.println(newAccount.toString());
			
			/// ici on va récupérer les comptes déjà existant dans le fichier bank.dta
			ArrayList<Account> accountsLoaded = bank.load(Bank.FILENAME);
			//on leur ajoute les comptes crées par la simulation + celui qui vient d'être créer par l'utilisateur
			accountsLoaded.addAll(bank.getAccounts());
			//on affiche ensuite le tout dans le detailsArea, pour que l'administrateur voit le compte crée 
			refresh(accountsLoaded,detailsArea);
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
		btnExit.addActionListener((e) -> {
		    // Cacher la fenêtre
		    this.setVisible(false);

		    // Affichage d'un message avec JOptionPane
		    JOptionPane.showMessageDialog(null, "données sauvegardées dans : bank.dta");
		    // Sauvegarde des données
		    bank.save(Bank.FILENAME); 
		    // Affichage d'un message dans la console
		    System.out.println();
		    System.out.println("-------------------Les données sont sérialisées-------------------"); 
		    System.exit(0);
		});
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
		detailsArea.setEditable(false);// annuler toute possibilité d'écriture externe par le clavier
		
		/*
		 * l'ajout de la possibilité de défilement. En effet, il y'aura plusieurs comptes que l'administrateur doit visualiser.
		 */
		JScrollPane scrollPane = new JScrollPane(detailsArea);
		scrollPane.setBounds(10, 23, 478, 243);
		accountsPanel.add(scrollPane);
		
		/*
		 * ici c'est la phase avant la création d'un compte : on charge les comptes déjà existant dans bank.dta
		 * et on leur ajoute ceux crée par la simulation, et on affiche tout 
		 */
		ArrayList<Account> accounts_Loaded;
		accounts_Loaded = bank.load(Bank.FILENAME); 		
		accounts_Loaded.addAll(bank.getAccounts());		
		refresh(accounts_Loaded,detailsArea);
		
		/*
		 *  on sérialise les données dés la fermeture de la fenetre pour qu'elles soit accessibles après.
		 */
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	bank.save(Bank.FILENAME);
            	JOptionPane.showMessageDialog(null, "données sauvegardés dans : bank.dta");
    		    System.out.println();
    		    System.out.println("-------------------Les données sont sérialisées-------------------");             
            	//ici juste un petit test pour voir si vraiment si vraiment les données sont sérialisés, on peut s'en passer
            	ArrayList<Account> accountsLoaded = bank.load(Bank.FILENAME);
        		accountsLoaded.forEach(
        				(c) -> System.out.println(c. bankStatement ())
        				);
	            //         		
        		dispose();
	            System.exit(0);             	
            }
        });
	}

	
	
/**
 * 
 * @param accounts
 * @param textArea
 */
	private void refresh(List<Account> accounts, JTextArea detailsArea) {
		detailsArea.setText("");
		accounts.forEach((account) -> {
			detailsArea.append(account+"\n");
		});
	}
	

}
