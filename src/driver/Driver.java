package driver;

import database.DbOperations;
import swingGUI.SwingComponents;

public class Driver {

	public static void main(String[] args) {
		DbOperations.startDbConnection();
		SwingComponents sc = new SwingComponents();
		sc.addCompnents();
	}

}
