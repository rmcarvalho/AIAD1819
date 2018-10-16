package logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Game {

	/**
	 * Name of the file used to store the board territories adjacency data
	 */
	private static final String boardFileName = "board.dat";

	/**
	 * List of all the territories in the board
	 */
	private ArrayList<Territory> territories;

	/**
	 * Constructs a game class with no territories
	 */
	public Game() {
		territories = new ArrayList<Territory>();
	}

	public static void main(String[] args) {
		Game g = new Game();
		g.setup();
		
		for(int i = 0; i < g.territories.size(); i++) {
			 g.territories.get(i).dump();
		}
	}

	/**
	 * Sets the game settings and data
	 */
	private void setup() {
		loadTerritories();		
	}

	/**
	 * Reads the asset containing the territories data
	 * @return true if successful, false otherwise
	 */
	private boolean loadTerritories(){
		try{

			File file = new File("src/assets/" + Game.boardFileName);

			if(file.exists()){

				FileReader fr = new FileReader(file);
				BufferedReader reader = new BufferedReader(fr);

				String line = reader.readLine();
				int lineCount = 1;
				while(line != null)
				{
					if(!processDataLine(lineCount, line.split(",")))
					{
						System.out.println("Line format for board.dat asset should be integers separated by commas");
						reader.close();
						return false;
					}
					lineCount++;
					line = reader.readLine();
				}

				reader.close();


			}else{
				System.out.println("Couldn't find " + Game.boardFileName + " in \"assets\" folder!");
				return false;
			}

		}catch(IOException e){
			return false;
		}


		System.out.println("Territories successfully loaded!");
		return true;
	}

	/**
	 * Function that processes lines from the board asset
	 * @param currentTerritoryID line number that corresponds to current territory id
	 * @param neighbours line content (array with neighbours' id's)
	 * @return true if successfull, false otherwise
	 */
	private boolean processDataLine(int currentTerritoryID, String[] neighbours) {

		if(currentTerritoryID > this.territories.size())
		{
			fillTerritories(currentTerritoryID);
		}

		for (int i = 0; i < neighbours.length; i++) {
			int currentNeighbourID;
			try {
				currentNeighbourID = Integer.parseInt(neighbours[i]);	
			} catch (NumberFormatException e) { //not an integer program should shut down
				return false;
			}

			if(currentNeighbourID > this.territories.size())
			{
				fillTerritories(currentNeighbourID);
			}

			this.getTerritoryByID(currentTerritoryID).addNeighbour(this.getTerritoryByID(currentNeighbourID));
		}
		return true;
	}

	/**
	 * Function that creates the needed amount of territories to match the total of territories given as a parameter
	 * @param totalGoal goal amount of territories
	 */
	private void fillTerritories(int totalGoal)
	{
		while(this.territories.size() < totalGoal)
		{
			this.territories.add(new Territory(this.territories.size()));
		}
	}
	
	public Territory getTerritoryByID(int id) {
		if(id > this.territories.size()) {
			return null;
		}
		
		return this.territories.get(id-1);
	}

}
