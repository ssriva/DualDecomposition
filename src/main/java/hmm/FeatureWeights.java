package hmm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class FeatureWeights {

	public FeatureWeights() {
		// TODO Auto-generated constructor stub
	}
	
	public static Boolean verbose = false;
	static HashMap<String, Double > htrans = new HashMap<String, Double >();
	static HashMap<String, Double > hemits = new HashMap<String, Double >();
	static{
		String line="";
		try {
			
			String emitsfile = "/Users/shashans/Desktop/hw3/hmm_emits";
			BufferedReader br1 = new BufferedReader(new FileReader(emitsfile));
			while ((line=br1.readLine())!=null) {
				line=line.trim();
			    String[] tok = line.split("\\s+");
			    if(tok.length==3){
			    	hemits.put(tok[0]+"+"+tok[1],Double.parseDouble(tok[2]));
				}
			}
			
			String transfile = "/Users/shashans/Desktop/hw3/hmm_trans";
			BufferedReader br2 = new BufferedReader(new FileReader(transfile));
			while ((line=br2.readLine())!=null) {
				line=line.trim();
			    String[] tok = line.split("\\s+");
			    if(tok.length==3){
			    	htrans.put(tok[0]+"+"+tok[1],Double.parseDouble(tok[2]));
				}
			}
			
			br1.close();
			br2.close();
			
		} catch (Exception e) {
			System.err.println(line);
			System.err.println(e);
		}
		
	}
	
	public static double stateScore(ArrayList<Token> sentence, int index, String state, String prevstate){
		
		double val=0;
		
		String transkey=prevstate+"+"+state;
		if(htrans.containsKey(transkey)){
			//System.out.println("Found key in transkey:"+transkey);
			val+=htrans.get(transkey);
		}
		else
			val+=-100000;
		

		String emitskey=state+"+"+sentence.get(index).getWord();
		if(hemits.containsKey(emitskey)){
			//System.out.println("Found key in emitskey:"+emitskey);
			val+=hemits.get(emitskey);
		}
		else
			val+=-1000;
	
		return val;
	}
	
}
