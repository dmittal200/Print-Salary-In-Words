package com.Salary;

import java.io.IOException;
import java.util.HashMap;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class PrintInWords extends HttpServlet
{
	
	public void doPost(HttpServletRequest req, HttpServletResponse res ) throws IOException 
	{
		
		 String databaseName = "";
		 String url = "jdbc:mysql://localhost:3306/"+ databaseName;
		
		 String username="root";
		 String password="Mittal2000@";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//System.out.print(db);
		String sActualVal = req.getParameter("num1");
		double i =Double.parseDouble(req.getParameter("num1"));
		
		String s=String.valueOf(String.format("%.2f", i));
		//		//ps.setDate(3, sqlDate);
		
		String[] parts = s.split("\\.");
		String sRupeesNum =parts[0];
		String sPaisaNum =parts[1];
		HashMap<String, String> hp=new HashMap<>();
		MapStr(hp);
	
		String Npart=NumberPart(sRupeesNum, hp);
		String Dpart=DecimalPart(sPaisaNum, hp);
		
		String amount="";
		String textAmt="";
		if(sRupeesNum.charAt(0)=='1' && sRupeesNum.length()==1 && Dpart.isEmpty() ) {
			amount="Rupee"+"  "+Npart.trim()+" "+"only";
			textAmt=amount;
		}
		
		else if(!Npart.isEmpty() && !Dpart.isEmpty()) {
			amount="Rupees"+" "+Npart.trim()+" "+"and"+" "+"Paisa"+" "+Dpart.trim()+" "+"only";
			textAmt=amount;
		}
			
		else if (Dpart.isEmpty() && !Npart.isEmpty() ) {
			amount="Rupees"+" "+Npart.trim()+" "+"only";
			textAmt=amount;
		}
		
		else if(Npart.isEmpty()){
			amount="Paisa"+"  "+Dpart.trim()+" "+"only";
			textAmt=amount;
		}
		
		LocalDate dt= LocalDate.now();
		LocalTime tt= LocalTime.now();
		
		String sdate = String.valueOf(LocalDate.now());
		String sFdate =  sdate.replace("-", "");
		int idate = Integer.valueOf(sFdate);
		
		String time = String.valueOf(LocalTime.now());
		time = time.substring(0,8);
		System.out.print(time);
		
		
		TextFile obj = new TextFile();   //TextFile Data
		HashMap<Integer, String> GapMap = new HashMap<>();
		obj.PutGap(GapMap);
		obj.AppData(s, textAmt,GapMap);
		 
		
		
		
		String query= "INSERT INTO mydb.trn_slry(dSlry_InNum,sSlry_InWrd, iTrn_Date, sTrn_Time) VALUES ("+i+",'"+amount+"', "+idate+", '"+time+"')";
		//String query1 = "SELECT sSlry_InWrd FROM mydb.trn_slry";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		String sTxtData="";
		String sResData = obj.ReadFile(sTxtData);
		
		System.out.print(sResData);
		
		
		res.sendRedirect("Index.html?InWords="+amount+"&InDigits="+sActualVal+"&resultStr="+sResData);
	}
	
	public String NumberPart(String sRupeesNum, HashMap<String, String> hm) 
	{
		int iCntDigits = sRupeesNum.length();
		String sRupeesWrd="";
		
		for(int c=0; c<sRupeesNum.length();c++) 
		{
			if(sRupeesNum.charAt(c)=='0') 
				{ 
					iCntDigits--;
				}
			
			else  
			{    
				String sCntDigits = String.valueOf(iCntDigits);
				if(iCntDigits==1 || iCntDigits==3 || iCntDigits==4 || iCntDigits==6  )            
				{  
					sRupeesWrd+= MapStrGet("0"+sRupeesNum.charAt(c), hm)+" "+ MapStrGet(sCntDigits, hm)+" ";
					iCntDigits--;
				}
				
				else if(sRupeesNum.charAt(c)=='1'|| sRupeesNum.charAt(c+1)=='0')
				{
					sRupeesWrd+= MapStrGet(sRupeesNum.charAt(c)+""+sRupeesNum.charAt(c+1), hm)+" "+ MapStrGet(sCntDigits, hm)+" ";
					c++;
					iCntDigits=iCntDigits-2;
				}
				else 
				{
					sRupeesWrd+= MapStrGet(sRupeesNum.charAt(c)+"0", hm)+ " " +MapStrGet("0"+sRupeesNum.charAt(c+1), hm)+" "+ MapStrGet(sCntDigits, hm)+" ";
					c++;
					iCntDigits=iCntDigits-2;
				}					
			}
		}
	return sRupeesWrd;
	}
	
	public String DecimalPart(String sPaisaNum, HashMap<String, String> hm)
	{
		String sPaisaWrd="";
		sPaisaNum+="00";
		String sPaisaNumSub = sPaisaNum.substring(0,2);
		
		int iPaisaNumSub= Integer.parseInt(sPaisaNumSub);
	
		if((iPaisaNumSub>=21 && iPaisaNumSub<=99) && (sPaisaNumSub.charAt(1)!='0') )
		{
			sPaisaWrd+=MapStrGet(sPaisaNumSub.charAt(0)+"0", hm)+ " " +MapStrGet("0"+sPaisaNumSub.charAt(1), hm);
		}
		
		else if(iPaisaNumSub>0)
		{
			sPaisaWrd+=MapStrGet(sPaisaNumSub, hm);
		}
		return sPaisaWrd;
	}
	
	public  void MapStr(HashMap<String, String> hm) 
	{
		hm.put("1", "");
		hm.put("2", "");
		hm.put("3", "Hundred");
		hm.put("4", "Thousand");
		hm.put("5", "Thousand");
		hm.put("6", "Lakh");
		hm.put("7", "Lakh");
		hm.put("01", "One");
		hm.put("02", "Two");
		hm.put("03", "Three");
		hm.put("04", "Four");
		hm.put("05", "Five");
		hm.put("06", "Six");
		hm.put("07", "Seven");
		hm.put("08", "Eight");
		hm.put("09", "Nine");
		hm.put("10", "Ten");
		hm.put("11", "Eleven");
		hm.put("12", "Twelve");
		hm.put("13", "Thirteen");
		hm.put("14", "Fourteen");
		hm.put("15", "Fifteen");
		hm.put("16", "Sixteen");
		hm.put("17", "Seventeen");
		hm.put("18", "Eighteen");
		hm.put("19", "Nineteen");
		hm.put("20", "Twenty");
		hm.put("30", "Thirty");
		hm.put("40", "Forty");
		hm.put("50", "Fifty");
		hm.put("60", "Sixty");
		hm.put("70", "Seventy");
		hm.put("80", "Eighty");
		hm.put("90", "Ninety");
	}
	
	public String MapStrGet(String s, HashMap<String, String> hm) {
		return hm.get(s);
	}
}
