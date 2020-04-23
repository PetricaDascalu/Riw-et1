package parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mongo.MongoUsage;
import objects.IndexareDirectaH;

public class IndexareDirecta {

	private static IndexareDirectaH getFileTemplate(String path, Writer writer){
		IndexareDirectaH di = new IndexareDirectaH();
		Writer inWriter = null;
		File file = new File(path);
		String filename = file.getName();

		String[] aux = filename.split("\\.");

		String absolutePath = file.getAbsolutePath();

		String filePath = absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
		filename = aux[0];
		if(aux[1].equals("html")){
			di.setFileName(absolutePath);
			try {
				inWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath+"/"+filename+".txt"), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Parser.parseInformations(inWriter, file);

			File f = new File(filePath+"/"+filename+".txt");
			HashMap<String, Integer> w = Parser.parseText(f);
			di.setWords(w);
			try {
				writer.write(file.getName() + ":indexDirectMongoDB.json\n");
				inWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return di;
	}
	
	private static List<IndexareDirectaH> getListOfFileTemplates(List<String> paths, Writer writer){
		List<IndexareDirectaH> listOfFileTemplates = new ArrayList<IndexareDirectaH>();
		for (String path : paths) {
			IndexareDirectaH di = IndexareDirecta.getFileTemplate(path, writer);
			if(di.getFileName() != null && di.getWords() != null){
				listOfFileTemplates.add(di);
			}
		}
		return listOfFileTemplates;
	}
	
	public static void directIndex(String dirName){
		List<String> paths = Parser.getFiles(new File(dirName));
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("mapare.txt"), "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		List<IndexareDirectaH> listOfFileTemplates = getListOfFileTemplates(paths, writer);
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		MongoUsage.popolateCollectionDirect(listOfFileTemplates);
		MongoUsage.directIndexToFile();
	}

}
