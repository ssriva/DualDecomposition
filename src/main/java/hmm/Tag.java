package hmm;

import java.util.ArrayList;
//import java.util.Arrays;

public class Tag {

	public Tag() {
		// TODO Auto-generated constructor stub
	}

	public static TagSequence decode(ArrayList<Token> sentence, double[][]u){
		
		//String[] states = {"I-LOC","I-MISC","I-ORG","I-PER","B-LOC","B-MISC","B-ORG","B-PER","O"};
		String[] states = {"ADJ", "ADJ_NUM", "ADV", "CC", "CONNEC", "CV", "CVSUFF", "CV_CVSUFF", "DEM_PRON", "DIALECT", "DT", "EMPHATIC", "FOCUS", "FOREIGN", "FUT", "GRAMMAR_PROBLEM", "IN", "INTERROG", "INTERROG_PRON", "IV", "IVSUFF", "IV_PASS", "JJ", "JUS", "LATIN", "NN", "NNP", "NNPS", "NNS", "NONE", "NOUN", "NOUN_NUM", "NOUN_PROP", "NOUN_QUANT", "POSS_PRON", "PRON", "PRP", "PRP$", "PSEUDO_VERB", "PUNC", "PV", "PVSUFF", "PV_PASS", "RB", "RC", "REL_PRON", "RESTRIC", "RP", "SUB_CONJ", "TYPO", "UH", "VERB", "VOC", "WP", "WRB"};
		//ArrayList<String> statesList = new ArrayList<String>(Arrays.asList(states));
		
		double[][] vals = new double[states.length][sentence.size()];
		int[][] backPtr = new int[states.length][sentence.size()];
		
		/*First token*/
		for(int j=0;j<states.length;j++){
			vals[j][1] = FeatureWeights.stateScore(sentence, 1, states[j], "sentence_boundary") -u[0][j]; ////
			backPtr[j][1] = -1;
		}
		
		/*Intermediate tokens*/
		for(int i=2;i<=sentence.size()-2;i++){
			{
				for(int j=0;j<states.length;j++){
					vals[j][i] = FeatureWeights.stateScore(sentence, i, states[j], states[0]) -u[i-1][j] + vals[0][i-1];////
					backPtr[j][i] = 0;
					for(int k=1;k<states.length;k++){
						if(FeatureWeights.stateScore(sentence, i, states[j], states[k]) -u[i-1][j] + vals[k][i-1] > vals[j][i]){////
							vals[j][i] = FeatureWeights.stateScore(sentence, i, states[j], states[k]) -u[i-1][j] + vals[k][i-1];////
							backPtr[j][i] = k;
						}
					}
				}
			}
		}
		
		/*Stop token*/
		double finalv;
		{
			int i= sentence.size()-1;
			finalv = FeatureWeights.stateScore(sentence, i, "sentence_boundary", states[0]) + vals[0][i-1];
			backPtr[0][i] = 0;
			for(int k=1;k<states.length;k++){
				if(FeatureWeights.stateScore(sentence, i, "sentence_boundary", states[k]) + vals[k][i-1] > finalv){
					finalv = FeatureWeights.stateScore(sentence, i, "sentence_boundary", states[k]) + vals[k][i-1];
					backPtr[0][i] = k;
				}
			}	
		}
		
		//Retrace backpointers
		String[] tagSequence = new String[sentence.size()-2];
		////String[] tagSequence = new String[sentence.size()];
		////tagSequence[0]="sentence_boundary";
		////tagSequence[sentence.size()-1]="sentence_boundary";
		int l=0;
		
		//FeatureWeights.verbose=true;
		for(int i=sentence.size()-1; i>=2; i--){
			tagSequence[i-2] = states[backPtr[l][i]];
			l = backPtr[l][i];
		}
		FeatureWeights.verbose=false;
					
		//System.out.println("Score:"+finalv);
		TagSequence ts = new TagSequence(tagSequence, finalv);
		return ts;
	}
}
