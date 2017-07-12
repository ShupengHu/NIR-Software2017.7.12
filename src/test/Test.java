package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class Test {
public static void main(String args[]) throws IOException{
	//Calculation1();
	//Calculation2();
	//Calculation3();
	//Calculation4();
	//Calculation5();
	//Calculation6();
	//Calculation7();
	//Calculation8();
	for(int i=0;i<3;i++){
	Calculation9();
	}
	//Calculation10();
}	
/**
 * ����2λС��
 * @throws FileNotFoundException
 */
public static void Calculation1() throws FileNotFoundException{
File file=new File("test.txt");
	 Scanner s1=new Scanner(file);
	 while(s1.hasNextDouble()){
     	double a=s1.nextDouble();
     	BigDecimal b = new BigDecimal(a);
     	double A = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
     	
     	System.out.println(a+"----------"+A);
     }
	 s1.close();
}
/**
 * ��������
 */
public static void Calculation2(){
	double a=Math.log10(10);
	System.out.println(a);
}
/**
 * ʮ������תʮ����
 */
public static void Calculation3(){
	System.out.println(Integer.parseInt("ff",16));
	System.out.println(0xff);
}
/**
 * Excel���ҳ���ֵ����һ��
 */
public static void Calculation4(){
	   try {
		    File file=new File("C:/Users/shupengh/Desktop/model.xlsx");
			InputStream is = new FileInputStream(file);
			XSSFWorkbook excel=new XSSFWorkbook(is);	
			//read sheet, index of sheet in excel starts from 1,while in java starts from 0					
			XSSFSheet sheet = excel.getSheetAt(0);
			//���жϿ�ֵ�е�λ��
			int index=0;
			XSSFRow rowTest=sheet.getRow(0);
			for(int i=0;i<sheet.getLastRowNum()+1;i++){
			XSSFCell cellTest=rowTest.getCell(i);
			//System.out.println(cellTest.getCellType());
			if(cellTest.getCellType()==3){
				index=i;
				break;
			}	
			}
		   //excel����һ��10�У�454�У�һ�������ǻ�ѧֵ���������ǿգ�getLastRowNum()����9������ȡֵ0-9��getLastCellNum()����454������ȡֵ0-453
			double[][] model_absorbance=new double[sheet.getLastRowNum()+1][sheet.getRow(0).getLastCellNum()-1-index];
			double[][] model_chemicalValue=new double[sheet.getLastRowNum()+1][index];
			for(int rowNo=0;rowNo<sheet.getLastRowNum()+1;rowNo++){
				XSSFRow row=sheet.getRow(rowNo);
					for(int cellNo=0;cellNo<row.getLastCellNum();cellNo++){
						//��ȡģ�ͻ�ѧֵ
						if(cellNo<index){
							XSSFCell cell=row.getCell(cellNo);
							model_chemicalValue[rowNo][cellNo]=cell.getNumericCellValue();								
						}
						//��ȡģ�������
						else if(cellNo>index){
							XSSFCell cell=row.getCell(cellNo);							
							model_absorbance[rowNo][cellNo-index-1]=cell.getNumericCellValue();							
						}																		
					}				
			}
			 System.out.println(model_absorbance[5][1]);
			 System.out.println(model_chemicalValue[6][1]);
			 System.out.println(model_chemicalValue.length+", "+model_chemicalValue[1].length);
	   }catch (IOException e1) {
			e1.printStackTrace();
		}	  
}
/**
 * StringBuffer ת��
 */
public static void Calculation5(){
	StringBuffer s=new StringBuffer();
	s.append("abc"+"\n"+"ddd");
	s.append("\n");
	s.append("eeee");
	System.out.println(s.toString());
}
/**
 * ����ɾ������Excel����
 * @throws IOException 
 */
public static void Calculation6() throws IOException{
	InputStream is = new FileInputStream("Concentration.xlsx");
	XSSFWorkbook excel=new XSSFWorkbook(is);
	XSSFSheet sheet=excel.getSheetAt(0);
	XSSFRow	row=sheet.getRow(sheet.getLastRowNum());
	sheet.removeRow(row);
	row=sheet.getRow(sheet.getLastRowNum());
	sheet.removeRow(row);
	row=sheet.getRow(sheet.getLastRowNum());
	sheet.removeRow(row);
    System.out.println(sheet.getLastRowNum());
	//����excel�޸�
	FileOutputStream FOS=new FileOutputStream("Concentration.xlsx");
	excel.write(FOS);
}
/**
 * ���ڲ���&excel�ļ�������&�½�excel����
 * @throws IOException 
 */
public static void Calculation7() throws IOException{
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	String dateTime = df.format(new Date());
	System.out.println(dateTime);
	File absorbanceFile=new File("./Absorbance(excel)/");
	File[] absorbanceFiles=absorbanceFile.listFiles();
	System.out.println(absorbanceFiles.length);
	String filename= absorbanceFiles[absorbanceFiles.length-1].getName();
	System.out.println(filename);
	if(filename.equalsIgnoreCase(dateTime+".xlsx")==false){
		XSSFWorkbook excel=new XSSFWorkbook();
		XSSFSheet sheet=excel.createSheet();
		System.out.println(sheet.getLastRowNum());
       FileOutputStream FOS=new FileOutputStream("./Absorbance(excel)/"+dateTime+".xlsx");
       excel.write(FOS);
	}	
	else System.out.println("excel file has existed");
}
/**
 * byte���������ӡ
 */
public static void Calculation8(){
	byte[] backward={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x01,(byte) 0xff,0x65};
	if(backward[0]==1){		
	System.out.println(Arrays.toString(backward));
	}
}
/**
 * excel��ͬ�汾��д����
 * absorbance excel û����
 * Sample_LightIntensity û����
 * Reference_LightIntensity û����
 * @throws IOException 
 */
public static void Calculation9() throws IOException{
	//InputStream is = new FileInputStream("./Absorbance(excel)/2016-11-07.xlsx");
	//InputStream is = new FileInputStream("./Sample_LightIntensity/2016-11-07.xlsx");
	//InputStream is = new FileInputStream("./Reference_LightIntensity/2016-11-07.xlsx");
	//InputStream is = new FileInputStream("./Reflectivity/2016-11-07.xlsx");
	//InputStream is = new FileInputStream("./Background_LightIntensity/2016-11-07.xlsx");
    InputStream is = new FileInputStream("C://Users//hsp//Desktop//123.xlsx");
	XSSFWorkbook excel=new XSSFWorkbook(is);	
	XSSFSheet sheet=excel.getSheetAt(0);	
	XSSFRow	row=sheet.getRow(0);
	//excelһ��n��m�У�POI��������0-(n-1)������0-m
	System.out.println(sheet.getFirstRowNum()+", "+sheet.getLastRowNum());
	System.out.println(row.getFirstCellNum()+", "+row.getLastCellNum());
	//ѭ��д������
	XSSFRow	nRow=sheet.createRow(sheet.getLastRowNum()+1);
	String[] s=new String[]{"a","b","c","d","e"};
	for(int i=0;i<s.length;i++){
	XSSFCell cell=nRow.createCell(i);
	cell.setCellValue(s[i]);;		
	}
	//FileOutputStream FOS=new FileOutputStream("./Absorbance(excel)/2016-11-07.xlsx");
		//FileOutputStream FOS=new FileOutputStream("./Sample_LightIntensity/2016-11-07.xlsx");
		//FileOutputStream FOS=new FileOutputStream("./Reference_LightIntensity/2016-11-07.xlsx");
		//FileOutputStream FOS=new FileOutputStream("./Background_LightIntensity/2016-11-07.xlsx");
		//FileOutputStream FOS=new FileOutputStream("./Reflectivity/2016-11-07.xlsx");
		FileOutputStream FOS=new FileOutputStream("C://Users//hsp//Desktop//123.xlsx");
	    excel.write(FOS);
	    is.close();
	    FOS.close();
}
/**
 * excel �ļ���ֵ����
 * @throws IOException
 */
public static void Calculation10() throws IOException{
	InputStream is = new FileInputStream("C://Users//hsp//Desktop//123.xlsx");
	XSSFWorkbook excel=new XSSFWorkbook(is);
	XSSFSheet sheet=excel.getSheetAt(0);		
	XSSFRow	row=sheet.getRow(0);
	
	int index=99;
	for(int i=0;i<row.getLastCellNum();i++){
		XSSFCell cell=row.getCell(i);
	if(cell.getCellType()==Cell.CELL_TYPE_STRING){
		index=i;
		break;
	}	
	}

	System.out.println(index);
}
}
