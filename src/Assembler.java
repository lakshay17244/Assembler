import java.io.*;
import java.util.*; 

class instruction{
	String opcode;
	String ascode;
	String var;
	
	public instruction(String n) {
		
	}
}

public class Assembler {
	
	static class OpCode{
		private Map<String, String> opcode;
		
		public OpCode() {
			opcode = new TreeMap<String, String>();
			opcode.put("CLA","0000");
			opcode.put("LAC","0001");
			opcode.put("SAC","0010");
			opcode.put("ADD","0011");
			opcode.put("SUB","0100");
			opcode.put("BRZ","0101");
			opcode.put("BRN","0110");
			opcode.put("BRP","0111");
			opcode.put("INP","1000");
			opcode.put("DSP","1001");
			opcode.put("MUL","1010");
			opcode.put("DIV","1011");
			opcode.put("STP","1100");
		}
		
		public String getOpCode(String n) {
			return opcode.get(n);
		}
		
	}

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
