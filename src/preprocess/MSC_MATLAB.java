package preprocess;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.mathworks.toolbox.javabuilder.*;

import MSC_Java.MSC;
/**
 * MSC(Multiplicative Scatter Correction)��Ԫɢ��У��
 * function [Xcorrect]=msc(X,Xref) XcorrectΪ����MSC�����ķ��ؾ���XΪʵʱɨ�������ȣ�XrefΪģ�͵�����Ⱦ���
 * @author Shupeng Hu
 */
public class MSC_MATLAB {
	private Object[] result=null;
	private double[][] Result=null;
	public MSC_MATLAB(double[] absorbance,double[][] model_absorbance) throws MWException, IOException{								
        MSC msc=new MSC();
        /*-----------��һԪ�����Ϊ��Ԫ����---------*/
		//���þ����С
		int[] n={1,absorbance.length};
		int[] n1={model_absorbance.length,model_absorbance[0].length};
		MWNumericArray X=MWNumericArray.newInstance(n, MWClassID.DOUBLE, MWComplexity.REAL);
		MWNumericArray Xref=MWNumericArray.newInstance(n1, MWClassID.DOUBLE, MWComplexity.REAL);
		/* Set matrix values */  
	    int[] index = {1, 1}; 
		for (index[0]= 1; index[0] <= n[0]; index[0]++) {  
		       for (index[1] = 1; index[1] <= n[1]; index[1]++) {  
		    	   //MATLAB�о����һ��Ԫ��λ��Ϊ��1,1), Java ��ά�����һ��Ԫ��λ��Ϊ��0,0��
		          X.set(index,absorbance[index[1]-1]);  
		       }  
		    }  
		for (index[0]= 1; index[0] <= n1[0]; index[0]++) {  
		       for (index[1] = 1; index[1] <= n1[1]; index[1]++) {  
		    	   //MATLAB�о����һ��Ԫ��λ��Ϊ��1,1), Java ��ά�����һ��Ԫ��λ��Ϊ��0,0��
		           Xref.set(index,model_absorbance[index[0]-1][index[1]-1]);  
		       }  
		    }  
		
        //����MATLAB��������
		this.result=msc.msc_Java(1, X,Xref);
        //��MATLAB���صĽ��ת����double[][]
		MWNumericArray a=(MWNumericArray) this.result[0];
		this.Result=(double[][]) a.toDoubleArray();		
		//��MSC����������ݴ��浽Excel�ļ�
		InputStream is = new FileInputStream("AfterMSC.xlsx");
		XSSFWorkbook Excel=new XSSFWorkbook(is);			
		XSSFSheet Sheet=Excel.getSheetAt(0);		
		XSSFRow	row=Sheet.createRow(Sheet.getLastRowNum()+1);
		
			for(int m=0;m<this.Result[0].length;m++){
				//BigDecimal bb = new BigDecimal(this.Result[n1][m]);
				//double B=bb.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue(); 		
				XSSFCell cell=row.createCell(m);
				cell.setCellValue(Result[0][m]);
			}
			FileOutputStream FOS=new FileOutputStream("AfterMSC.xlsx");
			Excel.write(FOS);
		
	}
	public double[][] getMSCResult(){
		return this.Result;
	}
}