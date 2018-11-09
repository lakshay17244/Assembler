import java.io.*;
import java.util.*;

class addressGen{
	static int counter=32;
	
	public static String toBinary(int N)	//Returns 8 bit binary number string for any integer <255 
    {	
		//System.out.println("N is "+N);
		if(N>255)
			System.out.println("Error in coding - at function binary()");
		 int[] binary = new int[8]; 
		   
	        // counter for binary array 
	        int i = 0; 
	        while (N > 0)  
	        { 
	            // storing remainder in binary array 
	            binary[i] = N % 2; 
	            N = N / 2; 
	            i++; 
	        } 
	       //now reverse the array
	        for(int r=0; r<binary.length/2; r++){ 
	        	int temp = binary[r]; 
	        	binary[r] = binary[binary.length -r -1]; 
	        	binary[binary.length -r -1] = temp; 
	        }
	        
        String res="";
        int addZeros = 8 - binary.length;		//number of zeros to add before number to make it 8 bit
        while(addZeros>0) {
        	System.out.println("addZeros is "+addZeros);
        	res=res+"0";
        	addZeros--;
        }
        
        for(int s : binary)
        	res=res+s;
        
        return res;	//return number
    }
	public static String getFreeAddress() {
		return toBinary(counter++);
	}
}

class OpCode{
	private static Map<String, String> opcode;
	private static Map<String, String> varcode;
	
	public OpCode() {
		varcode = new TreeMap<String, String>();
		opcode = new TreeMap<String, String>();
		//All opcodes
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
		//All varcodes
		varcode.put("DS","1101");
		varcode.put("DC","1110");
		varcode.put("DW","1111");
	}
	
	public static String toMachineOpCode(String n) {
		if(opcode.containsKey(n))
			return opcode.get(n);
		else if (varcode.containsKey(n))
			return varcode.get(n);
		else {
			System.out.println("Error! check toMachineOpCode() function");
			return "";
		}
	}
	
	public static boolean isLabel(String n) {
		
		if(opcode.containsKey(n))
			return true;
		else
			return false;
	}
	
	public static boolean isVariable(String n) {
		if(varcode.containsKey(n))
			return true;
		else
			return false;
	}
	
	public static boolean isValid(String n) {
		if(OpCode.isLabel(n) || OpCode.isVariable(n))
			return true;
		else
			return false;
	}
}

class allOperands{
	private static Map<String, String> operands;		//a map of operands and their addresses
	
	public allOperands() {
		operands = new TreeMap<String, String>();
	}
	
	public static void add(String name, int l) {
		if(operands.containsKey(name))
			System.out.println("--> ERROR - The variable '"+name+"' has already been defined earlier, no need to define again at line "+l);
		else
			operands.put(name, addressGen.getFreeAddress());
	}
	
	public static String getOperandAddress(String x,int line) {
		//System.out.println("---> "+operands);
		if(operands.containsKey(x))
			return operands.get(x);
		else {
			if(x!="")
				System.out.println("ERROR at line "+line+", Undefined operand - "+x);
			return "";
		}
	}

}

class symRow{		//One row of symbol table

	String symbol,type,value,address,opcode, labelOperand;


	int line;
	public String getOpcode() {
		return opcode;
	}
	
	public int getLine() {
		return line;
	}
	
	public String getSymbol() {
		return symbol;
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	public String getAddress() {
		return address;
	}

	
	public symRow(String n, int l) {	
		//System.out.println("====> "+n);
		String a[] = n.split(" ");
		line=l;
		
		symbol=a[0];				//label part of instruction 
		if(symbol.equals("****"))
			symbol="-none-";
		if(OpCode.isValid(a[1]))
			opcode=a[1];				//Opcode
		else {
			System.out.println("--> ERROR at line "+line+", Invalid OpCode '"+a[1]+"'");
		}
		address=addressGen.getFreeAddress();		//get next free address
		if(OpCode.isLabel(opcode)) {
			type="Label";				//Type is label
			//value="";
			labelOperand=a[2];
			if(a[2].equals("****") || a[2].charAt(0)=='\'')		//if operand is **** or operand is literal i.e. of the form '=2'
				labelOperand="";
		}
		else if(OpCode.isVariable(opcode)) {
			type="Variable";
			value=a[2];
			//System.out.println("Putting symbol - "+symbol);
			allOperands.add(symbol,line);				//Operand
			//System.out.println("Get - "+allOperands.getOperandAddress(symbol));
		}
		else
			System.out.println("--> ERROR: INVALID OPCODE '"+opcode+"' at line "+line);
	}
	
	public String toString(){
		return symbol+" "+type+" "+value+" "+address;
	}
	
	public String getOperandAddress(int l) {
		if(type.equals("Variable")) {
			//System.out.println("symbol is "+symbol);
			return allOperands.getOperandAddress(symbol,l);
		}
		else
			return allOperands.getOperandAddress(labelOperand,l);
	}
}

class literal{
	int value, line;			//value stores integer value
	String address, literal;	//literal stores value like '=1'
	
	public String getLiteral() {
		return literal;
	}
	public int getValue() {
		return value;
	}
	public String getAddress() {
		return address;
	}
	
	public literal(String n, int l) {
		line=l;
		literal=n.split(" ")[2];
		int endIndex=literal.length()-1;
		//System.out.println("literal is "+literal);
		value=Integer.parseInt(literal.substring(2,endIndex));
		address = addressGen.getFreeAddress();
	}
}

class MLRow{
	String address, opcode, operand, comment;	//Here, operand represents a binary address

	public String getAddress() {
		return address;
	}

	public String getOpcode() {
		return opcode;
	}

	public String getOperand() {
		return operand;
	}

	public String getComment() {
		return comment;
	}
	
	public MLRow(symRow s) {
		address=s.getAddress();
		opcode=OpCode.toMachineOpCode(s.getOpcode()); //fetches assembly opcode from symRow and converts to machine op code
		operand = s.getOperandAddress(s.getLine());
		comment=s.getOpcode();
	}	
}


class myAssembler{
	String path = "C:\\Users\\Lakshay\\Desktop\\Assembler\\2017244_2017254_test.txt";	//IMPORTANT - Enter the path of your text file,
															//and use two backslashes instead of one backslash
	ArrayList<String> code = new ArrayList<String>();		
	ArrayList<symRow> symTab = new ArrayList<symRow>();
	ArrayList<literal> litTab = new ArrayList<literal>();
	ArrayList<MLRow> MLTab = new ArrayList<MLRow>();
	
	public myAssembler() throws FileNotFoundException {
		File f = new File(path);
		Scanner in = new Scanner(f);
		while(in.hasNextLine())
			code.add(in.nextLine());
		
		/*for(String s : code) 
			System.out.println("--->"+s);*/
	}
	
	public void buildSymTab() {
		int a=0;
		for(String s: code) {
			symTab.add(new symRow(s,++a));
		}
		this.displaySymTab();
	}
	
	public void buildLitTab() {
		int a=0;
		for(String s: code) {
			++a;
			if(this.containsLiteral(s))
			try
			{
				literal=n.split(" ")[2];
				int endIndex=literal.length()-1;
				int value=Integer.parseInt(literal.substring(2,endIndex));
			}
			// if the literal is something like "=5f"
			catch( NumberFormatException e)
			{
				System.out.println("Invalid Literal given");
			}
			
			litTab.add(new literal(s,a));
		}
		
		this.displayLitTab();
	}
	
	public void buildMLTab() {
		for(symRow r : symTab)
			MLTab.add(new MLRow(r));
		this.displayMLTab();
	}
	public boolean containsLiteral(String n) {
		String[] t = n.split(" ");
		if(t[2].length()<4)
			return false;
		String i = t[2].substring(0, 2);
		//System.out.println("i is "+i);
		if(i.equals("'="))
			return true;
		else 
			return false;
	}
	
	public void displaySymTab() {
		System.out.println("\n\t\t\tSYMBOL TABLE");
		for(int i=0;i<70;i++)
			System.out.print("-");
		System.out.println();
		
		System.out.format("%10s%16s%16s%16s","Symbol","Type","Value","Address");
		System.out.println();
		for(int i=0;i<70;i++)
			System.out.print("-");
		System.out.println();
		for(symRow r : symTab) {
			System.out.format("%10s%16s%16s%16s",r.getSymbol(),r.getType(),r.getValue(),r.getAddress());
			System.out.println();
		}
	}
	
	public void displayLitTab() {
		System.out.println("\n\t\t\tLITERAL TABLE");
		for(int i=0;i<70;i++)
			System.out.print("-");
		System.out.println();
		
		System.out.format("%10s%16s%16s","Literal","Value","Address");
		System.out.println();
		for(int i=0;i<70;i++)
			System.out.print("-");
		System.out.println();
		for(literal r : litTab) {
			System.out.format("%10s%16s%16s",r.getLiteral(),r.getValue(),r.getAddress());
			System.out.println();
		}
		System.out.println();
	}
	
	public void displayMLTab() {
		System.out.println("\n\t\t\tMACHINE CODE");
		for(int i=0;i<70;i++)
			System.out.print("-");
		System.out.println();
		
		System.out.format("%10s%16s%16s%16s","Address","OpCode","Operand","Comment");
		System.out.println();
		for(int i=0;i<70;i++)
			System.out.print("-");
		System.out.println();
		for(MLRow r : MLTab) {
			System.out.format("%10s%16s%16s%16s",r.getAddress(),r.getOpcode(),r.getOperand(),r.getComment());
			System.out.println();
		}
	}
	
	public void writeMLOutput() {
		try { 
			 String f = path;
				int index=0;
				for(int i = f.length()-1;i>=0;i--) {
					if(f.charAt(i)=='\\'){
						index=i;
						break;
					}
				}
				
				f=f.substring(0, index);
				f=f+"\\output.txt";				//get path of output file in the same folder as input file
			
			 BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			 writer.write("Address\t\tOpCode\t\tOperand");
			 writer.newLine();
			 for(MLRow s : MLTab) {
				 writer.write(s.getAddress()+"\t"+s.getOpcode()+"\t\t"+s.getOperand());
				 writer.newLine();
			 }
			 writer.close();
		} 
		catch (IOException e) { 
			System.out.println("Exception - " + e); 
		} 
	}
}


public class Assembler {


	static void firstPass() throws FileNotFoundException {
		OpCode o = new OpCode();			//initialize
		allOperands b = new allOperands();	//initialize
		myAssembler a = new myAssembler();
		a.buildSymTab();
		a.buildLitTab();
		a.buildMLTab();
		a.writeMLOutput();
	}
	public static void main(String[] args) throws FileNotFoundException {
		
		firstPass();
	}

}
