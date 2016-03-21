import java.util.Scanner;

//This class is the basic structure for running a GBOT program.
//Only has a main method to run the program.

public class GBOT {
	public static void main(String[] args) {
		//Drive object
		Drive test = new Drive();
		
		//Smart Scan is turned on
//		test.toggleSmartScan();
		
		//System input
		Scanner sc = new Scanner(System.in);
		System.out.print("Name of file? (Press enter for default): ");
		String fileName = sc.nextLine();
		//gives a default file to put in for easier testing
		fileName = fileName.equals("") ? "simple1.gb" : fileName;
		
		//Make the symbol Table
		SymbolTable st = test.convertFileToST(fileName);
		
		//make the ParseTable
		ParseTable pt = new ParseTable("parsedata.txt");
		
		//make the Parser
		Parser parser = new Parser();
		
		//make the Grammar
		Grammar grammar = new Grammar("grammarRules.txt");
		
		//Parse for productions
		parser.parse(fileName, pt, grammar, st);
		
		//Store the Syntax Tree
		Execute.Begin syntaxTree = (Execute.Begin) Parser.STstack.peek();
		
		//Create an Object to run the program
		Execute execute = new Execute(syntaxTree, st);
		
		//Run the Program
		execute.start();
		
		//Write the final symbolTable to file
		st.writeSTtoFile("symboltable.txt");
	}
}
