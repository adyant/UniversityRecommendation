package service;

import java.util.ArrayList;
import java.util.List;

import model.College;
import model.Preference;
import model.Student;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.api.java.JavaSQLContext;
import org.apache.spark.sql.api.java.JavaSchemaRDD;
import org.apache.spark.sql.api.java.Row;

public class CollegeMatcher {



	public static void main(String[] args) {
		SparkConf sparkConf = new SparkConf();
		sparkConf.setMaster("local");
		sparkConf.setSparkHome("c:\\BigDataSw\\spark-1.0.1-bin-hadoop1");
		sparkConf.setAppName("sqlRead");
		JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
		JavaSQLContext sqlContext = new JavaSQLContext(javaSparkContext);
		JavaRDD<College> colleges = javaSparkContext.textFile("src/main/resources/collegeoutputmid.csv").map(
				new Function<String, College>() {

					@Override
					public College call(String line) throws Exception {
						String[] components = line.split("\t");
						List<Preference> interests = new ArrayList<Preference>();
						College college = new College();
						college.setId(Integer.parseInt(components[0].trim()));
						for (int i=1; i<components.length; i++){
							Preference onepref = new Preference();
							onepref.setId(components[i].substring(components[i].indexOf(':'), components[i].indexOf(';')));
							onepref.setTopdesc(components[i].substring(0, components[i].indexOf('|')));
							onepref.setMiddesc(components[i].substring(components[i].indexOf('|'),components[i].lastIndexOf('|')));
							onepref.setLowdesc(components[i].substring(components[i].lastIndexOf('|'), components[i].lastIndexOf(':')));
							onepref.setWeight(Double.parseDouble(components[i].substring(components[i].indexOf(';'))));
							interests.add(onepref);
						}
						college.setPreferences(interests);
						//college.setOnepref(onepref);

						/*college.setAttributeone(components[1]);
							college.setAttributetwo(components[2]);
							college.setAttributethree(components[3]);
							college.setAttributefour(components[4]);
							college.setAttributefive(components[5]);
							college.setAttributesix(components[6]);


*/

					//	String awe = components[i];
					//}*/
					//		components
					//person.setAge(Integer.parseInt(components[1].trim()));
					return college;
				}
	}
				);

		JavaSchemaRDD schemaRDD = sqlContext.applySchema(colleges, College.class);
		schemaRDD.registerAsTable("Colleges");


		JavaSchemaRDD college = sqlContext.sql("SELECT attributeone FROM Colleges WHERE id = 4");
		List<String> fourthcollegefirstattr = college.map(new Function<Row,String>(){

			@Override
			public String call(Row row) throws Exception {
				// TODO Auto-generated method stub
				return row.getString(0);
			}

		}
				).collect();

		/*for (String name: fourthcollegefirstattr) {
			System.out.println(name);
		}*/
}

}
