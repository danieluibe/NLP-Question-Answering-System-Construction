//package project3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class Sentence {
	public HashMap<String, Integer> nouns;
	public HashMap<String, Integer> words;
	private HashMap<String, Integer> named_nouns;
	private boolean isHeadline;
	public String originalText;
	private int frequency;
	public int docId;
	private double cosineDist;
	private int sentenceId;
	public double totalResponse;
	public double overlap;

	public Sentence(String text, int docId, boolean isHeadline, int sentenceId){
		originalText = text;
		this.docId = docId;
		frequency = 1;
		//		nouns = extractNouns(text);
		//		named_nouns = extractNamedNouns(text);
		//		words = extractWords(text);
		this.isHeadline = isHeadline;
		this.sentenceId = sentenceId;
		cosineDist = 0;
		totalResponse = 0;
	}
	
	
	//- See more at: http://java2novice.com/java-collections-and-util/arraylist/sort-comparator/#sthash.CwaHZ0Gj.dpuf

	public static Comparator<Sentence> SentComp = new Comparator<Sentence>() {
		
		public int compare(Sentence s1, Sentence s2) {
			double c1 = s1.overlap;
			double c2 = s2.overlap;
			//		   System.out.println("Compare " + c1 + " to " + c2);
			/*For one order*/
			//		   return (int) (1000*(c1-c2));
			if (c1 > c2)
				return -1;
			else if (c1 < c2)
				return 1;
			else
				return 0;
			/*For other order*/
			//		   if (c2 > c1)
			//			   return 1;
			//		   else if (c1 > c2)
			//			   return -1;
			//		   else
			//			   return 0;
		}};

		public void calcCosineDist(Map<String,Integer> question){
			cosineDist = calcSimilarity(question, nouns);
			totalResponse = cosineDist;
		}

		private static double calcSimilarity(Map<String,Integer> s1,Map<String,Integer> s2){
			int numerator = 0;
			for (String key: s1.keySet()){
				if (s2.get(key) != null && s1.get(key) != null)
					numerator += s2.get(key)*s1.get(key);
			}
			double result =  numerator/(double)(euclidLen(s1)*euclidLen(s2));
			//		System.out.println("sim for doc id "+docID + " is " + result + " for doc " + text);
			if (Double.isNaN(result))
				return 0.0;
			else
				return result;
			//baseline
			//		Random r = new Random();
			//		return r.nextDouble();
		}

		public static double euclidLen(Map<String,Integer> s1){
			double result = 0.0;
			for (String key: s1.keySet()){
				if (s1.get(key) != null)
					result += Math.pow(s1.get(key),2);
			}
			return Math.sqrt(result);
		}

		//TODO: Andrew
		public double[] getFeatureVector() {
			double[] arr = new double[5];
			arr[0] = sentenceId;
			arr[1] = docId;
			arr[2] = isHeadline ? 1.0 : 0.0;
			arr[3] = frequency;
			arr[4] = cosineDist;
			return arr;
		}

		//This is where you extract the nouns into a bag of words style HashMap
		//Where the key is the unique noun, and the value is the number of occurances in the sentence
		public static HashMap<String, Integer> extractNouns(String sentence){
			Postagger p = new Postagger();
			return p.getNP(sentence);

		}
		//This is where you extract the nouns into a bag of words style HashMap
		//Where the key is the unique noun, and the value is the number of occurances in the sentence
		public HashMap<String, Integer> extractWords(String sentence){
			String pre = Input.removeFunctionWords(sentence);
			HashMap<String,Integer> b = new HashMap<String, Integer>();
			String[] words = pre.split(" ");
			for (String word: words){
				if (b.get(word) != null)
					b.put(word, b.get(word) + 1);
				else
					b.put(word, 1);

			}
			this.words = b;
			return b;
		}

		//TODO:
		//This is where you extract the named nouns into a bag of words style HashMap
		//Where the key is the unique named noun, and the value is the number of occurances in the sentence
		public static HashMap<String, Integer> extractNamedNouns(String sentence){
			
			HashMap<String,Integer> b = new HashMap<String, Integer>();
			String[] words = sentence.split(" ");
			for (String word: words){
				if (b.get(word) != null)
					b.put(word, b.get(word) + 1);
				else
					b.put(word, 1);

			}
			return b;
		}

		@Override
		public String toString(){
			String result = "";
			//		for (String key: nouns.keySet()){
			//			if (nouns.get(key) != null)
			//				result += " " + key;
			//		}
			return originalText.replace("\n", "").replace("\r", "");
			//		return result;
		}

		public static void main(String[] args) {
			// TODO Auto-generated method stub
//		    ArrayList<Sentence> result = new ArrayList<Sentence>();
//			Sentence s1 = new Sentence("a", 1, false, 1);
//			s1.overlap = 5;
//			Sentence s2 = new Sentence("b", 1, false, 1);
//			s2.overlap = 3;
//			Sentence s3 = new Sentence("c", 1, false, 1);
//			s3.overlap = 2;
//			Sentence s4 = new Sentence("d", 1, false, 1);
//			s4.overlap = 8;
//			Sentence s5 = new Sentence("w", 1, false, 1);
//			s5.overlap = 10;
//			result.add(s1);
//			result.add(s2);
//			result.add(s3);
//			result.add(s4);
//			result.add(s5);
//			Collections.sort(result, SentComp);
//			for (Sentence s: result){
//                System.out.println(s.originalText);
//			}
			
		}
}



