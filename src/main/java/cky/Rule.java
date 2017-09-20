package cky;

public class Rule {

	public Rule() {}
	
	public Rule(Triple t, boolean IsUnary, int split, double score){
		this.t = t;
		this.score = score;
		this.IsUnary = IsUnary;
		this.split = split;
	}
	
	Triple t;
	boolean IsUnary;
	int split;
	double score;
}


class PairIndex{
	public PairIndex(){}
	
	public PairIndex(int first, int second){
		this.first = first;
		this.second =second;
	}
	
	public int getFirst() {
		return first;
	}
	public void setFirst(int first) {
		this.first = first;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	private int first;
	private int second;
	
	@Override
	public int hashCode() { return (new Integer(first).hashCode()) ^ (new Integer(second).hashCode()); }
	
	@Override
	  public boolean equals(Object o) {
	    if (o == null) return false;
	    if (!(o instanceof PairIndex)) return false;
	    PairIndex pairo = (PairIndex) o;
	    return ((this.first == pairo.getFirst()) && (this.second == pairo.getSecond()));
	  }
}

class Triple {
	public Triple(){}
	
	public Triple(String parent, String leftChild, String rightChild){
		this.setParent(parent);
		this.setLeftChild(leftChild);
		this.setRightChild(rightChild);
	}

	private String parent;
	private String leftChild;
	private String rightChild;
	
	@Override
	public int hashCode() { return parent.hashCode() ^ leftChild.hashCode() ^ rightChild.hashCode(); }
	
	@Override
	  public boolean equals(Object o) {
	    if (o == null) return false;
	    if (!(o instanceof Triple)) return false;
	    Triple triple = (Triple) o;
	    return this.leftChild.equals(triple.getLeftChild()) &&
	           this.rightChild.equals(triple.getRightChild()) &&
	           this.parent.equals(triple.getParent());
	  }
	
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getLeftChild() {
		return leftChild;
	}
	public void setLeftChild(String leftChild) {
		this.leftChild = leftChild;
	}
	public String getRightChild() {
		return rightChild;
	}
	public void setRightChild(String rightChild) {
		this.rightChild = rightChild;
	}
}

