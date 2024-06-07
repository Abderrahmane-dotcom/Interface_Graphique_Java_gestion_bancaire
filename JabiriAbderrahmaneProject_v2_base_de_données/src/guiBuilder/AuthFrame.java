// Data base version
package guiBuilder;

import javax.swing.*;
import bank.Bank;

import javax.swing.border.*;

import java.awt.Color;
import java.awt.event.*;
/**
 * 
 * @author elfaker
 * This JFrame was generated using WindowBuilder.
 * 	In eclipse : Help>Eclipse Marketplace> Find �WindowBuilder�>Install
 *
 */
public class AuthFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6532322191224082909L;
	private JPanel contentPane;
	private JTextField loginTextField;
	private JPasswordField passwordField;
	
	private JLabel statusLabel ; 

	/**
	 * Create the frame.
	 */
	public AuthFrame(Bank bank) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 361, 270);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel authPanel = new JPanel();
		authPanel.setBorder(new TitledBorder(null, "Authentification", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		authPanel.setBounds(5, 5, 332, 164);
		contentPane.add(authPanel);
		authPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Login");
		lblNewLabel.setBounds(27, 26, 46, 20);
		authPanel.add(lblNewLabel);
		
		loginTextField = new JTextField();
		loginTextField.setBounds(124, 26, 157, 20);
		authPanel.add(loginTextField);
		loginTextField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(124, 64, 157, 20);
		authPanel.add(passwordField);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(27, 64, 87, 14);
		authPanel.add(lblPassword);
		
		JButton btnNewButton = new JButton("OK");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String login = loginTextField.getText();
				String password = new String(passwordField.getPassword());
				if 	(login.equals("a") && password.equals("")) {
					System.out.println("Admin authentication successful. Access granted.\n");
					statusLabel.setText("Admin authentication successful. Access granted.\n");
					var adminFrame = new AdminFrame(bank);
					adminFrame.setVisible(true);
				}
				else if 	(login.equals("c") && password.equals("")) {
					System.out.println("Customer authentication successful. Access granted.\n");
					statusLabel.setText("Customer authentication successful. Access granted.\n");
					var customerFrame = new CustomerFrame(bank);
					customerFrame.setVisible(true);
				}
				else {
					System.out.println("Authentication failed. Access denied.");
					System.out.println("Hacker alert. ");
					System.exit(0);
					
				}
	
			}
		});
		btnNewButton.setBounds(124, 109, 59, 23);
		authPanel.add(btnNewButton);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginTextField.setText("");
				passwordField.setText("");
				statusLabel.setText("");
			}
		});
		btnCancel.setBounds(210, 109, 77, 23);
		authPanel.add(btnCancel);
		
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(null);
		statusPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Status", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		statusPanel.setBounds(5, 180, 332, 41);
		contentPane.add(statusPanel);
		
		statusLabel = new JLabel("");
		statusLabel.setBounds(10, 11, 312, 19);
		statusPanel.add(statusLabel);
	}
}
