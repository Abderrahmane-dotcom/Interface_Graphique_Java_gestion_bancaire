package bank;
//C'est une interface qui inclus le read de CRUD
import java.util.ArrayList;

public interface InterfaceDao {
	
	ArrayList<Account> recuperer_all_accounts(String NomTable); 
}
