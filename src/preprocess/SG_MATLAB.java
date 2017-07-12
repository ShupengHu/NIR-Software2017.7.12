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
 * Savitzky-Golay 平滑
 * function x_sg= savgol(x,width,order,deriv) dx是经过处理后返回的矩阵；x是实时扫描的吸光度;width是窗口宽度(取值为大于等于3的奇数)；order是多项式次数(取值为小于等于5且小于等于window-1);deriv是导数阶数
 * @author shupengh
 */
public class SG_MATLAB {
	private Object[] result=null;
	private double[][] Result=null;
public SG_MATLAB(double[] absorbance,int der,int win_num, int poly_order) throws MWException, IOException{
	SG sg=new SG();
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
	this.result=sg.savgol(1, matrix,(double)win_num,(double)poly_order,(double)der);
	//把MATLAB返回的结果转换成double[][]
	MWNumericArray a=(MWNumericArray) this.result[0];
	this.Result=(double[][]) a.toDoubleArray();	
	//把SNV处理过的数据储存到Excel文件
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