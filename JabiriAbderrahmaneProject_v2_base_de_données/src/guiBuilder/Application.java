// Data base version
package guiBuilder;

import javax.swing.SwingUtilities;
import bank.Bank;

public class Application {
	public static void main(String[] args) {
		//1. Simulation
		Bank bank = Bank.getInstance();
		bank.setName("SimBank");
		bank.makeSimulation();
		//2. Authentification
		SwingUtilities.invokeLater( () -> {	
			var authFrame = new AuthFrame(bank);
			authFrame.setVisible(true);
		});
		/* Le processus de sauvegarde dans notre base de données est effectué d'une manière automatique à chaque opération
		 * que ce soit aux opérations de créations de comptes ou lors des opérations de deposit, de withdraw ou de transfert :
		 * chaque opération est instantanément enregistrée dans la base de données. 
		 * */
		
	}
}
