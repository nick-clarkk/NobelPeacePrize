package edu.miracostacollege.cs112.ic15_nobelpeaceprize.controller;

import edu.miracostacollege.cs112.ic15_nobelpeaceprize.model.Model;
import edu.miracostacollege.cs112.ic15_nobelpeaceprize.model.NobelLaureate;
import javafx.collections.ObservableList;

/**
 * The <code>Controller</code> is a Singleton object that relays all commands between the Model and View
 * (and vice versa).  There is only one Controller object, accessible by a call to the static getInstance()
 * method.
 *
 * @author Michael Paulding
 * @version 1.0
 */
public class Controller {
	private ObservableList<NobelLaureate> mAllLaureatesList;

  //TODO: Implement the singleton pattern to ensure there is only one Controller object ever instantiated.
	//Only one object can ever be instantiated from the class
	//An example of this is the NumberFormat class where you instantiate one and only one instance.

	// 1) Field the same data type as the class (name it "theInstance" or "theOne")
	private static Controller theInstance;

	// 2) Make a public method called getInstance()

	public static Controller getInstance() {
		if (theInstance == null) {
			theInstance = new Controller();
			//Check to see whether to load data from CSV or Binary File
			//If size of binary file > 5L, load binary
			if (Model.binaryFileHasData())
				theInstance.mAllLaureatesList = Model.populateListFromBinaryFile();
			//Otherwise, use CSV
			else
				theInstance.mAllLaureatesList = Model.populateListFromCSVFile();
		}
		return theInstance;
	}

	// 3) Prevent other classes from making Controller objects, use private constructor
	private Controller() {

	}


	/**
	 * Gets the list of all laureates.
	 * @return The list of all laureates.
	 */
	public ObservableList<NobelLaureate> getAllLaureates() {
		return mAllLaureatesList;
	}

	/**
	 * Makes a request for the model to save all the laureates data (the list of all laureates) to
	 * a persistent binary file.
	 */
	public void saveData() {
		Model.writeDataToBinaryFile(mAllLaureatesList);
	}
}
