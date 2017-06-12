package tk.smartdrunk.smartdrunk.ID3;

public class User {
	public String uid;
	public int age;
	public boolean gender;
	public int weight;
	
	public User(String uid,	int age, boolean gender, int weight) {
	    this.uid = uid;
	    this.age = age;
	    this.gender = gender;
	    this.weight = weight;
	}
	
	@Override
	public String toString() {
		return "[UID=" + this.uid + " Age=" + this.age + " Gender=" + this.gender + " Weight=" + this.weight + "]";
	}
}
