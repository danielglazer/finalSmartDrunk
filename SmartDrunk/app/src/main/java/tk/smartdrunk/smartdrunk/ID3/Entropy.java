package tk.smartdrunk.smartdrunk.ID3;

import java.util.ArrayList;

/**
 * Entropy.java � In Entropy.java, we are concerned with calculating the amount of entropy, or the amount of uncertainty or randomness with a particular variable.
 * For example, consider a classifier with two classes, YES and NO.
 * If a particular variable or attribute, say x has three training examples of class YES and three training examples of class NO (for a total of six), the entropy would be 1.
 * This is because there is an equal number of both classes for this variable and is the most mixed up you can get.
 * Likewise, if x had all six training examples of a particular class, say YES, then entropy would be 0 because this particular variable would be pure, thus making it a leaf node in our decision tree.
 * Entropy may be calculated in the following way: Entropy(T) = - p(+) log_2 P(+) - p(+) log_2 P(+)
 * @author David Stites
 * @category Machine Learning
 * @category Software
 *
 */
public class Entropy {	
	public static double calculateEntropy(ArrayList<Record> data) {
		double entropy = 0;
		
		if(data.size() == 0) // nothing to do
			return 0;
		
		for(int i = 0; i < Hw1.setSize("Hangover"); i++) {
			int count = 0;
			for(int j = 0; j < data.size(); j++) {
				Record record = data.get(j);
				
				if(record.getAttributes().get(Hw1.NUM_ATTRS - 1).getValue() == i)
					count++;
			}
				
			double probability = count / (double)data.size();
			if(count > 0)
				entropy += -probability * (Math.log(probability) / Math.log(2));
		}
		
		return entropy;
	}
	
	public static double calculateGain(double rootEntropy, ArrayList<Double> subEntropies, ArrayList<Integer> setSizes, int data) {
		double gain = rootEntropy; 
		
		for(int i = 0; i < subEntropies.size(); i++)
			gain += -((setSizes.get(i) / (double)data) * subEntropies.get(i));
		
		return gain;
	}
}
