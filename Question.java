//package project3;



import java.util.ArrayList;
import java.util.Arrays;

public class Question {
	public String question;
	public int number;
	public int answerDoc;
	public String label;
	//ensure that the following two are always sorted
	//i.e answerSupport[0] is in doc answerDoc which is the best answer
	private String[] answerSupport;
	public ArrayList<String> textPaths;
	//all of the possible answers
	public ArrayList<Sentence> answers;
	
	public Question(String q, int n){
		question = q;
		number = n;
		addLabel(q);
		answerSupport = new String[5];
		textPaths = new ArrayList<String>();
	}
	
	public void addAnswerSupport(String[] support){
		if (support.length > 5)
			support = Arrays.copyOfRange(support,0,4);
		answerSupport = support;
//		for (String s: answerSupport)
//			System.out.println(s);
	}
	
	public static int wordCount(String str){
		return str.trim().split(" ").length;
	}
	
	public String getBestAtLeast50Words(){
		String result = "";
		for (int i = 0; i < answers.size() && wordCount(result) < 50; i++){
			result += answers.get(i).toString();
		}
		return result;
	}
	
	//TODO Andrew
	//update to ensure 10 words printed
	@Override
	public String toString(){
		String str = "";
		try{
			if (this.answers == null || this.answers.size() == 0)
				throw new NullPointerException();
//			String[] response = getBestAtLeast50Words().trim().split(" ");
//			for (int i = 0; i < 5; i ++){
//				Sentence s = answers.get(i);
//				str += number + " ";
//				for (int j = 0; j < 10; j++)
//					str += response[10*i + j] + " ";
//				str += "\n";
//			}
			for (int i = 0; i < answers.size(); i++){
				str += number + " " + answers.get(i).docId + " " + answers.get(i).toString().replace("\n", "").replace("\r", "") + "\n";
			} 
		} catch (NullPointerException e) {
			str = "";
			for (int i = 0; i < 5; i++){
				str += number + " " + "nil" + " " + "nil\n";
			} 
		}
		return  str;
	}
	
	public void addLabel(String q){
		if(q.contains("Who")) label = "who";
		else if(q.contains("Where")) label = "where";
		else if(q.contains("When")) label = "when";
		else {label = "O";
		System.err.println("Question: " + question + ", Not WHO/WHERE/WHEN");
		}
	}
	
	public static void main(String[] args){
		Question q = new Question("Where am I?",1);
		q.addLabel(q.question);
		System.out.println(q.label);
	}
}
