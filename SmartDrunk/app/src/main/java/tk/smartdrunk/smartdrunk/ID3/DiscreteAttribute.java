package tk.smartdrunk.smartdrunk.ID3;

public class DiscreteAttribute extends Attribute {

	// #0
	 // BAC (% by vol.)
	public static final int BAC0 = 0;	// 0.001�0.029
	public static final int BAC1 = 1;	// 0.030�0.059
	public static final int BAC2 = 2;	// 0.060�0.099
	public static final int BAC3 = 3;	// 0.100�0.199
	public static final int BAC4 = 4;	// 0.200�0.299
	public static final int BAC5 = 5;	// 0.300�0.399
	public static final int BAC6 = 6;	// 0.400�0.499
	public static final int BAC7 = 7;	// >0.50

	// #1
	// Range: [User's weight - 7, User's weight + 7]
	public static final int Weight0 = 0;
	public static final int Weight1 = 1;
	public static final int Weight2 = 2;
	public static final int Weight3 = 3;
	public static final int Weight4 = 4;
	public static final int Weight5 = 5;
	public static final int Weight6 = 6;
	public static final int Weight7 = 7; // User's weight
	public static final int Weight8 = 8;
	public static final int Weight9 = 9;
	public static final int Weight10 = 10;
	public static final int Weight11 = 11;
	public static final int Weight12 = 12;
	public static final int Weight13 = 13;
	public static final int Weight14 = 14;

	// #2
	// Range: [User's age - 3, User's age + 3]
	public static final int Age0 = 0;
	public static final int Age1 = 1;
	public static final int Age2 = 2;
	public static final int Age3 = 3; // User's age
	public static final int Age4 = 4;
	public static final int Age5 = 5;
	public static final int Age6 = 6;

	// #3
	public static final int Female = 0;
	public static final int Male = 1;
	
	// #4
	public static final int HangoverNo = 0;
	public static final int HangoverYes = 1;

	public DiscreteAttribute(String name, double value) {
		super(name, value);
	}

	public DiscreteAttribute(String name, String value) {
		super(name, value);
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}
