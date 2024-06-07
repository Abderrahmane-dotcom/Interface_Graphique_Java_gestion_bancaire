package guiBuilder;

import java.util.ArrayList;
import javax.swing.SwingUtilities;
import bank.Account;
import bank.Bank;

@SuppressWarnings("unused")
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
		// nous avons mis en place la sérialisation dès la fermeture de la fenêtre AdminFrame et CustumerFrame.
		// on va remarquer son fonctionnement après la fermeture dans chaque relancement de l'application.
	}
}
