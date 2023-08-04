package edu.miracostacollege.cs112.ic15_nobelpeaceprize.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.Arrays;
import java.util.Scanner;

/**
 * The <code>Model</code> class represents the business logic (data and calculations) of the application.
 * In the Nobel Peace Prize Laureates app, it either loads laureates from a CSV file (first load) or a binary file (all
 * subsequent loads).  It is also responsible for saving data to a binary file.
 *
 * @author Michael Paulding
 * @version 1.0
 */
public class Model {

  public static final String CSV_FILE = "NobelPeacePrizeWinners.csv";
	public static final String BINARY_FILE = "NobelPeacePrizeWinners.dat";

  //TODO: Implement the 4 methods below
	/**
	 * Determines whether the binary file exists and has data (size/length > 5L bytes).
	 * @return True if the binary file exists and has data, false otherwise.
	 */
	public static boolean binaryFileHasData()
	{
		//Check to see if it exists and if it has a length of at least 5 bytes
		//Empty files have 4 bytes
		File binaryFile = new File(BINARY_FILE);
		return (binaryFile.exists() && binaryFile.length() >= 5);
	}

	/**
	 * Populates the list of all laureates from the binary file. This will only be called once, the first time the app
	 * loaded to seed initial data from the CSV file.  All subsequent loads will be extracted from
	 * the binary file.be called everytime the application loads,
	 * @return The list of all laureates populated from the CSV file
	 */
	public static ObservableList<NobelLaureate> populateListFromCSVFile()
	{
		//creates a new empty list with capacity = 10
		//Updates as new information is introduced
		ObservableList<NobelLaureate> allLaureates = FXCollections.observableArrayList();
		//Read data from CSV file, use Scanner
		try {
			Scanner input = new Scanner(new File(CSV_FILE));
			int year;
			double prizeAmount;
			String name, motivation, country, line;
			String parts[];

			//Skip the first line (header)
			input.nextLine();

			//Loop through the file one line at a time
			while (input.hasNextLine()) {
				line = input.nextLine();
				//Divide the line into parts by the commas
				parts = line.split(",");
				//parts[0] is year
				//parts[1] is prize type

				year = Integer.parseInt(parts[0]);
				prizeAmount = Double.parseDouble(parts[6]);
				name = parts[13];
				motivation = parts[9];
				country = parts[26];

				allLaureates.add(new NobelLaureate(name, year, motivation, country, prizeAmount));
			}

			input.close();
		} catch (FileNotFoundException e) {
			System.err.println("Error:" + e.getMessage());
		}
		return allLaureates;
	}

	/**
	 * Populates the list of all laureates from the binary file. This will be called everytime the application loads,
	 * other than the very first time, since it needs initial data from CSV.
	 * @return The list of all laureates populated from the binary file
	 */
	public static ObservableList<NobelLaureate> populateListFromBinaryFile()
	{
		//New empty list with capacity 10
		ObservableList<NobelLaureate> allLaureates = FXCollections.observableArrayList();
		//Read from binary file
		try {
			ObjectInputStream fileReader = new ObjectInputStream(new FileInputStream(BINARY_FILE));
			//Flow: binary file -> array (fixed size) -> observable list
			//Make temp array to store binary file data
			NobelLaureate[] tempArray = (NobelLaureate[]) fileReader.readObject();
			//Copy data from temp to observable list
			allLaureates.addAll(Arrays.asList(tempArray));
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error:" + e.getMessage());
		}
		return allLaureates;
	}

	/**
	 * Saves the list of all laureates to the binary file. This will be called each time the application stops,
	 * which occurs when the user exits/closes the app.  Note this method is called in the View, by the controller,
	 * during the stop() method.
	 * @return True if the data were saved to the binary file successfully, false otherwise.
	 */
	public static boolean writeDataToBinaryFile(ObservableList<NobelLaureate> allLaureatesList)
	{
		//If list is empty (size = 0) return false
		if (allLaureatesList.size() == 0)
			return false;
		try {
			ObjectOutputStream fileWriter = new ObjectOutputStream(new FileOutputStream(BINARY_FILE));
			NobelLaureate[] tempArray = new NobelLaureate[allLaureatesList.size()];
			allLaureatesList.toArray(tempArray);

			fileWriter.writeObject(tempArray);

			fileWriter.close();
		}
		catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
		}
		return true;
	}

}
