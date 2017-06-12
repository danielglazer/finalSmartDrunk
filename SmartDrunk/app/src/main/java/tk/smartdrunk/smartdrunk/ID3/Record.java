package tk.smartdrunk.smartdrunk.ID3;

import java.util.*;

public class Record {
	private ArrayList<DiscreteAttribute> attributes;

	public ArrayList<DiscreteAttribute> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(ArrayList<DiscreteAttribute> attributes) {
		this.attributes = attributes;
	}
	
	@Override
	public String toString() {
		return attributes.toString();
	}
}
