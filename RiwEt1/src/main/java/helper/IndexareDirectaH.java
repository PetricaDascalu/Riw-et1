package helper;

import java.util.HashMap;

public class IndexareDirectaH {
	String fileName;
	HashMap<String, Integer> words;
	public IndexareDirectaH() {
		fileName = null;
		words = null;
	}
	public IndexareDirectaH(String fileName, HashMap<String, Integer> words) {
		this.fileName = fileName;
		this.words = words;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public HashMap<String, Integer> getWords() {
		return words;
	}
	public void setWords(HashMap<String, Integer> words) {
		this.words = words;
	}
	
	
}
