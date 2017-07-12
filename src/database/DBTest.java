package database;

import java.io.File;
import java.sql.Connection;
import java.util.Scanner;

public class DBTest {
public static void main(String args[]) throws Exception{

	File file=new File("Light Intensity for DB");
	File[] files=file.listFiles();
	Connection con=DBConnection.getConnection();
	Scanner s = null;
	
	for(int i=0;i<files.length;i++){
	 s=new Scanner(files[i]);	
	//DBProcess.s(con,i+1);	
	
	int scannedpoints=1;
	while(s.hasNextDouble()){
		double LI=s.nextDouble();
		DBProcess.UpdatetRice(con, LI, i+1, scannedpoints);
		scannedpoints++;
	}
	}
	s.close();
	DBConnection.ConnectionClose(con);	
}
}
