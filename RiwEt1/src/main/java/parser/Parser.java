package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import helper.HtmlHelp;

public class Parser {
	
	public static void parseInformations(Writer writer,File input){
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			if(HtmlProcessing.getTitle(doc)!=null){
				String title = HtmlProcessing.getTitle(doc);
				writer.write(title);
			}
			List<HtmlHelp> metadatas = HtmlProcessing.getMetadatas(doc);
			for (HtmlHelp metadata : metadatas) {
				if(metadata.getName()!="robots"){
					writer.write(metadata.getContent());
				}
			}
			String text = HtmlProcessing.getText(doc);
			writer.write(text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void parseLinks(Writer writer, File input){
		Document doc;
		try {
			doc = Jsoup.parse(input, null);
			HashSet<String> urls = HtmlProcessing.getLinks(doc);
	        for (String element : urls) {
				writer.write(element+"\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String readFromFile(String filename){
		String doc = "";
		BufferedReader br = null;
		FileReader fr = null;
		try {
			String  line ;
			fr = new FileReader(new File(filename));
			br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
				doc += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return doc;
	}
	
	public static HashMap<String, Integer> parseText(File file){
		HashMap <String,Integer> words = new HashMap<String,Integer>();
		String doc = readFromFile(file.getAbsolutePath());
		HtmlProcessing.getWords(doc, words);
		return words;
	}
	
	public static List<String> getFiles(File dir) {
		List<String> paths = new ArrayList<String>();
		Stack<File> stack = new Stack<File>();
		stack.push(dir);
		while(!stack.isEmpty()) {
			File child = stack.pop();
			if (child.isDirectory()) {
				for(File f : child.listFiles()) stack.push(f);
			} else if (child.isFile()) {
				paths.add(child.getPath());
			}
		}
		return paths;
	}
}
