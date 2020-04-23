package mongo;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import objects.IndexareDirectaH;
import objects.IndexareIndirctaH;

public class MongoUsage {
	static MongoClient mongoClient = new MongoClient("localhost", 27017);
	static MongoDatabase database = mongoClient.getDatabase("mylib");

	public static void popolateCollectionDirect(List<IndexareDirectaH> listOfFileTemplates) {
		MongoCollection<Document> collection = database.getCollection("IndexareDirecta");

		for(IndexareDirectaH directIndex : listOfFileTemplates)
		{
			Document doc = new Document();
			String word=directIndex.getFileName();
			
			doc.append("fileName",word);
			List<Document> docs =  new ArrayList<Document>();
			for(Map.Entry<String, Integer> entry : directIndex.getWords().entrySet())
			{
				Document docIterator = new Document();
				docIterator.append("word", entry.getKey());
				docIterator.append("count", entry.getValue());
				docs.add(docIterator);
			}
			doc.append("docs",docs);
			collection.insertOne(doc);
		}
	}
	
	public static void populateCollectionIndirect(List<IndexareIndirctaH> finalList)
	{
		MongoCollection<Document> collection = database.getCollection("IndexareIndirecta");
		
		for(IndexareIndirctaH wordFormat : finalList)
		{
			Document doc = new Document();
			String word=wordFormat.getWord();
			
			doc.append("word",word);
			List<Document> docs =  new ArrayList<Document>();
			for(Map.Entry<String, Integer> entry : wordFormat.getDocs().entrySet())
			{
				Document docIterator = new Document();
				docIterator.append("doc", entry.getKey());
				docIterator.append("count", entry.getValue());
				docs.add(docIterator);
			}
			doc.append("docs",docs);
			collection.insertOne(doc);
		}
	}
	
	public static List<IndexareIndirctaH> getDataFromIndirect()
	{
	    MongoCollection <Document> collection = database.getCollection("IndexareIndirecta");
	    FindIterable<Document> documents = collection.find();
	    
	    List<IndexareIndirctaH> list = new ArrayList<IndexareIndirctaH>();
	    for (Document document : documents) {
	    	IndexareIndirctaH wf = makeList(document);
	    	list.add(wf);
	    }
	    return list;
	}
	
	public static IndexareIndirctaH makeList(Document document)
	{
		IndexareIndirctaH wf = new IndexareIndirctaH();
		HashMap<String, Integer> docs = new HashMap<String, Integer>();
		wf.setWord(document.getString("word"));
		@SuppressWarnings("unchecked")
		List<Document> docTerms =  (List<Document>)document.get("docs");
    	for (Document docTerm : docTerms) {
    		docs.put(docTerm.getString("doc"), docTerm.getInteger("count"));
    	}
    	wf.setDocs(docs);
		return wf;
	}
	
	public static void indirectIndexToFile(){
		List<IndexareIndirctaH> list = getDataFromIndirect();
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		String strJson = g.toJson(list);
		
		Writer writer = null;
		try {
			writer = new FileWriter("IndexareIndirectaMongoDB.json");
			writer.write(strJson);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//IndexareDirecta
	public static List<IndexareDirectaH> getDataFromDirect()
	{
	    MongoCollection <Document> collection = database.getCollection("IndexareDirecta");
	    FindIterable<Document> documents = collection.find();
	    
	    List<IndexareDirectaH> list = new ArrayList<IndexareDirectaH>();
	    for (Document document : documents) {
	    	IndexareDirectaH wf = makeListDirect(document);
	    	list.add(wf);
	    }
	    return list;
	}
	
	public static IndexareDirectaH makeListDirect(Document document)
	{
		IndexareDirectaH wf = new IndexareDirectaH();
		HashMap<String, Integer> words = new HashMap<String, Integer>();
		wf.setFileName(document.getString("fileName"));
		@SuppressWarnings("unchecked")
		List<Document> docTerms =  (List<Document>)document.get("docs");
    	for (Document docTerm : docTerms) {
    		words.put(docTerm.getString("word"), docTerm.getInteger("count"));
    	}
    	wf.setWords(words);
		return wf;
	}
	
	public static void directIndexToFile(){
		List<IndexareDirectaH> list = getDataFromDirect();
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		String strJson = g.toJson(list);
		Writer writer = null;
		try {
			writer = new FileWriter("IndexareDirectaMongoDB.json");
			writer.write(strJson);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
