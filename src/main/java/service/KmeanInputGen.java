package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

public class KmeanInputGen {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Map<String, BufferedWriter> map = new HashMap<String, BufferedWriter>();
		Scanner scn = new Scanner(System.in);

		System.out.println("KMEAN INPUT GENERATION:");
		System.out.println("Generate for level: top, mid, or low (0, 1, 2)");
		int levelid = scn.nextInt();
		String level = "mid";
		switch (levelid){
		case 0: level = "top";
		break;
		case 1: level = "mid";
		break;
		case 2: level = "low";
		break;
		}

		FileReader fwt = new FileReader("src/main/resources/output" + level + ".csv");


		BufferedReader reader = new BufferedReader(fwt);
		String textline = null;
		StringTokenizer strtokenizer = null;
		int colonpos = 0, semipos = 0;// , tabpos = 0; 
		String fileid = null;
		String weight = null;
		String inputname = "src/main/resources/kmeaninp/feature";
		try {
			while((textline = reader.readLine()) != null){
				int firstTabPos = textline.indexOf('\t');
				String studentId = textline.substring(0,firstTabPos).trim();
				String afterStudentId = textline.substring(firstTabPos+1);
				strtokenizer = new StringTokenizer(afterStudentId, "\t");
				//String[] features = afterStudentId.;
				while (strtokenizer.hasMoreTokens()){

					String feature = strtokenizer.nextToken();
					colonpos = feature.indexOf(':');
					semipos = feature.indexOf(';');
					fileid = feature.substring(colonpos+1, semipos).trim();
					weight = feature.substring(semipos+1).trim();
					if (map.containsKey(fileid)){
						BufferedWriter writer = (BufferedWriter) map.get(fileid);
						writer.write(studentId + " " + weight +"\n");
					}else{
						BufferedWriter writer = new BufferedWriter(new FileWriter(inputname + "-" + fileid + ".txt"));
						writer.write(studentId + " " + weight +"\n");
						map.put(fileid, writer);
					}

					System.out.println(fileid);
					//	tabpos = textline.indexOf(':');

				}





			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		Collection<BufferedWriter> bfws = map.values();
		for (BufferedWriter b: bfws){
			b.close();
		}
		
		
		//fwt.close();
		reader.close();
	}
	

}
