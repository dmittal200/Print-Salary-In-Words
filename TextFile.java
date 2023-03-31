package com.Salary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TextFile {
	
	public static void main(String[] args) throws IOException {
		
	}
	public static void AppData(String sAmtInNum, String sAmtInWrd, HashMap<Integer, String> GapMap)throws IOException {
		
		FileWriter writeObj = new FileWriter("C:\\Backup Innojar\\Txt\\Text File.txt",true);
		
		sAmtInNum = GapMap.get(0)+sAmtInNum ;
		
		String sAmtInNumF = sAmtInNum.substring(sAmtInNum.length()-10);
		
		String sAns=sAmtInNumF+" "+sAmtInWrd+"\n";
		writeObj.write(sAns);
		writeObj.close();
	}
	public static void PutGap(HashMap<Integer, String> GapMap) {
		
		GapMap.put(0, "          ");	
	}
	
	public static String ReadFile(String sTxtData) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader("C:\\Backup Innojar\\Txt\\Text File.txt"));
		String sAppendData = "";
		while(in.readLine() != null)
	        {
			sAppendData=sAppendData+in.readLine()+"$"+"\n";
			
	            
	        }
	        in.close();
	        System.out.print(sAppendData);
	        return sAppendData;
	        
	}
}
