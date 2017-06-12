package tk.smartdrunk.smartdrunk.ID3;

import java.util.*;

public class Hw1 {
	public static int NUM_ATTRS = 5;
	public static ArrayList<String> attrMap;

	public static Tab tab;
	public static User user;
	public static ArrayList<User> users;
	public static ArrayList<Tab> tabs;

	public static ArrayList<User> getUsers() {
		ArrayList<User> users = new ArrayList<User>();
		users.add(new User("000", 20, true, 70));
		users.add(new User("001", 21, false, 67));
		users.add(new User("010", 22, true, 71));
		users.add(new User("011", 20, true, 71));
		users.add(new User("100", 19, true, 67));
		users.add(new User("101", 19, false, 70));
		users.add(new User("110", 20, false, 70));
		users.add(new User("111", 20, true, 71));
		return users;
	}

	public static ArrayList<Tab> getTabs() {
		ArrayList<Tab> tabs = new ArrayList<Tab>();
		tabs.add(new Tab("000", 0.3, true));
		tabs.add(new Tab("000", 0.39, true));
		tabs.add(new Tab("000", 0.29, false));
		tabs.add(new Tab("000", 0.2, true));
		tabs.add(new Tab("001", 0.12, false));
		tabs.add(new Tab("001", 0.39, true));
		tabs.add(new Tab("001", 0.29, false));
		tabs.add(new Tab("001", 0.2, true));
		tabs.add(new Tab("010", 0.3, true));
		tabs.add(new Tab("010", 0.39, true));
		tabs.add(new Tab("010", 0.29, true));
		tabs.add(new Tab("010", 0.2, true));
		tabs.add(new Tab("011", 0.12, true));
		tabs.add(new Tab("011", 0.39, true));
		tabs.add(new Tab("011", 0.29, true));
		tabs.add(new Tab("011", 0.2, true));
		tabs.add(new Tab("100", 0.3, true));
		tabs.add(new Tab("100", 0.39, false));
		tabs.add(new Tab("100", 0.29, false));
		tabs.add(new Tab("100", 0.2, true));
		tabs.add(new Tab("101", 0.12, true));
		tabs.add(new Tab("101", 0.39, true));
		tabs.add(new Tab("101", 0.29, false));
		tabs.add(new Tab("101", 0.2, false));
		tabs.add(new Tab("110", 0.3, true));
		tabs.add(new Tab("110", 0.39, true));
		tabs.add(new Tab("110", 0.29, false));
		tabs.add(new Tab("110", 0.2, true));
		tabs.add(new Tab("111", 0.12, true));
		tabs.add(new Tab("111", 0.39, false));
		tabs.add(new Tab("111", 0.29, false));
		tabs.add(new Tab("111", 0.2, true));
		return tabs;
	}

	public static void printRecords(ArrayList<Record> records) {
		for(Record record : records)
			System.out.println(record);
	}

	public static boolean main(Object[] args) {
		populateAttrMap();

		users = getUsers();
		//System.out.println(users);
		tabs = getTabs();
		//System.out.println(tabs);
		tab = tabs.get(0);
		//System.out.println(tab);
		user = users.get(0);
		//System.out.println(user);

		Tree t = new Tree();
		ArrayList<Record> records;

		// read in all our data
		records = InputHandler.buildRecords(user.weight, user.age, users, tabs);
		// printRecords(records);

		Node root = new Node();

		for(Record record : records)
			root.getData().add(record);

		t.buildTree(records, root);
		root.print();
		System.out.println();

		for (Record r : records) {
			System.out.println(r + " ");
			System.out.println("Prediction for Play Tennis: " + (traverseTree(r, root) ? "Yes" : "No"));
		}
		Record r = (Record) args[2];
		return (traverseTree(r, root));
	}

	public static boolean traverseTree(Record r, Node root) {
		if (root.children == null)
			return root.playTennis();

		for(int i = 0; i < r.getAttributes().size(); i++)
			if(r.getAttributes().get(i).getName().equalsIgnoreCase(root.getTestAttribute().getName()))
				return traverseTree(r, root.children[(int) r.getAttributes().get(i).getValue()]);

		throw new IllegalStateException();
	}

	public static int setSize(String set) {
		if(set.equalsIgnoreCase("BAC")) return 8;
		else if(set.equalsIgnoreCase("Weight")) return 15;
		else if(set.equalsIgnoreCase("Age")) return 8;
		else if(set.equalsIgnoreCase("Genger")) return 2;
		else if(set.equalsIgnoreCase("Hangover")) return 2;
		return 0;
	}

	public static String getLeafNames(int attributeNum, int valueNum) {
		switch (attributeNum) {
		case 0:
			switch (valueNum) {
		    case 0: return "BAC0";	// "0.001�0.029"
		    case 1: return "BAC1";	// "0.030�0.059"
		    case 2: return "BAC2";	// "0.060�0.099"
		    case 3: return "BAC3";	// "0.100�0.199"
		    case 4: return "BAC4";	// "0.200�0.299"
		    case 5: return "BAC5";	// "0.300�0.399"
		    case 6: return "BAC6";	// "0.400�0.499"
		    case 7: return "BAC7";	// ">0.50"
			}
		case 1:
			// return Integer.toString(valueNum + user.weight - 7);
			switch (valueNum) {
		    case 0: return "Weight0";
		    case 1: return "Weight1";
		    case 2: return "Weight2";
		    case 3: return "Weight3";
		    case 4: return "Weight4";
		    case 5: return "Weight5";
		    case 6: return "Weight6";
		    case 7: return "Weight7";
		    case 8: return "Weight8";
		    case 9: return "Weight9";
		    case 10: return "Weight10";
		    case 11: return "Weight11";
		    case 12: return "Weight12";
		    case 13: return "Weight13";
		    case 14: return "Weight14";
			}
		case 2:
			// return Integer.toString(valueNum + user.age - 3);
			switch (valueNum) {
		    case 0: return "Age0";
		    case 1: return "Age1";
		    case 2: return "Age2";
		    case 3: return "Age3";
		    case 4: return "Age4";
		    case 5: return "Age5";
		    case 6: return "Age6";
			}
		case 3:
			return valueNum == 0 ? "Female" : "Male";
		default:
			return null;
		}
	}

	public static void populateAttrMap() {
		attrMap = new ArrayList<String>();
		attrMap.add("BAC");			// #0
		attrMap.add("Weight");		// #1
		attrMap.add("Age");			// #2
		attrMap.add("Genger");		// #3
		attrMap.add("Hangover");	// #4
	}
}
