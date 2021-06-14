package riddles;

public class C extends B {
	
	private int i;
	private int j;

	public C(int i, int j) {
		super(i,j);
		
	}

	@Override
	public int compareTo(A other) {
		return this.j-other.j;
	}



}