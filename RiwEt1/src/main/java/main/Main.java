 package main;

import java.util.HashSet;
import java.util.Scanner;

import parser.HtmlProcessing;
import parser.IndexareDirecta;
import parser.IndexareIndirecta;
import search.CautareaBooleana;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unused")
		HtmlProcessing d = new HtmlProcessing();

		Scanner in = new Scanner(System.in);
		System.out.println(
				"1 : Realizeza indexarea directa \n "
			+ "2: Realizeaza indexarea indirecta\n"
				+ "3: Realizeza cautarea booleana");
		int num = in.nextInt();
		switch(num)
		{
		case 1:
			IndexareDirecta.directIndex("file");
			break;
		case 2:
			IndexareIndirecta.indirectIndex();
			break;
		case 3:
			String query = CautareaBooleana.readQueryFromUser();
		    HashSet<String> rez = CautareaBooleana.booleanSearch(query);
		    System.out.println((rez.size()==0)?"Nu s-au gasit documente":rez + "\ncount:" + rez.size());
			break;
		default:
			System.out.println("Introdu 1, 2 sau 3");
			break; 
		}
		in.close();
	}

}
