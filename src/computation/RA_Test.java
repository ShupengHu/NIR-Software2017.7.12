package computation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RA_Test {
public static void main(String args[]){
	File s_file=new File("./src/computation/s2.txt");
	File b_file=new File("./src/computation/b2.txt");
	File r_file=new File("./src/computation/r2.txt");
	
	//先判断数组的长度
    int points=0;
    try{
    	Scanner s=new Scanner(s_file);
	while(s.hasNextDouble()){
		//System.out.println("--------------------");
		s.nextDouble();
		points++;					
	}
	s.close();
    }catch (FileNotFoundException e1) {
		e1.printStackTrace();
	}
    System.out.println(points);
    double[] S_lightIntensities=new double[points];
    double[] B_lightIntensities=new double[points];
    double[] R_lightIntensities=new double[points];
     
   //读取文件数据
    //样品
    int a=0;
    try{
    	Scanner s=new Scanner(s_file);
	while(s.hasNextDouble()){
		S_lightIntensities[a]=s.nextDouble();
		a++;					
	}
	s.close();
    }catch (FileNotFoundException e1) {
		e1.printStackTrace();
	}
  //背景
    int b=0;
    try{
    	Scanner s=new Scanner(b_file);
	while(s.hasNextDouble()){
		B_lightIntensities[b]=s.nextDouble();
		b++;					
	}
	s.close();
    }catch (FileNotFoundException e1) {
		e1.printStackTrace();
	}
  //参考
    int c=0;
    try{
    	Scanner s=new Scanner(r_file);
	while(s.hasNextDouble()){
		R_lightIntensities[c]=s.nextDouble();
		c++;					
	}
	s.close();
    }catch (FileNotFoundException e1) {
		e1.printStackTrace();
	}
    
    
    //计算
    Reflectivity_Absorbance RA=new Reflectivity_Absorbance(R_lightIntensities, B_lightIntensities, S_lightIntensities);
    //double[] reflectivity=RA.getReflectivity();
    double[] absorbance=RA.getAbsorbance();
    //打印输出
    //for(double e: reflectivity){
    //	System.out.println(e);
    //}
    System.out.println("------------------------------------");
    for(double d: absorbance){
    	if(!Double.isNaN(d)){
    	System.out.println(d);
    	}else System.out.println(0.0);
    }
    
}
}
