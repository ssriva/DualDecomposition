package cky;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Parse {

	public Parse() {}

	
	static HashMap<Triple, Double > hunaryprobs = new HashMap<Triple, Double >();
	static HashMap<Triple, Double > hbinaryprobs = new HashMap<Triple, Double >();
	static{
		String line="";
		try {
			String rulesfile = "/Users/shashans/Desktop/hw3/pcfg";
			BufferedReader br = new BufferedReader(new FileReader(rulesfile));
			while ((line=br.readLine())!=null) {
				line=line.trim();
			    String[] tok = line.split("\\s+");
			    if(tok.length==3){
			    	hunaryprobs.put(new Triple(tok[0],tok[1],""), Double.parseDouble(tok[2]));}
			    else if(tok.length==4){
			    	hbinaryprobs.put(new Triple(tok[0],tok[1],tok[2]), Double.parseDouble(tok[3]));}
			}
			br.close();
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}
	
	public static String[] tags;
	public static String tree;
	
	public static ParseTree decode(String[] tokens, double[][]u) {
		
		int n = tokens.length;
		//Start with blank slate
		tags = new String[n];
		tree = null;
		
		HashMap<PairIndex, HashMap<String, Rule>> hmap = new HashMap<PairIndex, HashMap<String, Rule>>();
		String[] states = {"ADJ", "ADJ_NUM", "ADV", "CC", "CONNEC", "CV", "CVSUFF", "CV_CVSUFF", "DEM_PRON", "DIALECT", "DT", "EMPHATIC", "FOCUS", "FOREIGN", "FUT", "GRAMMAR_PROBLEM", "IN", "INTERROG", "INTERROG_PRON", "IV", "IVSUFF", "IV_PASS", "JJ", "JUS", "LATIN", "NN", "NNP", "NNPS", "NNS", "NONE", "NOUN", "NOUN_NUM", "NOUN_PROP", "NOUN_QUANT", "POSS_PRON", "PRON", "PRP", "PRP$", "PSEUDO_VERB", "PUNC", "PV", "PVSUFF", "PV_PASS", "RB", "RC", "REL_PRON", "RESTRIC", "RP", "SUB_CONJ", "TYPO", "UH", "VERB", "VOC", "WP", "WRB"};
		ArrayList<String> statesList = new ArrayList<String>(Arrays.asList(states));
		
		for(int j=0;j<=(n-1);j++){
			hmap.put(new PairIndex(j,j), new HashMap <String,Rule>());
			
				//Look at all unary rules
				for(Triple t:hunaryprobs.keySet()){
					if(t.getLeftChild().equals(tokens[j])){
						int index = statesList.indexOf(t.getParent());
						
						double score = hunaryprobs.get(t) + u[j][index];
						if(!hmap.get(new PairIndex(j,j)).containsKey(t.getParent())){
							hmap.get(new PairIndex(j,j)).put(t.getParent(), new Rule(t, true, j, score));
						}
						else if(hmap.get(new PairIndex(j,j)).get(t.getParent()).score < score ){
							hmap.get(new PairIndex(j,j)).put(t.getParent(), new Rule(t, true, j, score));
						}
					}
				}
				//If there is no unary rule for a word, create some
				if(hmap.get(new PairIndex(j,j)).keySet().size()<1){
					for(int l=0;l<states.length;l++){
						Triple t = new Triple(states[l],tokens[j],null);
						hmap.get(new PairIndex(j,j)).put(states[l], new Rule(t, true, j, -100000.00+u[j][l]));
					}
				}
		}
		
		//Levels of the parse tree
		for(int i=1;i<n;i++){
			//Individual cells at each level
			for(int j=0;j<=(n-1-i);j++){
				int start = j, end = j+i;
				hmap.put(new PairIndex(start,end), new HashMap <String,Rule>());
				//Partitions into (start,k) and (k+1,end)
				for(int k=start;k<end;k++){
					//Look at all binary rules
					for(Triple t:hbinaryprobs.keySet()){
						if((hmap.get(new PairIndex(start,k)).containsKey(t.getLeftChild())) && (hmap.get(new PairIndex(k+1,end)).containsKey(t.getRightChild()))){
							double sc1= hmap.get(new PairIndex(start,k)).get(t.getLeftChild()).score;
							double sc2= hmap.get(new PairIndex(k+1,end)).get(t.getRightChild()).score;
							double score = sc1+sc2+hbinaryprobs.get(t);
							//System.out.println(hmap.get(new PairIndex(start,end)).toString());
							if(!hmap.get(new PairIndex(start,end)).containsKey(t.getParent())){
								hmap.get(new PairIndex(start,end)).put(t.getParent(), new Rule(t, false, k, score));
							}
							if(hmap.get(new PairIndex(start,end)).get(t.getParent()).score < score ){
								hmap.get(new PairIndex(start,end)).put(t.getParent(), new Rule(t, false, k, score));
							}
						}
					}
				}		
			}
		}
		
		if(hmap.get(new PairIndex(0,n-1)).containsKey("S")){
			tree= retraceTree(hmap,0,n-1,"S");
			double treescore = hmap.get(new PairIndex(0,n-1)).get("S").score;
			ParseTree pt = new ParseTree(tree, tags, treescore);
			return pt;
		}
		else{
			String[]tags= {""};
			double treescore = 1;
			ParseTree pt = new ParseTree("", tags, treescore);
			return pt;
		}
	}

	private static String retraceTree(HashMap<PairIndex, HashMap<String, Rule>> hmap, int start, int end, String nonterminal) {
		if(start==end){
			tags[start]= nonterminal;
			return " ( " + nonterminal + " " + hmap.get(new PairIndex(start,end)).get(nonterminal).t.getLeftChild() +" )";
		}
		else{
			return " ( " + nonterminal + " " + retraceTree(hmap, start, hmap.get(new PairIndex(start,end)).get(nonterminal).split, hmap.get(new PairIndex(start,end)).get(nonterminal).t.getLeftChild()) + " " + retraceTree(hmap, hmap.get(new PairIndex(start,end)).get(nonterminal).split +1, end, hmap.get(new PairIndex(start,end)).get(nonterminal).t.getRightChild()) + " ) ";
		}
	}

}
