package dualDecomposition;

import hmm.Tag;
import hmm.TagSequence;
import hmm.Token;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import cky.Parse;
import cky.ParseTree;

public class IntegratedParserFST {

	public IntegratedParserFST() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length<1){
			System.err.println("Incorrect usage.");
			System.err.println("USAGE: exec inputFile");
			return;
		}
		String inputFile = args[0];
		String parseoutputFile = inputFile+".jointparsed";
		String tagoutputFile = inputFile+ ".jointtagged";
		String parsetagsoutputFile = inputFile+".parsedtags";////

		{
			BufferedReader br;	
			try {
				br = new BufferedReader(new FileReader(inputFile));
				PrintWriter parsewriter = new PrintWriter(parseoutputFile, "UTF-8");
				PrintWriter tagwriter = new PrintWriter(tagoutputFile, "UTF-8");
				PrintWriter parsetagwriter = new PrintWriter(parsetagsoutputFile, "UTF-8");////
				
				int count=0;
				String line;
				ArrayList<Token> sentence = new ArrayList<Token>();
				String[] states = {"ADJ", "ADJ_NUM", "ADV", "CC", "CONNEC", "CV", "CVSUFF", "CV_CVSUFF", "DEM_PRON", "DIALECT", "DT", "EMPHATIC", "FOCUS", "FOREIGN", "FUT", "GRAMMAR_PROBLEM", "IN", "INTERROG", "INTERROG_PRON", "IV", "IVSUFF", "IV_PASS", "JJ", "JUS", "LATIN", "NN", "NNP", "NNPS", "NNS", "NONE", "NOUN", "NOUN_NUM", "NOUN_PROP", "NOUN_QUANT", "POSS_PRON", "PRON", "PRP", "PRP$", "PSEUDO_VERB", "PUNC", "PV", "PVSUFF", "PV_PASS", "RB", "RC", "REL_PRON", "RESTRIC", "RP", "SUB_CONJ", "TYPO", "UH", "VERB", "VOC", "WP", "WRB"};
				
				while ( (line=br.readLine()) != null ) {
					count++;
					line = line.trim();
					String[] toks = line.split("\\s+");    //Get individual tokens by splitting on whitespaces
					
					sentence.add(new Token("sentence_boundary"));
					for(int i=0;i<toks.length;i++){
						sentence.add(new Token(toks[i]));
					}
					sentence.add(new Token("sentence_boundary"));
						
					//Complete sentence, and print it
					System.out.print("Line:"+count);
					for(int i=0;i<sentence.size();i++){
						System.out.print(sentence.get(i).getWord()+" ");
					}
					System.out.println();
						
					//Run dual decomposition to jointly parse and tag sentence:
					ParseTree optimalParse=new ParseTree();
					TagSequence optimalTags=null;
					
					//Initialize
					int n = toks.length, m = 55, tcount=0;
					double[][]u = new double[n][m];
					for(int i=0;i<n;i++){
						for(int j=0;j<m;j++){
							u[i][j]=0;
						}
					}
					
					int maxIter=500;
					double prev_score=0,delta=1;
					//Iterate
					for(int iter=1;iter<=maxIter;iter++){						
						optimalParse = Parse.decode(toks,u);
						optimalTags = Tag.decode(sentence,u);
						
						boolean match=true;
						for(int i=0;i<n;i++){
							if(!optimalParse.getTags()[i].equals(optimalTags.getTags()[i])){
								match=false;
							}
						}
						if(match){
							System.out.println("\n\nBOTH TAGGINGS MATCH AFTER "+ iter + " ITERAITIONS!!!\n\n");
							break;
						}
						else{
							for(int i=0;i<n;i++){
								for(int j=0;j<m;j++){
									if(!optimalParse.getTags()[i].equals(optimalTags.getTags()[i])){
										if(optimalParse.getTags()[i].equals(states[j])){
											u[i][j]-=delta;
										}else if(optimalTags.getTags()[i].equals(states[j])){
											u[i][j]+=delta;
										}
									}
								}
							}
						}
						
						double new_score = optimalParse.getScore() + optimalTags.getScore();
						System.out.println("Objective: "+new_score);
						if(new_score>prev_score){
							System.out.println("Decreasing delta as score increased from "+prev_score+" to "+ new_score);
							delta=1.0/(tcount+1);
							System.out.println("New delta: "+delta);
							tcount++;
						}
						prev_score=new_score;
						
						if(iter==maxIter){
							System.out.println("\n\n TAGGINGS DIDNT MATCH AFTER MAXITERATIONS\n\n");
						}
					}	
					
					//Write output tags to file
					parsewriter.println(optimalParse.getTree());
					for(int i=0;i<optimalParse.getTags().length;i++){////
						parsetagwriter.print(optimalParse.getTags()[i]+" ");////
					}////
					parsetagwriter.println();////
					
					for(int i=0;i<sentence.size()-2;i++){
						tagwriter.print(optimalTags.getTags()[i]+" ");
					}
					tagwriter.println();
					sentence.clear();
						
				}
				br.close();
				parsewriter.close();
				parsetagwriter.close();
				tagwriter.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
		
	}

}
