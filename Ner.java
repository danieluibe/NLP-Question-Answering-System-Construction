import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.sequences.DocumentReaderAndWriter;
import edu.stanford.nlp.util.Triple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Ner {

	String serializedClassifier = "C:/Cornell/cs4740/project3/stanford-ner-2015-12-09/classifiers/english.muc.7class.distsim.crf.ser.gz";

	public boolean containPerson(String txt) throws Exception {

		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);		
		if(classifier.classifyToString(txt, "slashTags", false).contains("/PERSON")) return true;					
		else return false;
	}

	public boolean containLocation(String txt) throws Exception {

		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
		String s = classifier.classifyToString(txt, "slashTags", false) + " ";
		if(classifier.classifyToString(txt, "slashTags", false).contains("/LOCATION")) return true;					
		else return false;
	}


	public boolean containTime(String txt) throws Exception {

		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
		String s = classifier.classifyToString(txt, "slashTags", false) + " ";
		if(classifier.classifyToString(txt, "slashTags", false).contains("/TIME") || classifier.classifyToString(txt, "slashTags", false).contains("/DATE")) return true;					
		else return false;
	}

	public String getPerson(String txt) throws Exception {

		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
		String s = classifier.classifyToString(txt, "slashTags", false) + " ";
		String[] ss = s.split(" ");
		String result = "";
		for (String str : ss) {
			if(classifier.classifyToString(str, "slashTags", false).contains("/PERSON")){
				result += str.split("/")[0] + " ";
			}
		}
		return result.trim();
	}
	
	public String getLocation(String txt) throws Exception {

		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
		String s = classifier.classifyToString(txt, "slashTags", false) + " ";
		String[] ss = s.split(" ");
		String result = "";
		for (String str : ss) {
			if(classifier.classifyToString(str, "slashTags", false).contains("/LOCATION")){
				result += str.split("/")[0] + " ";
			}
		}
		return result.trim();
	}
	
	public String getTime(String txt) throws Exception {

		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
		String s = classifier.classifyToString(txt, "slashTags", false) + " ";
		
		String[] ss = s.split(" ");
		String result = "";
		for (String str : ss) {
			if(classifier.classifyToString(str, "slashTags", false).contains("/TIME") || classifier.classifyToString(str, "slashTags", false).contains("/DATE")){
				result += str.split("/")[0] + " ";
			}
		}
		return result.trim();
	}

	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		String example = "Mike Jason is born in May 1988 in America.";
		Ner tn = new Ner();
		System.out.println(tn.getTime(example));
	}

}
