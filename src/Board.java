import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class Board {
	String[][] board;
	HashMap<String, Bot> allBots = new HashMap<String, Bot>();
	ArrayList<Integer[]> allObstacles = new ArrayList<Integer[]>();
	
	/**
	 * Creates the board that is +1 the parameters given, since the board will start at 0,0 at the top left
	 * @param integer1
	 * @param integer2
	 */
	public Board(Integer integer1, Integer integer2) {
		int x = integer1 + 1;
		int y = integer2 + 1; 
		board = new String[x][y];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = ".";
			}
		}
		System.out.println("Board Created");
	}
	/**
	 * Add a robot with "name". Represented on the board with the capital letter
	 * @param name
	 * @param x
	 * @param y
	 */
	public void addRobot(String name, Integer x, Integer y) {
		Bot robot = new Bot(name, x, y);
		allBots.put(name, robot);
		board[y][x] = robot.Name.substring(0, 1).toUpperCase();
	}
	/**
	 * Representation of the board for the console
	 */
	public String toString() {
		return Arrays.deepToString(board).replaceAll("(, )|(\\[\\[)|(\\]\\])", "").replaceAll("(\\]\\[)", "\n") + "\n";
	}

	/**
	 * Add obstacle to the board represented by "*" on the board
	 * @param x
	 * @param y
	 */
	public void addObstacle(Integer x, Integer y) {
		Integer[] obstacle = {x, y};
		allObstacles.add(obstacle);
		board[y][x] = "*";
	}


	/**
	 * Move the robot in a certain direction
	 * 
	 * Checks whether or not there the robot is off the board or not. As well as if there is an
	 * Obstacle in the way. The program will also show the path of the robot, represented by lowercase
	 * first letters of the robot name. 
	 * @param name
	 * @param direction
	 * @param i
	 */
	public void moveRobot(String name, String direction, Integer i) {
		Bot robot = allBots.get(name);
		board[robot.yPos][robot.xPos] = ".";
		if (direction.equals("east")) {
			for (int j = robot.xPos; j < robot.xPos + i + 1; j++) {
				for (int k = 0; k < allObstacles.size(); k++) {
					Integer[] robotPos = {j, robot.yPos};
					
					//draw the path
					board[robot.yPos][j] = robot.Name.substring(0, 1).toLowerCase();
					if (Arrays.equals(allObstacles.get(k), robotPos)) {
						System.err.printf("Robot Hit Obstacle at %s\n", Arrays.toString(robotPos));
						throw new IllegalStateException();
					}
					
				}
			}
			robot.xPos = robot.xPos + i;
		}
		if (direction.equals("west")) {
			for (int j = robot.xPos - i; j < robot.xPos + 1; j++) {
				for (int k = 0; k < allObstacles.size(); k++) {
					Integer[] robotPos = {j, robot.yPos};
					board[robot.yPos][j] = robot.Name.substring(0, 1).toLowerCase();
					if (Arrays.equals(allObstacles.get(k), robotPos)) {
						System.err.printf("Robot Hit Obstacle at %s\n", Arrays.toString(robotPos));
						throw new IllegalStateException();
					}
					
				}
			}
			robot.xPos = robot.xPos - i;
		}
		if (direction.equals("north")) {
			for (int j = robot.yPos-i; j < robot.yPos + 1; j++) {
				for (int k = 0; k < allObstacles.size(); k++) {
					Integer[] robotPos = {robot.xPos, j};
					board[j][robot.xPos] = robot.Name.substring(0, 1).toLowerCase();
					if (Arrays.equals(allObstacles.get(k), robotPos)) {
						System.err.printf("Robot Hit Obstacle at %s\n", Arrays.toString(robotPos));
						throw new IllegalStateException();
					}
					
				}
			}
			robot.yPos = robot.yPos - i;
		}
		if (direction.equals("south")) {
			for (int j = robot.yPos; j < robot.yPos + i + 1; j++) {
				for (int k = 0; k < allObstacles.size(); k++) {
					Integer[] robotPos = {robot.xPos, j};
					board[j][robot.xPos] = robot.Name.substring(0, 1).toLowerCase();
					if (Arrays.equals(allObstacles.get(k), robotPos)) {
						System.err.printf("Robot Hit Obstacle at %s\n", Arrays.toString(robotPos));
						throw new IllegalStateException();
					}
				}
			}
			robot.yPos = robot.yPos + i;
		}
		
		//put the final position of the robot on the board
		try {
			board[robot.yPos][robot.xPos] = robot.Name.substring(0, 1).toUpperCase();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Robot is off the board");
			e.printStackTrace();
		}
	}
}
