package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeTest {
	public static void main(String[] args) throws Exception { 
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		String dateTime=df.format(new Date());
		System.out.println(dateTime);// new Date()Ϊ��ȡ��ǰϵͳʱ��
		
		Connection con=DBConnection.getConnection();
		/*
		for(int i=1;i<257;i++){		
		DBProcess.InsertScannedPoints(con, i);
		}
		*/
		/*
		PreparedStatement p=con.prepareStatement("INSERT INTO scans VALUES (?,?)");
		 p.setString(1, dateTime);
		 p.setInt(2, 1);
		 p.execute();
		*/
		System.out.println(DBProcess.selectLastScannedSampleNo(con));
		
		
		}
}
