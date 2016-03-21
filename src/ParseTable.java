import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class ParseTable {
	//Table represented with two hashMaps, one for row and column
	private static Map<String, ArrayList<String>> rowMap = new HashMap<String, ArrayList<String>>();
	private static Map<String, Integer> columnMap = new HashMap<String, Integer>();

	/**
	 * Initialize the Parse Table by initializing rowMap and columnMap
	 * 
	 * @param fileName
	 */
	ParseTable(String fileName) {
		try {
			//buffer to hold each line
			String currentLine = null;
			
			//standard reading from file
			BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));

			//array for the header of each column
			ArrayList<String> columnEntryArray = new ArrayList<String>();
			while (((currentLine = bufferedReader.readLine()) != null)) {
				
				//makes key for the row
				String rowKey = currentLine.substring(0, currentLine.indexOf("&"));
				
				//makes each row
				ArrayList<String> rowList = new ArrayList<String>(Arrays.asList(currentLine.split("&")));
				
				//gets rid of row label
				rowList.remove(0);

				//check if in dictionary
				if (!rowKey.equals("") && !rowMap.containsKey(rowKey)) {
					
					//makes empty array for the rest of entries that don't show up when you use "split"
					String[] emptyArray = new String[18 - rowList.size()];
					
					//fill with empty string
					Arrays.fill(emptyArray, "");
					
					//make into an ArrayList
					ArrayList<String> empty = new ArrayList<String>(Arrays.asList(emptyArray));
					
					//combine the row list with empty list
					rowList.addAll(empty);
					
					//add to map
					rowMap.put(rowKey, rowList);

				} //If already in dictionary, (adding the second part of the table)
				else if (!rowKey.equals("")) {
					
					//Makes an empty array for the rest of the entries that don't appear when using "split"
					//eg, split("1&&", &) will produce [1]... I want [1, "", ""]
					String[] emptyArray = new String[6 - rowList.size()];
					
					//fill the array with empty strings
					Arrays.fill(emptyArray, "");
					
					//make the array into an ArrayList for easy adding
					ArrayList<String> empty = new ArrayList<String>(Arrays.asList(emptyArray));
					
					//get the entry that's already in the map
					ArrayList<String> newEntry = rowMap.get(rowKey);
					
					//Add the row list first
					newEntry.addAll(rowList);
					
					//add the rest of the empty entries
					newEntry.addAll(empty);
					
					//put updated list into the map
					rowMap.put(rowKey, newEntry);
					
				}//these rows are to make the columnmap 
				else if (rowKey.equals("") && columnMap.size() != 0) {
					columnEntryArray = new ArrayList<String>(rowList);
				}
				//get the second half of the table 
				else {
					columnEntryArray.addAll(rowList);
				}

			}
			//create the map for the columns 
			for (int i = 0; i < columnEntryArray.size(); i++) {
				columnMap.put(columnEntryArray.get(i), i);
			}

			//standard error report for file reading
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Get the corresponding entry in the parse table.
	 * 
	 * @param rowLabel
	 * @param columnLabel
	 * @return
	 */
	public String getEntry(String rowLabel, String columnLabel) throws Exception {
		String entry = rowMap.get(rowLabel).get(columnMap.get(columnLabel));
		if (entry.equals("")) {
			System.err.printf("Error:Empty Entry in Parse Table at Row %s Column %s\n", rowLabel, columnLabel);
			throw new Exception();
		} else {
			return entry;
		}
	}
	/**
	 * Show the Parse Table in Console
	 */
	public static void printPT() {
		System.out.println(columnMap);
		System.out.println(rowMap);
	}
	
//	public static void main(String[] args) {
//		ParseTable testing = new ParseTable("parsedata.txt");
//		System.out.println(columnMap);
//		System.out.println(rowMap);
//
//	}
}
