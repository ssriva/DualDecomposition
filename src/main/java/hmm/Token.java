package hmm;

public class Token {

	private String word;
	private String pos;
	private String chuckTag;
	private String label;
	
	public Token(){}
	
	public Token(String s){
		this.setWord(s);
		this.setPos(s);
		this.setChuckTag(s);
		this.setLabel(s);
	}
	
	public Token(String w, String p, String c, String l) {
		//Populate all fields
		this.setWord(w);
		this.setPos(p);
		this.setChuckTag(c);
		this.setLabel(l);
	}
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public String getChuckTag() {
		return chuckTag;
	}
	public void setChuckTag(String chuckTag) {
		this.chuckTag = chuckTag;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
