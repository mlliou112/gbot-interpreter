import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/*
 * Class for a Token Object. The Driver class will try to make a token object out of the strings that the
 * Scanner class passes the driver. 
 */

public class Token {
	
	//Instance Variables
	private String name = null;
	private String type = null;
	private String chvalue = null;
	private int intvalue = 0;
	//Array of the Keywords that the program should recognize
	private static final String[] keywords = {      //keywords and punctuation
		"begin", "halt", "obstacle", "add", "to",
		"move", "north", "south", "east", "west",
		"robot", "do", "until", "=", ";", ">"};
	private static final Map<String,String> typeMap =  //Map to store types
			new HashMap<String,String>();
	
	//Static initialization of map such that the each keyword is mapped to the 
	//its own first letter.
	static
	{
		for (int i = 0; i < keywords.length; i++) {
			typeMap.put(keywords[i], keywords[i].substring(0, 1));
		}
	}
	
	//Basic Constructor for Token Object
	public Token() {

	}
	
	//Constructor to make a token object from string, automatically assigns chvalue and intvalue
	//Covers keywords, punctuation, variables, and integers
	//Will throw exception if Invalid Token
	public Token(String string) throws Exception {
		this.name = string.toLowerCase();

		//for Keyword or Punctuation
		if (typeMap.containsKey(name)) {
			this.type = typeMap.get(name);
			this.chvalue = null;
			//for integer
		} else if(name.matches("0|[1-9]\\d{0,7}")) {
			this.type = "i";
			this.chvalue = name;
			this.intvalue = Integer.parseInt(name);
			//for variables
		} else if(name.matches("[A-Za-z]{1,8}")) {
			this.type ="v";
			this.chvalue = name;
			this.intvalue = 0;
		} else {
			//error message
			System.err.printf("Invalid Token: \"%s\" ", name);
			throw new Exception();
		}
	}

	//getters and setters for all instance variables
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	public String getChvalue() {
		return chvalue;
	}
	public int getIntvalue() {
		return intvalue;
	}
	
	//when printing the string, just show the name of the token
	@Override
	public String toString() {
		return name;
	}
	
	//EXTRA CREDIT IS IMPLEMENTED IN THIS METHOD!!!
	//This method parses the invalid token looking for valid tokens
	//returns a list of valid tokens in the string
	public List<Token> smartParse(String string) {
		String stringcopy = string;
		List<Token> result = new ArrayList<Token>();
		
		//double for loop to go through all substrings
		//first for loop goes forward through string
		for (int j = 0; j < stringcopy.length(); j++) {
			//eliminates one character off the back each time
			for (int i = stringcopy.length(); i > j; i--) {
//				System.out.printf("(%d,%d)\n", i, j);
				
				//substring variable to check if token
				String possibleToken = stringcopy.substring(j, i);
				
				//checks if keyword or punctuation
				if (typeMap.containsKey(possibleToken)) {
					try {
						result.add(new Token(possibleToken));
						//replaces token so it doesn't get double counted
						stringcopy = stringcopy.replaceFirst(possibleToken, "");
						//restart forloop for new string (+1 because it will -1 at end)
						i = stringcopy.length() + 1;
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					//checks if integer
				} else if(possibleToken.matches("0|[1-9]\\d{0,7}")) {
					try { // same try/catch block as above
						result.add(new Token(possibleToken));
						stringcopy = stringcopy.replaceFirst(possibleToken, "");
						i = stringcopy.length() + 1;
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					//checks if variable
				} else if(possibleToken.matches("[A-Za-z]{1,8}")) {
					try { // same try/catch block as above
						result.add(new Token(possibleToken));
						stringcopy = stringcopy.replaceFirst(possibleToken, "");
						i = stringcopy.length() + 1;
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					continue;
				}
			}
		}
		
		//Uses helper method to see whether or not they are all variables
		//If so, it means that it was just one long variable and should be an
		//error, or if it was just one large number.
		boolean allSame = false;
		if (result.size() > 0) {
		allSame = checkSameType(result);
		}
		if (!allSame) {
			result.clear();
		} else {
			//give the user a warning of what has been split
			System.err.println("Warning: \"" + string + "\" has been split into tokens:");
			System.err.println(result.toString());			
		}
		
		//if there are tokens left, they're invalid and the user is notified.
		if (stringcopy.length() !=0) {
			System.err.printf("Invalid Tokens after smartParse: %s\n", stringcopy);			
		}
		
//		System.out.println(result.toString());
		
		//If it was a just a long variable, it will return null
		//otherwise returns the list of tokens
		return result;
	}

	//Helper Method for Extra Credit to see if it they are all variables or not
	private boolean checkSameType(List<Token> result) {
		boolean allSameType = false;
		String type = result.get(0).getType();
		
		//This is a basic check to make sure the smart scan doesn't split up variables that were just too long
		//or integers that were too long. (in other words, things that should just be noted as invalid)
		for (int i = 0; i < result.size(); i++) {
			if (result.get(i).getType() != type)
				allSameType = true;
		}
		return allSameType;
	}

}

