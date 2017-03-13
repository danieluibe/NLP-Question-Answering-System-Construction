//package project3;

/**
 * 
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * @author asm278
 *
 */
public class Input {
//	public static final String question_path = "/Users/asm278/Documents/Sem9/5740/project3/question.txt";
//	public static final String answer_path = "/Users/asm278/Documents/Sem9/5740/project3/answers.txt";
//	public static final String perl_script = "/Users/asm278/Documents/Sem9/5740/project3/eval.pl";
//	public static final String gt_path = "/Users/asm278/Documents/Sem9/5740/project3/pattern.txt";
//	public static final String files_path = "/Users/asm278/Documents/Sem9/5740/project3/files/doc_dev/";//< end with line seperator
//	public static final String function_folder = "/Users/asm278/Documents/Sem9/5740/project3/English_Function_Words_Set/";
	public static final String question_path = "C:/Cornell/cs4740/project3/question_dev.txt";
	public static final String answer_path = "C:/Cornell/cs4740/project3/answer_dev.txt";
	public static final String perl_script = "C:/Cornell/cs4740/project3/eval.pl";
	public static final String gt_path = "C:/Cornell/cs4740/project3/pattern.txt";
	public static final String files_path = "C:/Cornell/cs4740/project3/doc_dev/";//< end with line seperator
	public static final String function_folder = "C:/Cornell/cs4740/project3/English_Function_Words_Set/";
	// from: http://www2.fs.u-bunkyo.ac.jp/~gilner/wordlists.html#functionwords
	
	public Input(){
		
	}
	
	public ArrayList<Question> readInQuestions(){
		ArrayList<Question> questions = new ArrayList<Question>();
		try { 
			BufferedReader br = new BufferedReader(new FileReader(new File(question_path)));
			String line = "";
			String q = "";
			int num = -1;
			while ((line = br.readLine()) != null){
				if (line.length() < 1){
//					System.out.println("newline" + line);
				}else if (line.equals("</top>")){
					questions.add(new Question(q, num));
				} else if (line.equals("<top>")){
					q = ""; num = -1;
				} else if (line.contains("<num>")) {
					num = Integer.valueOf(line.substring(line.indexOf("Number:") + 8,line.length()));
//					System.out.println("num " + num);
//					System.out.println("num" + line + ", " + line.indexOf("Number:")+8);
				} else if (line.contains("<desc>")){
					q = "<desc>";
				} else if (q.equals("<desc>")){
					q = line;
//					System.out.println(line + " is question");
				} else {
//					System.out.println("useless" + line);
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return questions;
	}

	public static String[] FilePathGet(String s) {
		File file = new File(s);
		String filepath[];
		filepath = file.list();
		return filepath;
	}
	
	public static String txt2String(File file) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s = null;
			while ((s = br.readLine()) != null) {
				result.append(System.lineSeparator() + s);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public static String txt2String(String path){
		return txt2String(new File(path));
	}
	
	//if asParagraph, then it reads in based on only return characters
	//if not asPAragraph then read in sentence by sentence.
	public static ArrayList<Sentence> readInDocToSentences(String filepath, int docId, boolean asParagraph){
		ArrayList<Sentence> result = new ArrayList<Sentence>();
		String raw = txt2String(filepath);
		//extract the title
		String header = null;
		int i = 0;
		if (raw.indexOf("<HEADLINE>") >= 0){
			header = raw.substring(raw.indexOf("<HEADLINE>") + 10,raw.indexOf("</HEADLINE>"));
			if (header.indexOf("<P>") >= 0)
				header = header.substring(header.indexOf("<P>") + 3);
			if (header.indexOf("</P>") >= 0)
				header = header.substring(0,header.indexOf("</P>"));
		} else if((raw.indexOf("<HEADER>") >= 0)) {
			header = raw.substring(raw.indexOf("<HEADER>") + 10,raw.indexOf("</HEADER>"));
			if (header.indexOf("</AU>") >= 0)
				header = header.substring(header.indexOf("</AU>") + 5);
			if (header.indexOf("<DATE") >= 0)
				header = header.substring(0,header.indexOf("<DATE"));
		} else if (raw.indexOf("<HEAD>") >= 0){
			header = raw.substring(raw.indexOf("<HEAD>") + 6,raw.indexOf("</HEAD>"));
		} else if (raw.indexOf("<HL>") >= 0){
			header = raw.substring(raw.indexOf("<HL>") + 4,raw.indexOf("</HL>"));
		} else {
//			System.err.println("No header: " + raw);
		}
		if (header != null){
			result.add(new Sentence(header, docId, true, i++));
		}

		//extract the body sentences
		if (raw.indexOf("<TEXT>") < 0)
			return result;
		String body = raw.substring(raw.indexOf("<TEXT>") + 6,raw.lastIndexOf("</TEXT>"));
		body = body.replace("<P>", "").replaceAll("</P>", "");
		if ( asParagraph)
			System.err.println("asPARAGRAPH NOT IMPLEMENTED");
		else{
			//http://stackoverflow.com/questions/2687012/split-string-into-sentences
			BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
			iterator.setText(body);
			int start = iterator.first();
			for (int end = iterator.next();
			    end != BreakIterator.DONE;
			    start = end, end = iterator.next()) {
//			    System.out.println("\n" + body.substring(start,end) + "\n");
			    result.add(new Sentence(body.substring(start, end), docId, false, i++));
			}
		}
//		System.out.println(result);
		return result;
	}
	
	public static ArrayList<Sentence> questionToSentences(Question q, boolean asParagraph){
		ArrayList<Sentence> result = new ArrayList<Sentence>(100);
		for (int i = 0; i < q.textPaths.size(); i++){
			result.addAll(Input.readInDocToSentences(q.textPaths.get(i),i,asParagraph));
		}
		q.answers = result;
//		for (Sentence s: result)
//			s.calcCosineDist(Sentence.extractNouns(q.question));
		return result;
	}
	
	public static ArrayList<double[]> questionToVectors(Question q, boolean asParagraph){
		ArrayList<Sentence> sentences = questionToSentences(q,asParagraph);
		ArrayList<double[]> vectors = new ArrayList<double[]>();
		for (Sentence s: sentences)
			vectors.add(s.getFeatureVector());
		return vectors;
	}
	
	//TODO:
	//hopefully sorted by svm result, 
	//but classified, maybe SVM result stored as elm in the double[]
	public static ArrayList<double[]> classifyVectors(ArrayList<double[]> vectors){
		Random rand = new Random();
		ArrayList<double[]> result = new ArrayList<double[]>();
		for (double[] arr: vectors){
			double[] vec = new double[arr.length + 1];
			for (int i = 0; i < arr.length; i++){
				vec[i] = arr[i];
			}
			vec[arr.length] = rand.nextDouble();
			result.add(vec);
		}
		return result;
	}
	
	public static ArrayList<Sentence> vectorToAnswers(Question q, ArrayList<double[]> vec){
		ArrayList<Sentence> result = new ArrayList<Sentence>();
		for (double[] arr : vec){
			Sentence s = q.answers.get((int)arr[0]);
			s.totalResponse = arr[arr.length-1];
			result.add(s);
		}
		Collections.sort(result,Sentence.SentComp);
		q.answers = result;
		return result;
	}
	
	public void readInDocs(ArrayList<Question> questions){
		for (Question q: questions) { 
			String folder_path = files_path + q.number + "/";
			String FilePath[] = FilePathGet(folder_path);
			int FilePathInt[] = new int[FilePath.length];
			int i = 0;
			for (String file: FilePath){
				FilePathInt[i++] = new Integer(file);
			}
			Arrays.sort(FilePathInt);
			for(int doc_path: FilePathInt) {
				q.textPaths.add(folder_path + doc_path);
			}
		}
	}
	
	private static ArrayList<String> functionWords2String() {
		File file = new File(function_folder);
		String function_words_files[] = file.list();
		ArrayList<String> function_words = new ArrayList<String>();
		for (String words_file: function_words_files){
			try {
				file = new File(function_folder + words_file);
				
				BufferedReader br = new BufferedReader(new FileReader(file));
				String s = null;
				while ((s = br.readLine()) != null) {
					if (!s.contains("//"))
						function_words.add(s.toLowerCase());
				}
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return function_words;
	}
	
	public static String removeFunctionWords(String original){
		ArrayList<String> function_words = functionWords2String();
		String[] input = original.toLowerCase().substring(0, original.length() - 1).split(" ");//get rid of the ? ate the end of the question
		String acc = "";
		for (String in_word: input){
			if (!function_words.contains(in_word))
				acc += " " + in_word;
		}
		return acc.trim();
	}
	
	public static void writeAnswers(ArrayList<Question> questions){
			try( PrintWriter out = new PrintWriter(answer_path)){
			    for(Question q: questions)
			    	out.print(q);
			    
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	}
	
	public static void main(String[] args) throws Exception{
		Input in = new Input();
//		readInDocToSentences("/Users/asm278/Documents/Sem9/5740/project3/files/doc_dev/89/1", 1, false);
//		ArrayList<Question> questions = in.readInQuestions();
//		in.readInDocs(questions);
//		for (Question q: questions){
//			ArrayList<double[]> vectors = questionToVectors(q,false);
//			//classify vectors
//			vectors = classifyVectors(vectors);
//			//get from vectors to original sentences
//			vectorToAnswers(q, vectors);
//		}
//		writeAnswers(questions);
////		System.out.println(removeFunctionWords("all aboard mateys !"));
//		for(Question q : questions){
//			System.out.println("question:" + q.question);
//			System.out.println("question number:" + q.number);
//			System.out.println("question label: " + q.label);
//		}
//		BaselineAnswerer ba = new BaselineAnswerer();
//		for (Question q: questions)
//			ba.answer(q);
//			
//		
//
//		HashMap<Integer, ArrayList<String>> pattern = DocumentSelectorEvaluator.readInPattern();
//		HashMap<Integer,ArrayList<Integer>> correct_docs = DocumentSelectorEvaluator.findDoc(pattern);
//		DocumentSelectorEvaluator.getPercentCorrect(questions, correct_docs);
//		
		//System.out.println("Answers put at: " + answer_path);
		//System.out.println("perl " + perl_script + " " + gt_path  + " " + answer_path);
		
		String s = "This is a beautiful bag and he is a football player?";
		System.out.println(in.removeFunctionWords(s));
	}

}
