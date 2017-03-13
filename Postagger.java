//package project3;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
 
public class Postagger {
    public HashMap<String, Integer> getNP(String s) {
    	HashMap<String, Integer> map = new HashMap<String, Integer>();
        // Initialize the tagger
//        MaxentTagger tagger = new MaxentTagger(
//                "/Users/asm278/Documents/Sem9/5740/stanford-postagger-2015-12-09/models/english-bidirectional-distsim.tagger");
    	 MaxentTagger tagger = new MaxentTagger(
               "C:/Cornell/cs4740/project3/stanford-postagger-2015-12-09/models/english-bidirectional-distsim.tagger");
        // The tagged string
        String tagged = tagger.tagString(s);
 
        // Output the result
        System.out.println(tagged);
        
        String temp = "";
		String la = "";
		String lalabel = "O";
		
        String[] tag = tagged.split(" ");
		for (String str : tag) {
			
			if(str.contains("_NN")){
				
				temp = str.split("_")[0];

				if(lalabel.equals("NN")){
					temp = la + " " + temp;
				}
				la = temp;
				lalabel = "NN";
			}
			
			else if(lalabel.equals("NN")){
				int count = map.containsKey(la.toLowerCase()) ? map.get(la.toLowerCase()) : 0;
				map.put(la.toLowerCase(), count + 1); 
				temp = "";
				la = "";
				lalabel = "O";
				count = 0;				
			}
		}
//		int count = map.containsKey(la) ? map.get(la) : 0;
//		map.put(la, count + 1);
		
		for (Entry<String, Integer> entry : map.entrySet()) {
			String st = entry.getKey();			
			int num = map.get(st.toLowerCase());
			System.out.println("NP:" + st + "; number:" + num);
		}
        return map;
    }
    public static void main(String[] args) {
		// TODO Auto-generated method stub
    	String sample = "Who is the leader of India?";
    	Postagger pos = new Postagger();
    	pos.getNP(sample);
        
	}
}

