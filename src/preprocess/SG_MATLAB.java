package preprocess;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWComplexity;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import savgol.SG;


/**
 * Savitzky-Golay ƽ��
 * function x_sg= savgol(x,width,order,deriv) dx�Ǿ�������󷵻صľ���x��ʵʱɨ��������;width�Ǵ��ڿ��(ȡֵΪ���ڵ���3������)��order�Ƕ���ʽ����(ȡֵΪС�ڵ���5��С�ڵ���window-1);deriv�ǵ�������
 * @author shupengh
 */
public class SG_MATLAB {
	private Object[] result=null;
	private double[][] Result=null;
public SG_MATLAB(double[] absorbance,int der,int win_num, int poly_order) throws MWException, IOException{
	SG sg=new SG();
	//���þ����С
	int[] n={1,absorbance.length};
	MWNumericArray matrix=MWNumericArray.newInstance(n, MWClassID.DOUBLE, MWComplexity.REAL);
	/* Set matrix values */  
	int[] index = {1, 1}; 
	for (index[0]= 1; index[0] <= n[0]; index[0]++) {  
	for (index[1] = 1; index[1] <= n[1]; index[1]++) {  
	 //MATLAB�о����һ��Ԫ��λ��Ϊ��1,1), Java ��ά�����һ��Ԫ��λ��Ϊ��0,0��
	 matrix.set(index,absorbance[index[1]-1]);  
	 }  
  }  
	//����MATLAB��������
	this.result=sg.savgol(1, matrix,(double)win_num,(double)poly_order,(double)der);
	//��MATLAB���صĽ��ת����double[][]
	MWNumericArray a=(MWNumericArray) this.result[0];
	this.Result=(double[][]) a.toDoubleArray();	
	//��SNV����������ݴ��浽Excel�ļ�
	InputStream is = new FileInputStream("AfterSG.xlsx");
	XSSFWorkbook Excel=new XSSFWorkbook(is);			
	XSSFSheet Sheet=Excel.getSheetAt(0);		
	XSSFRow	row=Sheet.createRow(Sheet.getLastRowNum()+1);
			
			for(int m=0;m<this.Result[0].length;m++){
			//BigDecimal bb = new BigDecimal(this.Result[n1][m]);
			//double B=bb.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue(); 		
			XSSFCell cell=row.createCell(m);
			cell.setCellValue(Result[0][m]);
		    }
			FileOutputStream FOS=new FileOutputStream("AfterSG.xlsx");
			Excel.write(FOS);
}
public double[][] getSGResult(){
	return this.Result;
}
}