import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;


public class Parser {
	//When the parser reduces, this keeps track of the rules
	private Stack<Integer> rulesStack = new Stack<Integer>();
	//Holds the input to be processed
	private static Queue<String> inputQueue = new LinkedList<String>();
	//before we start, add current state.
	//Stack used for the SLR Parsing
	private static Stack<String> parseStack = new Stack<String>();
	//STstack
	static Stack<Execute> STstack = new Stack<Execute>();

	static {
		//put initial state in parsing Stack
		parseStack.push("0");
	}

	/**
	 * Go through the parseTable and generate the rules
	 * @param file
	 * @param parseTable
	 * @param symbolTable
	 * @param grammar
	 */
	public void parse(String file, ParseTable parseTable, Grammar grammar, SymbolTable symTab) {
		try {
			String currentLine = "";
			//Reads one line at a time
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

			//skip back here when encountering a comment
			nextLine:
				//goes through the program and fills the input queue line by line
				while (((currentLine = bufferedReader.readLine()) != null)) {
					Scanner scanner = new Scanner(new String(currentLine));

					//one word at a time, delimited by whitespace
					while(scanner.hasNext()) {
						String name = scanner.next();
						//Checks for comment. Goes to the next line
						if (name.equals("*-")) {
							continue nextLine;
						}
						//put into the input Queue
						inputQueue.offer(name);
					}
				}
			//append a "EOF" marker after the program
			inputQueue.offer("$");

			//Keep going until no more input
			while (inputQueue.size() != 0) {
//				System.out.println(STstack.toString());
				try {
					Token token = null;
					String nextInputType;
					String nextCharacterValue;
					if (inputQueue.peek().equals("$")) {
						nextInputType = "$";
					}else {
						token = new Token(inputQueue.peek());
						nextInputType = token.getType();
//						System.out.printf("Token is: %s\n", token.toString());
					}
					//type of the name. If can't find in parseTable, this will throw and exception
					String ptEntry = parseTable.getEntry(parseStack.peek(), nextInputType);

					//if it's a shift, put it onto the stack
					if (ptEntry.startsWith("s")) {
						shift(token, ptEntry, symTab);
					}
					//reduce
					if (ptEntry.startsWith("r")) {
						ptEntry = reduce(parseTable, grammar, ptEntry);

					}
					//gets to accept state, print the rules and exit the function
					if (ptEntry.equals("acc")) {
						System.out.println("Correct syntax!");
//						System.out.println(
//								"Filename: " + file + "\n" +
//										"Correct Syntax!\n" +
//										"==============\n\n" + 
//
//								"RULES");
//						showProductions(grammar);
						return;
					}
					//standard error handling
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public String reduce(ParseTable parseTable, Grammar grammar, String ptEntry)
			throws Exception {
		//get the rule Number from grammar
		Integer ruleNumber = Integer.valueOf(ptEntry.substring(1));
		
//		System.out.printf("reducing by: %d\n", ruleNumber);

		//add rule to grammarStack
		rulesStack.push(ruleNumber);

		//We create
		Execute node = makeTree(ruleNumber);
		if (node != null) {
//			System.out.printf("%s -- has been added to STstack\n", node.toString());
			STstack.push(node);
		}
		
		//rule comes back as an array [LHS, RHS]
		String[] rule = grammar.getRule(ruleNumber);

		//pop off two times the number of variables in the rule
		for (int i = 0; i < rule[1].length() * 2; i++) {
			parseStack.pop();
		}
		//this is now the current state
		ptEntry = parseTable.getEntry(parseStack.peek(), rule[0]);

		//push the LHS and the entry onto the stack
		parseStack.push(rule[0]);
		parseStack.push(ptEntry);
		return ptEntry;
	}
	/**
	 * Creates a node in the Syntax tree depending on the Rule used in a LR Parse
	 * @param ruleNumber
	 * @return
	 */
	public Execute makeTree(Integer ruleNumber) {
		switch (ruleNumber) {

		case 1: //P->biiLh
			//backwards because order in Stack is reversed when pushing down. 
			Execute.Sequence seq = (Execute.Sequence) STstack.pop();
			Execute.Reference integer2 = (Execute.Reference) STstack.pop();
			Execute.Reference integer1 = (Execute.Reference) STstack.pop();
			Execute.Begin one = new Execute().new Begin(integer1, integer2, seq);
			return one;
		case 2: //L->S
			Execute t1 = STstack.pop();
			Execute.Sequence two = new Execute().new Sequence(t1);
			return two;
		case 3: //L->LS
			Execute tree2 = STstack.pop();
			Execute tree1 = STstack.pop();
			Execute.Sequence three = new Execute().new Sequence(tree1, tree2);
			return three;
		case 4: //S->rvTT
			Execute.Reference b = (Execute.Reference) STstack.pop(); 
			Execute.Reference a = (Execute.Reference) STstack.pop(); 
			Execute.Reference reference = (Execute.Reference) STstack.pop(); 
			Execute.Robot four = new Execute().new Robot(reference, a, b);
			return four;
		case 5: //S->oTT
			Execute.Obstacle five = new Execute().new Obstacle((Execute.Reference) STstack.pop(), (Execute.Reference) STstack.pop());
			return five;
		case 6: //S->mvDT
			Execute.Reference number = (Execute.Reference) STstack.pop();
			Execute.Direction direction = (Execute.Direction) STstack.pop();
			Execute.Reference v = (Execute.Reference) STstack.pop();
			Execute.Move six = new Execute().new Move(v, direction, number);
			return six;
		case 7: //S->aTtv
			Execute.Reference ref2 = (Execute.Reference) STstack.pop();
			Execute.Reference ref1 = (Execute.Reference) STstack.pop();
			Execute.Add seven = new Execute().new Add(ref1, ref2);
			return seven;
		case 8: //S->v=T
			Execute.Reference r2 = (Execute.Reference) STstack.pop();
			Execute.Reference r1 = (Execute.Reference) STstack.pop();
			Execute.Assign eight = new Execute().new Assign(r1, r2);
			return eight;
		case 9: //S->dLuT>T
			Execute.Reference a2 = (Execute.Reference) STstack.pop();
			Execute.Reference a1 = (Execute.Reference) STstack.pop();
			Execute.Sequence s1 = (Execute.Sequence) STstack.pop();
			Execute.Do nine = new Execute().new Do(s1, a1, a2);
			return nine;
		case 10: //T-> v
			return null;
		case 11: //T -> i 
			return null;
		case 12: //D->n
			return null;
		case 13: //D->s
			return null;
		case 14: //D->e
			return null;
		case 15: //D->w
			return null;
		}
		return null;
	}
	public void shift(Token token, String ptEntry, SymbolTable symTab) {
		
		//if the symbol is in the symbol table, then it should be in the STstack
		if (symTab.containsKey(token.toString())) {
			String nextCharacterValue = symTab.get(token.toString()).getSecond();
			Execute node = new Execute().new Reference(nextCharacterValue);
			STstack.push(node);
		}
		//If the token is a direction, it should also be in the ST stack
		ArrayList<String> cardinal = new ArrayList<String>();
		cardinal.add("east");
		cardinal.add("west");
		cardinal.add("south");
		cardinal.add("north");
		if (cardinal.contains(token.toString())) {
			STstack.push(new Execute().new Direction(token.toString()));
		}
		
		parseStack.push(token.getType());
		parseStack.push(ptEntry.substring(1));
		inputQueue.poll();
	}
	/**
	 * Helper method to show the rules
	 * @param grammar
	 */
	public void showProductions(Grammar grammar) {
		while (rulesStack.size() != 0) {
			Integer ruleNumber = rulesStack.pop();
			String LHS = grammar.getRule(ruleNumber)[0];
			String RHS = grammar.getRule(ruleNumber)[1];
			System.out.printf("(%-2d) %s -> %s\n", ruleNumber, LHS, RHS);
		}
	}
}
