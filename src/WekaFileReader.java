import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.Remove;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WekaFileReader {
	private Classifier ssdKnn;
	private Classifier marsyasKnn;
	private ArrayList<Double> ssdKnnValidate;
	private ArrayList<Double> marsyasKnnValidate;

	public WekaFileReader(String fileInput, String fileOutput) throws Exception{
		ssdKnnValidate = new ArrayList<Double>();
		ssdKnn = (Classifier) weka.core.SerializationHelper.read("ssd-knn.model");
		single(fileInput, fileOutput, true);
	}

	public WekaFileReader(String fileInput1, String fileInput2, String fileOutput) throws Exception{
		ssdKnnValidate = new ArrayList<Double>();
		marsyasKnnValidate = new ArrayList<Double>();
		ssdKnn = (Classifier) weka.core.SerializationHelper.read("ssd-knn.model");
		marsyasKnn = (Classifier) weka.core.SerializationHelper.read("marsyas-knn.model");
		multi(fileInput1, fileInput2, fileOutput);
	}

	public void single(String input, String output, boolean isSingle) throws Exception {
		try {
			System.out.println("The process is running ....");

			BufferedWriter writer = new BufferedWriter(new FileWriter(output));
			BufferedReader readerValidate = new BufferedReader(new FileReader(input));
			Instances wekaDataValidate = new Instances(readerValidate);
			Instances newWekaDataValidate = null;
			Instances DataNormalized = null;
			Normalize normalize = new Normalize();
			readerValidate.close();

			Remove rm = new Remove();
			rm.setAttributeIndicesArray(new int[]{0, 1});
			rm.setInvertSelection(false);
			rm.setInputFormat(wekaDataValidate);
			newWekaDataValidate = Filter.useFilter(wekaDataValidate, rm);

			normalize.setInputFormat(newWekaDataValidate);
			DataNormalized = Filter.useFilter(newWekaDataValidate, normalize);

			int index = DataNormalized.numAttributes() - 1;
			DataNormalized.setClassIndex(index);

			writer.flush();
			for (int i = 0; i < DataNormalized.numInstances(); i++) { 
				double clsLabel = ssdKnn.classifyInstance(DataNormalized.instance(i));
				ssdKnnValidate.add(1.0);
				if(isSingle){
					writer.write(DataNormalized.classAttribute().value((int) clsLabel));	
					writer.newLine();	
				}
			}
			System.out.println("Done ....");
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println("Can't read file");
		} catch (IOException e) {
			System.out.println("Can't extract data");
		}
	}

	public void multi(String fileInput1,String fileInput2, String output) throws Exception {
		try {	
			single(fileInput1, output, false); 

			BufferedWriter writer = new BufferedWriter(new FileWriter(output));
			BufferedReader readerValidate = new BufferedReader(new FileReader(fileInput2));
			Instances wekaDataValidate = new Instances(readerValidate);
			Instances newWekaDataValidate = null;
			Instances DataNormalized = null;
			Normalize normalize = new Normalize();
			readerValidate.close();

			Remove rm = new Remove();
			rm.setAttributeIndicesArray(new int[]{0, 1});
			rm.setInvertSelection(false);
			rm.setInputFormat(wekaDataValidate);
			newWekaDataValidate = Filter.useFilter(wekaDataValidate, rm);

			normalize.setInputFormat(newWekaDataValidate);
			DataNormalized = Filter.useFilter(newWekaDataValidate, normalize);

			int index = DataNormalized.numAttributes() - 1;
			DataNormalized.setClassIndex(index);

			writer.flush();
			for (int i = 0; i < DataNormalized.numInstances(); i++) { 
				System.out.println(((double)i / DataNormalized.numInstances()) * 100);
				double clsLabel = marsyasKnn.classifyInstance(DataNormalized.instance(i));
				marsyasKnnValidate.add(1.0);
				writer.write(DataNormalized.classAttribute().value((int) clsLabel));	
				writer.newLine();				
			}
			System.out.println("Done ....");
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println("Can't read file");
		} catch (IOException e) {
			System.out.println("Can't extract data");
		}
	}

}
