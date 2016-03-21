import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

/* This class is the driver. This has a method to loop through the file, call a scanner class and make tokens
 * of each of the strings returned by the scanner.
 * 
 * The string is appropriately printed to either the console or stored in the symbol table.
 * 
 * The scanner class called is the Scanner class from the Java API Library.
 * 
 */
public class Drive {
	//Toggles the Extra Credit: breaking invalid tokens into valid tokens
	boolean smartScan = false;
	
	//Short method that changes smartScan from falst to true or true to false.
	public void toggleSmartScan() {
		this.smartScan = (smartScan == false) ? true : false;
	}
	
	//Method converts a GBOT program into a symbol table.
	public SymbolTable convertFileToST(String fileName) {
		SymbolTable st = new SymbolTable();
		String currentLine = null;
		
		//keep track of line number
		int lineNum = 1;
		
		try {
			//Initialize the output to the console to show what we're doing
//			System.out.println("Processing");
//			String formatStr = "%-5s %-10s %-10s\n";
//			System.out.printf(formatStr, "TYPE", "CH VALUE", "INT VALUE");
//			System.out.printf(formatStr, "====", "========", "=========");
			
			BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
			//Reads one line at a time
			nextLine:
			while ((currentLine = bufferedReader.readLine()) != null) {
				Scanner scanner = new Scanner(new String(currentLine));
				
				//one word at a time, delimited by whitespace
				while(scanner.hasNext()) {
					String name = scanner.next();
					
					//Checks for comment. Goes to the next line
					if (name.equals("*-")) {
						
						//If end comments need to be detected, this regular expression would be 
						//uncommented
						
//						if (!currentLine.matches("[\\s\\S]*\\*-[\\s\\S]*-\\*[\\s\\S]*")) {
//							System.err.printf("Missing end comment on line %d\n", lineNum);
//						}
						lineNum++;
						continue nextLine;
					}
					
					//Initialize the Token object
					Token token = new Token();
					try { //try to make a token out of the given string
						token = new Token(name);
						
//						Store the information in the form of a triplet
						Triplet<String, String, Integer> info =
								new Triplet<String, String, Integer>(token.getType(), token.getChvalue(), token.getIntvalue());
						
						//if it's a valid, and should be in the symboltable, it makes an entry for it in the symbol Table
						if (!st.containsKey(name) && info.getSecond() != null) {
							st.put(name, info);
						}
						
						// If it is not in the symbol table, print only the type
//						if (token.getChvalue() == null) {
//							System.out.printf("%-5s\n", token.getType());
//						} else { //If it is in the symbol table, then print all three values.
//							System.out.printf("%-5s %-10s %-10d\n", token.getType(), token.getChvalue(), token.getIntvalue());
//						}
						
						//If not a valid token, it will throw an exception
					} catch (Exception e) {
						//if smartscan is turn on, it will tyr to make valid tokens of the invalid token
						if (smartScan) {
							//Tells you the line number of invalid token
							System.err.printf("on line %d\n", lineNum);	
							//make a list of all the possible tokens
							ArrayList<Token> tokens = new ArrayList<Token>(token.smartParse(name));
							//iterator to loop through the possible tokens
							Iterator<Token> iterator = tokens.iterator();
							while (iterator.hasNext()) {
								Token possibleTokens = iterator.next();
								//makes the info triplet for the new token
								Triplet<String, String, Integer> info =
										new Triplet<String, String, Integer>(
												possibleTokens.getType(), possibleTokens.getChvalue(), possibleTokens.getIntvalue());
								//puts into symbol table if variable or integer and not already in the symbol table
								if (!st.containsKey(possibleTokens.getName()) && info.getSecond() != null) {
									st.put(possibleTokens.getName(), info);
								}
								//decides the format of what should be printed to the console
								if (possibleTokens.getChvalue() == null) {
									System.out.printf("%-5s\n", possibleTokens.getType());
								} else {
									System.out.printf("%-5s %-10s %-10d\n", 
											possibleTokens.getType(), possibleTokens.getChvalue(), possibleTokens.getIntvalue());
								}
							}
						} else {
							//line of invalid token
							System.err.printf("on line %d\n", lineNum);							
						}
					}
					
				} scanner.close();
				lineNum++;
			}bufferedReader.close();
		}
		catch (IOException e) {
			System.err.println("An error occured reading file: " + e);
			e.printStackTrace();
		}
		//simply print the table for reference,
		st.printTable();
		return st;		
	}
}
