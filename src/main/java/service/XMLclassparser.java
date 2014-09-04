package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.xpath.XPath;
//import javax.xml.xpath.XPathFactory;


import model.Attribute;


//import org.apache.hadoop.io.IntWritable;
//import org.apache.hadoop.io.Text;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//import au.com.bytecode.opencsv.CSVWriter;

public class XMLclassparser {

	public static void main(String[] args) throws IOException {		
		Map<String, Attribute> qualifiers = parseFile("StudentAtrributes.xml");
		Set<String> set = null;
		StringTokenizer strtokenizer = null;
		Scanner scn = new Scanner(System.in);
		System.out.println("WELCOME TO STUDENT DATA GENERATION:");
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

		FileWriter fwt = new FileWriter("src/main/resources/output" + level + ".csv");
		System.out.println("Please select the number of students you want(1-100): Press 0 for default 100");
		int nostudents = scn.nextInt();
		if (nostudents==0) nostudents = 100;
		System.out.println("How many maximum attributes/interests will a student have (1-6)");
		int maxattributes = scn.nextInt();
		if (maxattributes > 6) maxattributes = 6;

		Random randomGenerator = new Random();
		int topcnt = qualifiers.get("0_0_0").getMaxno();
//qualifiers.get("0_0_0").
		for (Integer i = 1; i <= nostudents; i++){
			System.out.println("=============================================");
			fwt.append(i.toString());
			int numofattr = randomGenerator.nextInt(maxattributes) + 1;
			set = new HashSet<String>();

			for (int j = 1; j <= numofattr; j++){
				try{
					int toplevel= randomGenerator.nextInt(topcnt) + 1; 					// randomly generated top value "x__"

					int midcnt = qualifiers.get("" + toplevel + "_0_0").getMaxno();
					int midlevel= randomGenerator.nextInt(midcnt) + 1; 					// randomly generated mid value "_x_"
					int lowcnt = qualifiers.get("" + toplevel + "_" + midlevel + "_0").getMaxno();					
					int lowlevel= randomGenerator.nextInt(lowcnt) + 1; 					// randomly generated low value "__x"
					String key = "";
				
					switch (levelid){
					case 0: key = toplevel + "_0_0";
					break;
					case 1: key = toplevel + "_" + midlevel + "_0";
					break;
					case 2: key = toplevel + "_" + midlevel + "_" + lowlevel;
					break;
					}
					set.add(key);
				}
				catch (Exception e){
					j--;
				}
			}
			
			Iterator itr = set.iterator();
			
			while (itr.hasNext()){
				String uniquekey = (String)itr.next();
				System.out.println(uniquekey);
				int maxwt = qualifiers.get(uniquekey).getWeight();
				float weight = randomGenerator.nextInt(maxwt) + 1; //random generation of weight
				strtokenizer= new StringTokenizer(uniquekey, "_");
				String top, mid, low;
				top = strtokenizer.nextToken("_");
				mid = strtokenizer.nextToken("_");
				low = strtokenizer.nextToken("_");
				String topname = qualifiers.get(""  + top + "_0_0").getCategory();
				String midname = qualifiers.get(top + "_" + mid + "_0").getCategory();
				String lowname = qualifiers.get(top + "_" + mid + "_" + low).getCategory();
				String record = "";

				switch (levelid){
				case 0: record = topname + ": " + top + "; " + weight;
				break;
				case 1: record = topname + "|" + midname + ": " + top + "_" + mid +  "; " + weight;
				break;
				case 2: record = topname + "|" + midname + "|" + lowname + ": " + top + "_" + mid + "_" + low + "; " + weight;
				break;
				}

				//String record = topname + "|" + midname + "|" + lowname+ ": " + weight;
				System.out.println(record);
				fwt.append("\t");
				fwt.append(record);
			}
			fwt.append("\n");
		}
		fwt.flush();
		fwt.close();
		scn.close();
	}

	public static Map<String, Attribute> parseFile(String filepath){
		File xmlFilerequest= new File(filepath);
		FileInputStream fstreamRequest;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentbuilder;
		Document doc;
		Attribute oneattr = null;
		Map<String, Attribute> attrmap = new HashMap<String, Attribute>();

		try {
			fstreamRequest = new FileInputStream(xmlFilerequest);
			documentbuilder = factory.newDocumentBuilder();
			doc = documentbuilder.parse(fstreamRequest);
			doc.getDocumentElement().normalize();
			NodeList nodelist = null;
			Node node = null, node2 = null, node3 = null;
			Element element = null, element2 = null, element3 = null;
			System.out.println("ROOT element: " + doc.getDocumentElement().getNodeName());
			System.out.println("_______________________");
			element = (Element)doc.getFirstChild();
			System.out.println(doc.getDocumentElement().getNodeName());
			String hmapkey;
			nodelist = doc.getElementsByTagName("attr");
			attrmap.put("0_0_0", new Attribute("root", nodelist.getLength()));

			for (int i = 0; i < nodelist.getLength(); i++){
				node = nodelist.item(i);
				element = (Element) node;
				//PUT TOP LEVEL INTO HASHMAP
				hmapkey = element.getAttribute("id") + "_0_0";
				oneattr = new Attribute();
				NodeList nodes= doc.getElementsByTagName("attr_"+(i+1));
				oneattr.setCategory(element.getAttribute("name"));
				oneattr.setMaxno(nodes.getLength());
				oneattr.setWeight(Integer.parseInt(element.getAttribute("weight")));
				attrmap.put(hmapkey, oneattr);

				for (int j=0; j < nodes.getLength(); j++){	
					node2 = nodes.item(j);
					element2 = (Element) node2;
					hmapkey = element.getAttribute("id") + "_"+ element2.getAttribute("id")+"_0";
					//PUT MID LEVEL INTO HASHMAP
					NodeList weights = doc.getElementsByTagName("attr_"+element.getAttribute("id")+"_"+(j+1));
					oneattr = new Attribute();
					oneattr.setCategory(element2.getAttribute("name"));
					oneattr.setWeight(Integer.parseInt(element2.getAttribute("weight")));//added line
					oneattr.setMaxno(weights.getLength());
					attrmap.put(hmapkey, oneattr);

					for(int k = 0; k < weights.getLength(); k++){
						node3 = weights.item(k);
						element3 = (Element) node3;
						hmapkey = element.getAttribute("id") + "_"+ element2.getAttribute("id")+"_"+ element3.getAttribute("id");
						oneattr = new Attribute();
						oneattr.setCategory(element3.getAttribute("name"));
						oneattr.setMaxno(Integer.parseInt(element3.getAttribute("weight")));
						oneattr.setWeight(Integer.parseInt(element3.getAttribute("weight")));
						attrmap.put(hmapkey, oneattr);
					}

				}
			}
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (ParserConfigurationException e) {
			System.err.println(e);
		} catch (SAXException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
		return attrmap;

	}





}
