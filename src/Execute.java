

public class Execute {
	static Execute.Begin syntaxTree;
	static SymbolTable st;
	static Board board;
	
	public Execute() {
	}
	public Execute(Execute.Begin syntaxTree, SymbolTable st) {
		Execute.syntaxTree = syntaxTree;
		Execute.st = st;
	}
	public void start() {
		syntaxTree.run();
	}
	public void run() {
		System.out.println(board.toString());
	}
	
	public String toString() {
		return "Execute Object";
	}
	public class Reference extends Execute {
		String myValue;

		public Reference(String ref) {
			this.myValue = ref;
		}
		public Integer getIntValue() {
			return st.get(myValue).getThird();
		}
		public String toString() {
			return "Reference: " + myValue;
		}
		
	}
	public class Begin extends Execute {
		// begin i j <list> halt
		Reference integer1;
		Reference integer2;
		Sequence sequence;
		
		public Begin(Reference i1, Reference i2, Sequence s) {
			this.integer1 = i1;
			this.integer2 = i2;
			this.sequence = s;
		}
		
		
		@Override
		public void run() {
			Execute.board = new Board(integer1.getIntValue(), integer2.getIntValue());
			System.out.println(board.toString());
			sequence.run();
		}


		@Override
		public String toString() {
			return "Begin [integer1=" + integer1.toString() + ", integer2=" + integer2.toString()
					+ ", sequence=" + sequence.toString() + "]";
		}
		
		
	}
	
	public class Robot extends Execute {
		// robot v a b
		Reference variable;
		Reference a;
		Reference b;
		
		public Robot(Reference variable, Reference a, Reference b) {
			this.variable = variable;
			this.a = a;
			this.b = b;
		}

		public void run() {
			board.addRobot(variable.myValue, a.getIntValue(), b.getIntValue());
			System.out.printf("Robot \"%s\" added at %d,%d\n", variable.myValue, a.getIntValue(), b.getIntValue());
			super.run();
		}
		@Override
		public String toString() {
			return "Robot [variable=" + variable + ", a=" + a + ", b=" + b
					+ "]";
		}
		
		
	}
	public class Obstacle extends Execute {
		// obstacle a b 
		Reference a;
		Reference b;
		
		public Obstacle(Reference a, Reference b) {
			this.a = b;
			this.b = a;
		}

		@Override
		public void run() {
			board.addObstacle(a.getIntValue(), b.getIntValue());
			System.out.printf("Obstacle Added at %d,%d\n", a.getIntValue(), b.getIntValue());
			super.run();
		}
		@Override
		public String toString() {
			return "Obstacle [a=" + a + ", b=" + b + "]";
		}
		
	}
	public class Sequence extends Execute {
		//seq
		Execute syntaxTree1;
		Execute syntaxTree2;
		
		public Sequence(Execute tree1, Execute tree2) {
			this.syntaxTree1 = tree1;
			this.syntaxTree2 = tree2;
		}

		public Sequence(Execute tree1) {
			this.syntaxTree1 = tree1;
		}
		
		public void run() {
			syntaxTree1.run();
			if (syntaxTree2 != null) {
				syntaxTree2.run();
			}
		}
		@Override
		public String toString() {
			return "Sequence [syntaxTree1=" + syntaxTree1 + ", syntaxTree2="
					+ syntaxTree2 + "]";
		}
		
		
		
	}
	public class Add extends Execute {
		//add a to v
		Reference a;
		Reference variable;
		
		public Add(Reference a, Reference v) {
			this.a = a;
			this.variable = v;
		}
		@Override
		public void run() {
			Integer original = st.get(variable.myValue).getThird();
			Integer updated = original + a.getIntValue();
			st.updateIntValue(variable.myValue, updated);
			System.out.printf("\"%s\" changed from %d to %d\n",variable.myValue, original, updated );
		}
		@Override
		public String toString() {
			return "Add [a=" + a + ", variable=" + variable + "]";
		}
		
	}
	//move v d a
	public class Move extends Execute {
		Reference v;
		Direction dir;
		Reference a;
		
		public Move(Reference v, Direction dir, Reference a) {
			this.v = v;
			this.dir = dir;
			this.a = a;
		}
		
		@Override
		public void run() { 
			board.moveRobot(v.myValue, dir.direction, a.getIntValue());
			System.out.printf("move: %s %s by %d\n", v.myValue, dir.direction, a.getIntValue());
			super.run();
		}
		
		@Override
		public String toString() {
			return "Move [v=" + v + ", dir=" + dir + ", a=" + a + "]";
		}
		
	}
	public class Assign extends Execute {
		//v = a
		Reference v;
		Reference a;
		
		public Assign(Reference v, Reference a) {
			this.v = v;
			this.a = a;
		}
		
		@Override
		public void run() {
			st.updateIntValue(v.myValue, a.getIntValue());
			System.out.printf("\"%s\" assigned value %d\n", v.myValue, a.getIntValue());
		}

		@Override
		public String toString() {
			return "Assign [v=" + v + ", a=" + a + "]";
		}
		
	}
	//do <statements> until a > b
	public class Do extends Execute {
		Execute syntaxTree1;
		Reference a;
		Reference b;
		
		public Do(Execute syntaxTree1, Reference a, Reference b) {
			this.syntaxTree1 = syntaxTree1;
			this.a = a;
			this.b = b;
		}
		/**
		 * run the statements while a condition is true
		 */
		@Override
		public void run() {
			do {
				syntaxTree1.run();
			} while (a.getIntValue() <= b.getIntValue());
		}
	}
	public class Direction extends Execute {
		String direction;
		
		public Direction(String direction) {
			if (direction.equals("east")) {
				this.direction = "east";
			}
			else if (direction.equals("west")) {
				this.direction = "west";
			}
			else if (direction.equals("south")) {
				this.direction = "south";
			}
			else if (direction.equals("north")) {
				this.direction = "north";
			}
			else {
				this.direction = null;
			}
		}
		@Override
		//A direction statement doesn't need to do anything
		public void run() {
			return;
		}
		
		@Override
		public String toString() {
			return "Direction [direction=" + direction + "]";
		}	
	}
}
