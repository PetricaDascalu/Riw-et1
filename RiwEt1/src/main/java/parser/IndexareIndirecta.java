package parser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import mongo.MongoUsage;
import objects.IndexareDirectaH;
import objects.IndexareIndirctaH;

public class IndexareIndirecta {
	private static List<IndexareIndirctaH> reverse(String filename){
		List<IndexareIndirctaH> list = new ArrayList<IndexareIndirctaH>();
        Gson gson = new Gson();
        String json = Parser.readFromFile(filename);
        List<IndexareDirectaH> listOfFiles = gson.fromJson(json, new TypeToken<List<IndexareDirectaH>>() {}.getType());
        for (IndexareDirectaH di : listOfFiles) {
			for (Map.Entry<String,Integer> word : di.getWords().entrySet()) {
				IndexareIndirctaH wt = new IndexareIndirctaH();
				wt.setWord(word.getKey());
				wt.add(di.getFileName(), word.getValue()); 
				list.add(wt);
			}
		}
        return list;
	}
	
	//se unesc fisierele
	private static List<IndexareIndirctaH> gather(List<IndexareIndirctaH> list){
		List<IndexareIndirctaH> finalList = new ArrayList<IndexareIndirctaH>();
		finalList.add(list.get(0));
		int last = 0;
		for (IndexareIndirctaH wordsTemplate : list) {
			if(finalList.get(last).getWord().equals(wordsTemplate.getWord())){
				finalList.get(last).getDocs().putAll(wordsTemplate.getDocs());
			}
			else{
				finalList.add(wordsTemplate);
				++last;
			}
		}
		return finalList;
	}

	private static void sort(List<IndexareIndirctaH> list) {
        list.sort(Comparator.comparing(IndexareIndirctaH::getWord));
    }
	
	public HashMap<String, Integer> getDocsFromWord(String word, List<IndexareIndirctaH> list)
	{
		HashMap<String, Integer> docs = new HashMap<String, Integer>();
		for (IndexareIndirctaH wordsTemplate : list) {
			if(word==wordsTemplate.getWord())
			{
				docs = wordsTemplate.getDocs();
			}
			else
			{
				System.out.println("Nu exista asa ceva!");
				return null;
			}
		}
		return docs;
	}
	
	public static void indirectIndex(){
		List<IndexareIndirctaH> list = IndexareIndirecta.reverse("IndexareDirectaMongoDB.json");
		sort(list);
		List<IndexareIndirctaH> finalList = gather(list);
		MongoUsage.populateCollectionIndirect(finalList);
		MongoUsage.indirectIndexToFile();
	}
}
