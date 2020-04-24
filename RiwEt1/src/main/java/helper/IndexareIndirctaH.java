package helper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class IndexareIndirctaH {
	String word;
	HashMap<String, Integer> docs;
	
	public IndexareIndirctaH(){
		word = "";
		docs = new HashMap<String, Integer>();
	}
	
	public IndexareIndirctaH(String word, HashMap<String, Integer> docs) {
		this.word = word;
		this.docs = docs;
	}
	
	public String getWord() {
		return word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}
	
	public HashMap<String, Integer> getDocs() {
		return docs;
	}
	
	public void setDocs(HashMap<String, Integer> docs) {
		this.docs = docs;
	}
	
	public void add(String fileName, Integer no){
		docs.put(fileName, no);
	}
	
	public HashSet<String> getOnlyDocs(){
		HashSet<String> documents = new HashSet<String>();
		Iterator<Entry<String, Integer>> it = docs.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,Integer> pair = (Map.Entry<String,Integer>)it.next();
	        documents.add(pair.getKey());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
		return documents;
	}	
}
