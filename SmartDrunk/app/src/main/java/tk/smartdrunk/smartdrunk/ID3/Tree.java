package tk.smartdrunk.smartdrunk.ID3;

import java.util.*;

/**
 * Tree.java � This tree class contains all our code for building our decision tree.
 * Note that each level, we choose the attribute that presents the best gain for that node.
 * The gain is simply the expected reduction in the entropy of X achieved by learning the state of the random variable A.
 * Gain is also known as Kullback-Leibler divergence.
 * Gain can be calculated in the following way: Gain(T, A) = Entropy(T) - sum (|T_v| / |T|) * Entropy(T_v) for each v in Values(A)
 * Notice that gain is calculated as a function of all the values of the attribute.
 * @author David Stites
 * @category Machine Learning
 * @category Software
 * 
 */
public class Tree {
	public Node buildTree(ArrayList<Record> records, Node root) {
		int i, j, bestAttribute = -1;
		double bestGain = 0;
		root.setEntropy(Entropy.calculateEntropy(root.getData()));
		
		if(root.getEntropy() == 0)
			return root;
		
		for(i = 0; i < Hw1.NUM_ATTRS - 1; i++) {
			if(!root.isAttributeUsed(i)) {
				double entropy = 0;
				ArrayList<Double> entropies = new ArrayList<Double>();
				ArrayList<Integer> setSizes = new ArrayList<Integer>();
				
				for(j = 0; j < Hw1.NUM_ATTRS - 1; j++) {
					ArrayList<Record> subset = subset(root, i, j);
					setSizes.add(subset.size());
					
					if(subset.size() != 0) {
						entropy = Entropy.calculateEntropy(subset);
						entropies.add(entropy);
					}
				}
				
				double gain = Entropy.calculateGain(root.getEntropy(), entropies, setSizes, root.getData().size());
				
				if(gain > bestGain) {
					bestAttribute = i;
					bestGain = gain;
				}
			}
		}
		if(bestAttribute != -1) {
			int setSize = Hw1.setSize(Hw1.attrMap.get(bestAttribute));
			root.setTestAttribute(new DiscreteAttribute(Hw1.attrMap.get(bestAttribute), 0));
			root.children = new Node[setSize];
			root.setUsed(true);
			root.addUsedAttribute(bestAttribute);
			
			for (j = 0; j < setSize; j++) {
				root.children[j] = new Node();
				root.children[j].setParent(root);
				root.children[j].setData(subset(root, bestAttribute, j));
				root.children[j].getTestAttribute().setName(Hw1.getLeafNames(bestAttribute, j));
				root.children[j].getTestAttribute().setValue(j);
				
				root.children[j].setUsedAttributes(root.usedAttributes);
			}

			for (j = 0; j < setSize; j++)
				buildTree(root.children[j].getData(), root.children[j]);

			root.setData(null);
		}
		
		return root;
	}
	
	public ArrayList<Record> subset(Node root, int attr, int value) {
		ArrayList<Record> subset = new ArrayList<Record>();
		
		for(int i = 0; i < root.getData().size(); i++) {
			Record record = root.getData().get(i);
			
			if(record.getAttributes().get(attr).getValue() == value)
				subset.add(record);
		}
		return subset;
	}
	
	public double calculateSurrogates(ArrayList<Record> records) {
		return 0;
		
	}
	
	public void pruneTree() {
		
	}
}
