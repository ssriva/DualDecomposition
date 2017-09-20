package cky;

public class ParseTree{
	public ParseTree(){}
	
	public ParseTree(String tree, String[] tags, double score){
		this.setTree(tree);
		this.setTags(tags);
		this.setScore(score);
	}
	
	private String tree;
	private String[] tags;
	private double score;
	
	public String getTree() {
		return tree;
	}
	public void setTree(String tree) {
		this.tree = tree;
	}
	public String[] getTags() {
		return tags;
	}
	public void setTags(String[] tags) {
		this.tags = tags;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
}