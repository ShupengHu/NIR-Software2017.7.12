package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import OmniDriver.OmniDriver;

public class ModeTest {
	/**
	* �������±��ļ���������
	* @param data ����
	* @param fileName ���±��ļ���
	* @throws IOException
	*/
	public static void dataStored (double[] data, String fileName) throws IOException{
				//�����ļ�
				File file=new File(fileName);
				if(!file.exists()){
					file.createNewFile();	
				}
				//���ݴ���
				FileWriter FW=new FileWriter(file);
				for(double d:data){	
					//����2λС��������������
					//BigDecimal b = new BigDecimal(LightIntensity);
					//double LI=b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 				
					//FW.write(Double.toString(LI)+"\r\n");
					FW.write(Double.toString(d)+"\r\n");
				}     
				FW.close();
			}		
	
	
	public static void main(String args[]) throws IOException{
		OmniDriver OD =new OmniDriver();
		//����ģʽ
		OD.setMode(3);
		//���ò���������ʱ�䣺ms, ƽ��������ƽ���ȣ�
		OD.OmniOperations(1*1000, 3, 3);
		double[] sampleIntensity= OD.getLigthIntensities();
		
		//�����ļ���
		String fileName="12345.txt";	
		//���ݴ洢�����±��ļ�
		dataStored(sampleIntensity, "./ModeTestFiles/SampleIntensity/"+fileName);	
	}
}
