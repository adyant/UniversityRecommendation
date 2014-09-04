package service;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

//import mlib.spark.JavaKMeans.ParsePoint;














import model.Centroid;
import model.Distance;
import model.Record;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Kmean {

	@SuppressWarnings("serial")
	private static class ParsePoint implements Function<String, Vector> {
		private static final Pattern SPACE = Pattern.compile(" ");

		@Override
		public Vector call(String line) {	
			String[] tok = SPACE.split(line);
			double[] point = new double[tok.length];
			for (int i = 0; i < tok.length; ++i) {
				point[i] = Double.parseDouble(tok[i]);
			}
			return Vectors.dense(point);
		}
	}

	public static void main(String[] args) {
		List<String> filelist = new ArrayList<String>();
		String inputfolder = "src/main/resources/kmeaninp/";
		
		File folder = new File(inputfolder);
		File[] listoffiles = folder.listFiles();
		if (args.length < 3) { //putting a folder
			for (int i = 0; i < listoffiles.length; i++){
				filelist.add(inputfolder+listoffiles[i].getName());
			}

		}else{	//putting ONE file
			filelist.add(inputfolder+args[2]);
		}

		int k = Integer.parseInt(args[0]);
		int iterations = Integer.parseInt(args[1]);
		int runs = 1;

		if (args.length >= 4) {
			runs = Integer.parseInt(args[3]);
		}
		SparkConf sparkConf = new SparkConf().setAppName("JavaKMeans");
		sparkConf.setMaster("local");
		sparkConf.setSparkHome("/Users/adutta/SW/spark-1.0.2");
		JavaSparkContext sc = new JavaSparkContext(sparkConf);

		for (String s : filelist){
			processOneFile(s, sc, k, iterations, runs);
		}
		sc.stop();
	}

	@SuppressWarnings({ "unchecked" })
	public static void processOneFile(String inputfile, JavaSparkContext sc, int k, int iterations, int runs){
		JavaRDD<String> lines = sc.textFile(inputfile);
		JavaRDD<Vector> points = lines.map(new ParsePoint());
		List<Centroid> listofcentroids = new ArrayList<Centroid>();
		KMeansModel model = KMeans.train(points.rdd(), k, iterations, runs, KMeans.K_MEANS_PARALLEL());
		System.out.println("Cluster centers:");
		String outputfolder = "src/main/resources/kmeanout/";
		List<Vector> dataset = points.collect();

		for (Vector center : model.clusterCenters()) {
			double[] xy = center.toArray();

			Centroid centroid = new Centroid(xy[0], xy[1]);
			listofcentroids.add(centroid);
			System.out.println(" " + center);
		}

		Collections.sort(listofcentroids);
		//List<Record> listofrecords = new ArrayList<Record>();
		JSONArray list = new JSONArray();
		List<Distance> distances = new ArrayList<Distance>();
		int counter; //will go up to #clusters-1

		for(Vector vect: dataset){ //all the points
			counter = 0;
			double[] onepoint = vect.toArray(); //the point
			for (Centroid cent: listofcentroids) { //compute distance b/w centroid & point. store distance in distance[]
				double distance = Math.pow((onepoint[1]-cent.getWeight()), 2) + Math.pow( (onepoint[0]-cent.getStudentid()) , 2);
				distances.add(new Distance(counter, distance));
				++counter;
			}

			Collections.sort(distances, new DistanceComparator());
			Record newrecord = new Record(onepoint[0], onepoint[1], distances.get(0).getClusterid(), distances.get(0).getDistancesquare());
			//listofrecords.add(newrecord);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("sid", newrecord.getStudentid());
			jsonObj.put("weight", newrecord.getWeight());
			jsonObj.put("distance", newrecord.getError());
			jsonObj.put("cid", newrecord.getClusterid());
			list.add(jsonObj);
			
		}
	
		try {
			String outputfile = inputfile.substring((inputfile.lastIndexOf("/")+1));
			FileWriter jsonWriter = new FileWriter(outputfolder+ outputfile);
			jsonWriter.write(list.toJSONString());
			jsonWriter.flush();
			jsonWriter.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		double cost = model.computeCost(points.rdd());
		System.out.println("Cost: " + cost);

	}



}
