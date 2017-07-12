package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBProcess { 
	public static void InsertScannedPoints(Connection con,int number) throws Exception{
		PreparedStatement p=con.prepareStatement("INSERT INTO absorbance VALUES (?,?)");
		 p.setInt(1, number);
		 p.setDouble(2, 0);
		 p.execute();
	}
	public static int selectLastScannedSampleNo(Connection con) throws Exception{
		int sampleNo=0;
		PreparedStatement p=con.prepareStatement("SELECT MAX(sampleNo) FROM scans");
			p.execute();
			ResultSet rs=p.getResultSet();
			if(rs.next()){
			sampleNo=rs.getInt("MAX(sampleNo)");
			}
			return sampleNo;
	}
	public static void InsertScans(Connection con,int sampleNo,String dateTime) throws Exception{
		PreparedStatement p=con.prepareStatement("INSERT INTO scans VALUES (?,?)");
		p.setString(1, dateTime);
		p.setInt(2, sampleNo);
		p.execute();
	}	
	public static void StoreAbsorbance(Connection con,int sampleNo,double[] absorbance) throws SQLException{
		//跟新表，添加当前样品列
		String sample="sample"+sampleNo;
		String sql1="ALTER TABLE absorbance ADD "+sample+" double(9,2)";
		PreparedStatement p1=con.prepareStatement(sql1);
		p1.execute();
		//储存当前样品的吸光度数据
		for(int i=1;i<257;i++){
		String sql2="UPDATE absorbance SET "+sample+"=(?)"+" WHERE scannedPoints=(?)";
		PreparedStatement p2=con.prepareStatement(sql2);
		p2.setDouble(1, absorbance[i-1]);
		p2.setDouble(2, i);
		p2.execute();
		}
	}
	public static void UpdatetRice(Connection con,double LigthIntensity,int AttemptNo,int scannedpoints) throws SQLException{
		String attempt="Attempt"+AttemptNo;
		String sql="UPDATE rice SET "+attempt+"=(?)"+" WHERE scannedpoints=(?)";
		PreparedStatement p=con.prepareStatement(sql);
		p.setDouble(1, LigthIntensity);
		p.setInt(2, scannedpoints);
		p.execute();
	}
}
