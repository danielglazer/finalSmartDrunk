package tk.smartdrunk.smartdrunk.ID3;

public class Tab {
	public String uid;
	public double bac;
	public boolean hangover;
	
	public Tab(String uid, double bac, boolean hangover) {
	    this.uid = uid;
	    this.bac = bac;
	    this.hangover = hangover;
	}
	
	@Override
	public String toString() {
		return "[UID=" + this.uid + " BAC=" + this.bac + " Hangover=" + this.hangover + "]";
	}
}
