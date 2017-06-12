package tk.smartdrunk.smartdrunk.ID3;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class InputHandler {
	
	public static ArrayList<Record> buildRecords(int weight, int age, ArrayList<User> users, ArrayList<Tab> tabs) {
		
		ArrayList<Record> records = new ArrayList<Record>();
    	
    	for (Tab tab : tabs)
    		records.add(buildRecord(weight, age, tab, findUser(users, tab.uid)));
    	
		return records;
	}
	
	public static Record buildRecord(int weight, int age, Tab tab, User user) {
		Record r = new Record();
		ArrayList<DiscreteAttribute> attributes = new ArrayList<DiscreteAttribute>();
		String attr;
		
		attr = "BAC";
		double bac = tab.bac;
		if (0.001 <= bac && bac <= 0.029) attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.BAC0));
		else if (0.030 <= bac && bac <= 0.059) attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.BAC1));
		else if (0.060 <= bac && bac <= 0.099) attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.BAC2));
		else if (0.100 <= bac && bac <= 0.199) attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.BAC3));
		else if (0.200 <= bac && bac <= 0.299) attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.BAC4));
		else if (0.300 <= bac && bac <= 0.399) attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.BAC5));
		else if (0.400 <= bac && bac <= 0.499) attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.BAC6));
		else if (0.50 <= bac) attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.BAC7));
		
		attr = "Weight";
		switch (user.weight - weight + 7) {
		  case 0: attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.Weight0)); break;
          case 1: attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.Weight1)); break;
          case 2: attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.Weight2)); break;
          case 3: attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.Weight3)); break;
          case 4: attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.Weight4)); break;
          case 5: attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.Weight5)); break;
          case 6: attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.Weight6)); break;
          case 7: attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.Weight7)); break;
          case 8: attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.Weight8)); break;
          case 9: attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.Weight9)); break;
          case 10: attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.Weight10)); break;
          case 11: attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.Weight11)); break;
          case 12: attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.Weight12)); break;
          case 13: attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.Weight13)); break;
          case 14: attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.Weight14)); break;
		}
		
		attr = "Age";
		switch (user.age - age + 3) {
		  case 0: attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.Age0)); break;
          case 1: attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.Age1)); break;
          case 2: attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.Age2)); break;
          case 3: attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.Age3)); break;
          case 4: attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.Age4)); break;
          case 5: attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.Age5)); break;
          case 6: attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.Age6)); break;
		}
		
		attr = "Genger";
		if(!user.gender) attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.Female));
		else if(user.gender) attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.Male));
		
		attr = "Hangover";
		if(!tab.hangover) attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.HangoverNo));
		else if(tab.hangover) attributes.add(new DiscreteAttribute(attr, DiscreteAttribute.HangoverYes));
		
		r.setAttributes(attributes);
		return r;
	}
	
	public static User findUser(ArrayList<User> users, String uid) {
	    for (User u : users)
	        if (u.uid.equals(uid))
	        	return u;
	    throw new NoSuchElementException();
	}
	
}
