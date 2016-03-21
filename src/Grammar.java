import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Grammar {
	//Grammar represented with an ArrayList of String[]
	private ArrayList<String[]> grammarList = new ArrayList<String[]>();

	/**
	 * Constructor for the grammar from a file
	 * 
	 * Reads the grammar rules write in format LHS->RHS
	 * @param fileName
	 */
	Grammar(String fileName){
		try {
			//Standard file opening
			FileReader temp = new FileReader(fileName);
			BufferedReader nottemp = new BufferedReader(temp);

			String currentLine = "";
			//Get each line
			while((currentLine  = nottemp.readLine()) != null) {
				//Split each line and add to the List
				grammarList.add(currentLine.split("->"));
			}
		} catch (IOException e) {
			//std error for file reading
			e.printStackTrace();
		}

	}

	/**
	 * Gets corresponding grammar rule
	 * @param i
	 * @return
	 */
	public String[] getRule(Integer i) {
		return grammarList.get(i);
	}
	public static void main(String[] args) {
		Grammar test = new Grammar("grammarrules.txt");
		System.out.println(Arrays.toString(test.getRule(0)));
	}

}
