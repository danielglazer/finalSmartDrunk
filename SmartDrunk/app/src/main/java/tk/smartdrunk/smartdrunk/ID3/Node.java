package tk.smartdrunk.smartdrunk.ID3;

import java.util.*;

/**
 * Node.java � Node.java holds our information in the tree.
 * @author David Stites
 * @author Alon Dankner
 * @category Machine Learning
 * @category Software
 *
 */
public class Node {
	private Node parent;
	public Node[] children;
	private ArrayList<Record> data;
	private double entropy;
	private boolean isUsed;
	private DiscreteAttribute testAttribute;
	
	public ArrayList<Integer> usedAttributes;

	public Node() {
		this.data = new ArrayList<Record>();
		setEntropy(0.0);
		setParent(null);
		setChildren(null);
		setUsed(false);
		setTestAttribute(new DiscreteAttribute("", 0));
		
		this.usedAttributes = new ArrayList<Integer>();
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Node getParent() {
		return parent;
	}

	public void setData(ArrayList<Record> data) {
		this.data = data;
	}

	public ArrayList<Record> getData() {
		return data;
	}

	public void setEntropy(double entropy) {
		this.entropy = entropy;
	}

	public double getEntropy() {
		return entropy;
	}

	public void setChildren(Node[] children) {
		this.children = children;
	}

	public Node[] getChildren() {
		return children;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setTestAttribute(DiscreteAttribute testAttribute) {
		this.testAttribute = testAttribute;
	}

	public DiscreteAttribute getTestAttribute() {
		return testAttribute;
	}
	
	public void setUsedAttributes(ArrayList<Integer> usedAttributes) {
		this.usedAttributes.addAll(usedAttributes);
	}
	
	public void addUsedAttribute(int attribute) {
		this.usedAttributes.add(attribute);
	}
	
	public boolean isAttributeUsed(int attribute) {
		return this.usedAttributes.contains(attribute);
	}
	
	public void print() {
        print("", true);
    }

    private void print(String prefix, boolean isTail) {
    	System.out.println(prefix + (isTail ? "L--- " : "|--- ") + this.testAttribute.getName());
        
        if (children != null) {
        	for (int i = 0; i < children.length - 1; i++)
            	children[i].print(prefix + (isTail ? "    " : "|   "), false);
            
            if (children.length > 0)
            	children[children.length - 1].print(prefix + (isTail ? "    " : "|   "), true);
        }
    }
    
    public boolean playTennis() {
    	int count = 0;
        for (Record record : data)
            if(record.getAttributes().get(Hw1.NUM_ATTRS - 1).getValue() == 0)
                count++;
		return (data.size() - count) >= count;
	}
}
