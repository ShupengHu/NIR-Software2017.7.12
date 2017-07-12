package calibration;
import java.io.FileNotFoundException;
import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWComplexity;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import PLS_Java.Computing;
//import PLS_Java.PLS;
/**
 * PLS(Partial Least Squares) 偏最小二乘法
 * function [YY]=PLS_Java(X,Y,X1,PC) YY是处理后返回的结果；X是模型的吸光度；Y是模型的化学值；X1是经过预处理的吸光度;PC是主成分数
 * 化学值Y的矩阵的行表示样品数，列表示成分种类数量
 * @author shupengh
 */
public class PLS_Algorithm {
	private Object[] result=null;
	private double[][] concentration=null;
	/**
	 * B已知，只需要实时吸光度X1作为输入，X,Y,PC都暂时默认为1
	 * @param model_absorbance
	 * @param model_chemicalValue
	 * @param afterPreprocess
	 * @param PC
	 * @throws MWException
	 * @throws FileNotFoundException
	 */
//public PLS_Algorithm(double[][] model_absorbance,double[][] model_chemicalValue, double[][] afterPreprocess,int PC) throws MWException, FileNotFoundException{	
public PLS_Algorithm(double[][] afterPreprocess) throws MWException, FileNotFoundException{
	Computing pls=new Computing();
            //设置矩阵
			//int[] n1={model_absorbance.length,model_absorbance[0].length};
			//int[] n2={model_chemicalValue.length,model_chemicalValue[0].length};
			int[] n3={afterPreprocess.length,afterPreprocess[0].length};
			//MWNumericArray X=MWNumericArray.newInstance(n1, MWClassID.DOUBLE, MWComplexity.REAL);
			//MWNumericArray Y=MWNumericArray.newInstance(n2, MWClassID.DOUBLE, MWComplexity.REAL);
			MWNumericArray X1=MWNumericArray.newInstance(n3, MWClassID.DOUBLE, MWComplexity.REAL);
			
			/* Set matrix values */  
			int[] index = {1, 1}; 
			/*		    int[] index = {1, 1}; 
			for (index[0]= 1; index[0] <= n1[0]; index[0]++) {  
			       for (index[1] = 1; index[1] <= n1[1]; index[1]++) {  
			    	   //MATLAB中矩阵第一个元素位置为（1,1), Java 二维数组第一个元素位置为（0,0）
			           X.set(index,model_absorbance[index[0]-1][index[1]-1]);  
			       }  
			    }  
			for (index[0]= 1; index[0] <= n2[0]; index[0]++) {  
			       for (index[1] = 1; index[1] <= n2[1]; index[1]++) {  
			    	   //MATLAB中矩阵第一个元素位置为（1,1), Java 二维数组第一个元素位置为（0,0）
			           Y.set(index,model_chemicalValue[index[0]-1][index[1]-1]);  
			       }  
			    }  
			    */
			for (index[0]= 1; index[0] <= n3[0]; index[0]++) {  
			       for (index[1] = 1; index[1] <= n3[1]; index[1]++) {  
			    	   //MATLAB中矩阵第一个元素位置为（1,1), Java 二维数组第一个元素位置为（0,0）
			           X1.set(index,afterPreprocess[index[0]-1][index[1]-1]);  
			       }  
			    }  
			//调用MATLAB进行计算
			this.result=pls.PLS_Java(1, 1,1,X1,1);
			//把MATLAB返回的结果转换成double[][]
			MWNumericArray a=(MWNumericArray) this.result[0];
			this.concentration=(double[][]) a.toDoubleArray();	
			System.out.println("返回的浓度种类个数是    "+this.concentration[0].length);
}
public double[][] getConcentration(){
	return this.concentration;
}
}
