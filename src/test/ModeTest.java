package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import OmniDriver.OmniDriver;

public class ModeTest {
	/**
	* 创建记事本文件储存数据
	* @param data 数据
	* @param fileName 记事本文件名
	* @throws IOException
	*/
	public static void dataStored (double[] data, String fileName) throws IOException{
				//创建文件
				File file=new File(fileName);
				if(!file.exists()){
					file.createNewFile();	
				}
				//数据储存
				FileWriter FW=new FileWriter(file);
				for(double d:data){	
					//保留2位小数并且四舍五入
					//BigDecimal b = new BigDecimal(LightIntensity);
					//double LI=b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 				
					//FW.write(Double.toString(LI)+"\r\n");
					FW.write(Double.toString(d)+"\r\n");
				}     
				FW.close();
			}		
	
	
	public static void main(String args[]) throws IOException{
		OmniDriver OD =new OmniDriver();
		//设置模式
		OD.setMode(3);
		//设置参数（积分时间：ms, 平均次数，平滑度）
		OD.OmniOperations(1*1000, 3, 3);
		double[] sampleIntensity= OD.getLigthIntensities();
		
		//设置文件名
		String fileName="12345.txt";	
		//数据存储到记事本文件
		dataStored(sampleIntensity, "./ModeTestFiles/SampleIntensity/"+fileName);	
	}
}
