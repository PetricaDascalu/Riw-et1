package search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import helper.IndexareIndirctaH;
import parser.HtmlProcessing;
import parser.Parser;
import stemming.Stemming;


public class CautareaBooleana {
	private static HashSet<IndexareIndirctaH> indirectIndex;

	
	private static void loadIndirectIndex()
	{
		Gson gson = new Gson();
		String json = Parser.readFromFile("IndexareIndirectaMongoDB.json");
		indirectIndex = gson.fromJson(json, new TypeToken<HashSet<IndexareIndirctaH>>() { //convert  json into hashSet
		}.getType());
	}
	
	public static String readQueryFromUser() {
		String query;
		Scanner scanner = new Scanner(System.in);
		System.out.print("Introduceti cuvintele: ");
		query = scanner.nextLine();
		scanner.close();
		return query;
	}
	
	private static List<String> splitIntoOperators(String query)
	{
		List<String> list = new ArrayList<String>();
		int len = query.length();
		char c;
		for (int i = 0; i < len; i++) {
			c = query.charAt(i);
			if ((c == '+') || (c == '-') || (c == ' ')) {
				list.add(c + "");
			}
		}
		return list;
	}
	
	private static List<String> splitIntoOperands(String query) {
		List<String> list = new ArrayList<String>();
		String[] words = query.split("[-|+|\\s]");
		
		for (String string : words) {
			if (HtmlProcessing.isException(string)) {
				string = string.toLowerCase();
				list.add(string);
			} 
			else
			{
				string = string.toLowerCase();
				if (HtmlProcessing.isStopWord(string)) 
				{
					continue;
				} 
				else
				{
					Stemming s = new Stemming();
					char[] chs = string.toCharArray();
					s.add(chs, chs.length);
					s.stem();
					string = s.toString();
					list.add(string);
				}
			}
		}
		return list;
	}
	
	private static HashSet<String> And(HashSet<String> string1, HashSet<String> string2)
	{
		HashSet<String> rezultat = new HashSet<String>();
		int len1 = string1.size();
		int len2 = string2.size();
		if(!string2.isEmpty() && !string1.isEmpty())
		{
			if(len1 < len2)
			{
				for(String string : string1)
				{
					if(string2.contains(string)) {
						rezultat.add(string);
					}
				}
			}
			else
			{
				for(String string : string2)
				{
					if(string1.contains(string)) {
						rezultat.add(string);
					}
				}
			}
		}
		else
		{
			System.out.println("Nu exista aceste cuvinte!");
			System.exit(-1);
		}
		return rezultat;
	}
	
	private static HashSet<String> Or(HashSet<String> string1, HashSet<String> string2)
	{
		HashSet<String> rezultat = new HashSet<String>();
		int len1 = string1.size();
		int len2 = string2.size();
		if(!string2.isEmpty() && !string1.isEmpty())
		{
			if(len1 < len2)
			{
				rezultat.addAll(string2);
				for(String string : string1)
				{
					rezultat.add(string);
				}
			}
			else
			{
				rezultat.addAll(string1);
				for(String string : string2)
				{
					rezultat.add(string);
				}
			}
		}
		else
		{
			System.out.println("Nu exista asemenea termeni!");
			System.exit(-1);
		}
		return rezultat;
	}
	
	private static HashSet<String> Not(HashSet<String> string1, HashSet<String> string2)
	{
		HashSet<String> rezultat = new HashSet<String>();
		if(!string2.isEmpty() && !string1.isEmpty())
		{
			for(String string : string1)
			{
				if(!string2.contains(string))
				{
					rezultat.add(string);
				}
			}
		}
		else
		{
			System.out.println("Nu exista aceste cuvinte!");
			System.exit(-1);
		}
		return rezultat;
	}

	
	private static HashSet<String> getDocsForWord(String word)
	{
		for(IndexareIndirctaH wordsFormat : indirectIndex)
		{
			if(wordsFormat.getWord().equals(word)) {
				return wordsFormat.getOnlyDocs();
			}
		}
		return null;
	}
	
	 private static HashSet<String> getWordsIndirectIndex()
	{
		HashSet<String> words = new HashSet<String>();
		Iterator<IndexareIndirctaH> iter = indirectIndex.iterator();
		while (iter.hasNext()) {
		    words.add(iter.next().getWord());
		}
		return words;
	}
	
	public static HashSet<String> booleanSearch(String query)
	{
		HashSet<String> rezultat = new HashSet<String>();
		loadIndirectIndex();
		HashSet<String> words = getWordsIndirectIndex();
		
		List<String> operators = splitIntoOperators(query);
		List<String> operands = splitIntoOperands(query);
		
		List<String> operators2 = new ArrayList<String>();
		List<String> operands2 = new ArrayList<String>();
		
		if(words.contains(operands.get(0)))
		{
			operands2.add(operands.get(0));
		}
		for(int i = 0; i < operators.size(); i++)
		{
			
			if(words.contains(operands.get(i+1)))
			{
				if(!operands2.isEmpty())
				{
					operators2.add(operators.get(i));
				}
				operands2.add(operands.get(i+1));
			}
		}
		if(operands2.isEmpty() || (operands2.size() == 1))
		{
			System.out.println("Nu exista aceste cuvinte!");
			System.exit(-1);
		}
		rezultat = getDocsForWord(operands2.get(0));
		for(int i = 0; i < operators2.size(); i++)
		{
			switch (operators2.get(i)) {
			case "+":
				rezultat=And(rezultat, getDocsForWord(operands2.get(i+1)));
				break;
			case "-":
				rezultat=Not(rezultat, getDocsForWord(operands2.get(i+1)));
				break;
			case " ":
				rezultat=Or(rezultat, getDocsForWord(operands2.get(i+1)));
				break;
			}
		}
		return rezultat;
	}

}
