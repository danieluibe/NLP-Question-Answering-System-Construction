//package project3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

public class Simpler {

	public static ArrayList<Sentence> getOverlappingSentences(ArrayList<Sentence> sentences, HashMap<String, Integer> questionWords, double factor, int num){
		ArrayList<Sentence> preresult = new ArrayList<Sentence>();
		ArrayList<Sentence> result = new ArrayList<Sentence>();
		for (Sentence s: sentences){
			int olap = countOverlap(questionWords, s.originalText);
			if (olap > 0){
				s.overlap += factor * olap;
				preresult.add(s);
			}
		}
		Collections.sort(preresult, Sentence.SentComp);
		int size = preresult.size();
		System.out.println("size in overlap function: " + size);
		int fsize = Math.min(size, num);
		for (int i = 0; i < fsize; i++){

			result.add(preresult.get(i));
		}
		return result;
	}

	public static ArrayList<Sentence> getFinalGuess(ArrayList<Sentence> sentences){
		ArrayList<Sentence> result = new ArrayList<Sentence>();
		ArrayList<Sentence> guess = new ArrayList<Sentence>();
		for (Sentence s: sentences){
			double oriscore = s.overlap;
			String[] sa = s.originalText.split(" ");
			double fiscore = oriscore + 1 * sa.length; //1 can be changed
			s.overlap = fiscore;
			result.add(s);
		}
		Collections.sort(result, Sentence.SentComp);
		int size = result.size();
		System.out.println("size in guess function: " + size);
		int fsize = Math.min(size, 5);
		for(int i = 0; i < fsize; i++){
			guess.add(result.get(i));
		}
		return guess;
	}

	//TODO implement this using the NER
	public static ArrayList<Sentence> removeWrongTypeAnswers(Question q) throws Exception{
		Ner n = new Ner();
		q.addLabel(q.question);
		ArrayList<Sentence> result = new ArrayList<Sentence>();
		for (Sentence s : q.answers){
			if(q.label.equals("who")){
				boolean b = n.containPerson(s.originalText);
				if(b) result.add(s); 
			}
			else if(q.label.equals("where")){
				boolean b = n.containLocation(s.originalText);
				if(b) result.add(s);
			}
			else if(q.label.equals("when")){
				boolean b = n.containTime(s.originalText);
				if(b) result.add(s);
			}
			else result.add(s);
		}

		return result;
	}

	public static ArrayList<Sentence> getshortAnswer(Question q) throws Exception{
		ArrayList<Sentence> re = new ArrayList<Sentence>();
		HashMap<String, Integer> asw = new HashMap<String, Integer>();
		Ner n = new Ner();
		String temp = "";
		String result = "";
		for(Sentence a : q.answers){

			if(q.label.equals("who")){
				temp = n.getPerson(a.originalText);
			}
			if(q.label.equals("where")){
				temp = n.getLocation(a.originalText);
			}
			if(q.label.equals("when")){
				temp = n.getTime(a.originalText);
			}
			String[] ss = temp.trim().split(" ");
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			int size = Math.min(ss.length, 10);
			for(int i = 0; i < size; i++){
				if(!map.containsKey(ss[i])){
					result += ss[i] + " "; 
					map.put(ss[i], 1); 
				}
			}
            result = result.trim();
			
			if(!result.equals("") && !asw.containsKey(result)){
				a.originalText = result;
				re.add(a);
				asw.put(result, 1); 
				System.out.println("answer: " + a.originalText + "; score:" + a.overlap);
			}
			
			result = "";
		}
		return re;
	}

	public static int countOverlap(HashMap<String, Integer> map, String s){
		int numerator = 0;
		String[] sar = s.toLowerCase().split(" ");
		for (String str : sar){
			if (map.get(str) != null)
				numerator += 1; //or map.get(str) depends on the design
		}
		return numerator;
	}

	public static void main(String[] args) throws Exception {
		Input in = new Input();
		ArrayList<Question> questions = in.readInQuestions();
		in.readInDocs(questions);
		for (Question q: questions){
			Sentence qSentence = new Sentence(q.question, q.number, false, -1);
			qSentence.nouns = Sentence.extractNouns(q.question);
			q.answers = getOverlappingSentences(Input.questionToSentences(q, false), qSentence.nouns, 1.0, 25); //25 can be changed


			//NER tagging removal of some answers
			q.answers = removeWrongTypeAnswers(q);

			//get overlapping questions with all text words
			qSentence.extractWords(q.question);
			System.out.println("q.answers.size():" + q.answers.size());
			System.out.println("qSentence.words:" + qSentence.words);
			q.answers = getOverlappingSentences(q.answers, qSentence.words, 1.0, 15); //15 can be changed
			System.out.println("q.answers.size() after:" + q.answers.size());
			q.answers = getshortAnswer(q);

			//get 5 guess
			q.answers = getFinalGuess(q.answers);
			System.out.println("q.guess.size():" + q.answers.size());
			for(Sentence aa : q.answers){
				System.out.println("q guess:" + aa.originalText + "; final score:" + aa.overlap);
			}
		}
		Input.writeAnswers(questions);
		System.out.println("Answers put at: " + Input.answer_path);
		System.out.println("perl " + Input.perl_script + " " + Input.gt_path  + " " + Input.answer_path);


	}

}
