package hmm;

public class TagSequence {
	public TagSequence(){}
	
	public TagSequence(String[] tags, double score){
		this.setTags(tags);
		this.setScore(score);
	}
	
	private String[] tags;
	private double score;
	
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
