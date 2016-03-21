
/*
 * Simple implementation of a triplet class
 * 
 *Used for easy storage of token properties (type, char value, int value)
 *
 *Class created to be general type, just incase the triplet needs to hold different types of objects
 *In this Program, Triplet is normally used as <String, String, int>, which is specified when it's declared.
 *
 *Contains simple get methods to reference values in the triplet.
 */
public class Triplet<A, B, C> {
	A a;
	B b;
	C c;
	
	//Basic constructor to make the triplet
	Triplet(A a, B b, C c){
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	//Getter Methods
	public A getFirst() {
		return a;
	}
	public B getSecond() {
		return b;
	}
	public C getThird() {
		return c;
	}
	
	@Override
	//represent the triplet as (a, b, c)
	public String toString() {
		return "(" + a + ", " + b + ", " + c + ")";
	}
}