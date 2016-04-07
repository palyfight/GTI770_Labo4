
public class main {

	public static void main(String[] args) throws Exception {
		String fileInput1 = "";
		String fileInput2 = "";
		String output = "";
		WekaFileReader wfr = null;
		
		//wfr = new WekaFileReader("msd-ssd_test_nolabels.arff", "single.txt");
		
		wfr = new WekaFileReader("msd-ssd_test_nolabels.arff", "msd-marsyas_test_new_nolabels.arff", "multi.txt");
		
		
		if(args.length == 2){
			fileInput1 = args[0];
			output = args[1];
			wfr = new WekaFileReader(fileInput1, output);
		}
		else if(args.length == 3){
			fileInput1 = args[0];
			fileInput2 = args[1];
			output = args[2];
			wfr = new WekaFileReader(fileInput1, fileInput2, output);
		}
		else{
			System.out.println("Wrong number of arguments");
		}
	}
}
