package preprocess;
import SNV.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mathworks.toolbox.javabuilder.*;
/**
 * SNV(Standard Normal Variate Transformation)标准正太变量变换
 * function [B]=SNV(A) A为实时扫描的吸光度；B为经过SNV处理后的返回矩阵
 * @author Shupeng Hu
 */
public class SNV_MATLAB {
	private Object[] result=null;
	private double[][] Result=null;
	public SNV_MATLAB(double[] absorbance) throws MWException, IOException{								
		snv s=new snv();
		/*-----------将一元数组改为二元数组---------*/
		//设置矩阵大小
		int[] n={1,absorbance.length};
		MWNumericArray matrix=MWNumericArray.newInstance(n, MWClassID.DOUBLE, MWComplexity.REAL);
		/* Set matrix values */  
	    int[] index = {1, 1}; 
		for (index[0]= 1; index[0] <= n[0]; index[0]++) {  
		       for (index[1] = 1; index[1] <= n[1]; index[1]++) {  
		    	   //MATLAB中矩阵第一个元素位置为（1,1), Java 二维数组第一个元素位置为（0,0）
		           matrix.set(index,absorbance[index[1]-1]);  
		       }  
		    }  
        //调用MATLAB进行运算
		this.result=s.SNV(1,matrix);		
        //把MATLAB返回的结果转换成double[][]
		MWNumericArray a=(MWNumericArray) this.result[0];
		this.Result=(double[][]) a.toDoubleArray();		
		//把SNV处理过的数据储存到Excel文件
		InputStream is = new FileInputStream("AfterSNV.xlsx");
		XSSFWorkbook Excel=new XSSFWorkbook(is);			
		XSSFSheet Sheet=Excel.getSheetAt(0);		
		XSSFRow	row=Sheet.createRow(Sheet.getLastRowNum()+1);
		
			for(int m=0;m<this.Result[0].length;m++){
				//BigDecimal bb = new BigDecimal(this.Result[n1][m]);
				//double B=bb.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue(); 		
				XSSFCell cell=row.createCell(m);
				cell.setCellValue(Result[0][m]);
			}
			FileOutputStream FOS=new FileOutputStream("AfterSNV.xlsx");
			Excel.write(FOS);
		
	}
	public double[][] getSNVResult(){
		return this.Result;
	}
}