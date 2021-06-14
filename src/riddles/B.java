package riddles;

public class B extends A{
	
	protected int i;
	protected int j;


	public B(int i, int j) {
		super(i,j);
	
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		 int result = 1;
		 result = prime * result +j;
		 return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			 return true;}
			 if (obj == null) {
			 return false;}
			 if (getClass() != obj.getClass()) {
			 return false;}
			 B other = (B) obj;
			 if (this.j != other.j) {
			 return false;}
			 return true;

	}

}
