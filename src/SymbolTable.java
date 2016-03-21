import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/*
 * Implementation of a symbol table using the Map Library in Java.
 * Maps a "Name" to a "Triplet<String, String, Integer>"
 * 
 * Includes methods for:
 * 		Searching for matching keys
 * 		Putting things into symbol table
 * 		Returning the size of the table
 * 		Printing the table to the Console
 * 		Writing the table to a file.
 */
public class SymbolTable {
	
	//Instance Variables
	private Map<String, Triplet<String, String, Integer>> symTable =
			new HashMap<String, Triplet<String, String, Integer>>();
	
	//getter methods
	public Map<String, Triplet<String, String, Integer>> getSymTable() {
		return symTable;
	}

	//Method to put things into symbol table
	public void put(String string, Triplet<String, String, Integer> triplet) {
		symTable.put(string, triplet);
	}

	//get entry in the symbol table
	public Triplet<String, String, Integer> get(String name) {
		return symTable.get(name);
	}
	
	//update integer value
	public void updateIntValue(String key, Integer a) {
		Triplet<String, String, Integer> trip = symTable.get(key);
		symTable.put(key, new Triplet<String, String, Integer>(trip.getFirst(), trip.getSecond(), a));
	}
	
	//check to find a key
	public boolean containsKey(String name) {
		return symTable.containsKey(name);
	}

	public int size() {
		return symTable.size();
	}
	
	//Prints to console
	public void printTable() {
		System.out.println("SYMBOL TABLE");
		String formatStr = "%-5s %-10s %-10s\n";
		System.out.printf(formatStr, "TYPE", "CH VALUE", "INT VALUE");
		System.out.printf(String.format(formatStr, "====", "========", "========="));
		
		//loop through symbol table
		for (Entry<String, Triplet<String, String, Integer>> entry : symTable.entrySet()) {
			Triplet<String,String,Integer> triplet = entry.getValue();
			//print each to line
			System.out.printf("%-5s %-10s %-10d\n", 
					triplet.getFirst(), triplet.getSecond(), triplet.getThird());				
		}
	}
	
	//Writes to file
	public void writeSTtoFile(String fileName) {
		try {
			FileWriter writer = new FileWriter(fileName);
			String formatStr = "%-5s %-10s %-10s\n";
			writer.write(String.format(formatStr, "TYPE", "CH VALUE", "INT VALUE"));
			writer.write(String.format(formatStr, "====", "========", "========="));
			//Loop through the symboltable
			for (Entry<String, Triplet<String, String, Integer>> entry : symTable.entrySet()) {
				Triplet<String,String,Integer> triplet = entry.getValue();
				
				//write each line to a file.
				writer.write(String.format("%-5s %-10s %-10d\n", 
						triplet.getFirst(), triplet.getSecond(), triplet.getThird()));				
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
