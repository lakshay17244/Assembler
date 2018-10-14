import java.io.*;
import java.util.*; 

class instruction{
	char label;
	
}

public class Assembler {

	public static void main(String[] args) throws FileNotFoundException {
		File f = new File("C:\\Users\\Lakshay\\Desktop\\test.txt");
		Scanner in = new Scanner(f);
		ArrayList<String> allLines = new ArrayList<String>();
		
		while(in.hasNextLine())
			allLines.add(in.nextLine());
		
		for(String s : allLines) 
			System.out.println("--->"+s);
	}

}
