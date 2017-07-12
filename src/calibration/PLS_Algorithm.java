package calibration;
import java.io.FileNotFoundException;
import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWComplexity;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import PLS_Java.Computing;
//import PLS_Java.PLS;
/**
 * PLS(Partial Least Squares) ƫ��С���˷�
 * function [YY]=PLS_Java(X,Y,X1,PC) YY�Ǵ���󷵻صĽ����X��ģ�͵�����ȣ�Y��ģ�͵Ļ�ѧֵ��X1�Ǿ���Ԥ����������;PC�����ɷ���
 * ��ѧֵY�ľ�����б�ʾ��Ʒ�����б�ʾ�ɷ���������
 * @author shupengh
 */
public class PLS_Algorithm {
	private Object[] result=null;
	private double[][] concentration=null;
	/**
	 * B��֪��ֻ��Ҫʵʱ�����X1��Ϊ���룬X,Y,PC����ʱĬ��Ϊ1
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
            //���þ���
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
			    	   //MATLAB�о����һ��Ԫ��λ��Ϊ��1,1), Java ��ά�����һ��Ԫ��λ��Ϊ��0,0��
			           X.set(index,model_absorbance[index[0]-1][index[1]-1]);  
			       }  
			    }  
			for (index[0]= 1; index[0] <= n2[0]; index[0]++) {  
			       for (index[1] = 1; index[1] <= n2[1]; index[1]++) {  
			    	   //MATLAB�о����һ��Ԫ��λ��Ϊ��1,1), Java ��ά�����һ��Ԫ��λ��Ϊ��0,0��
			           Y.set(index,model_chemicalValue[index[0]-1][index[1]-1]);  
			       }  
			    }  
			    */
			for (index[0]= 1; index[0] <= n3[0]; index[0]++) {  
			       for (index[1] = 1; index[1] <= n3[1]; index[1]++) {  
			    	   //MATLAB�о����һ��Ԫ��λ��Ϊ��1,1), Java ��ά�����һ��Ԫ��λ��Ϊ��0,0��
			           X1.set(index,afterPreprocess[index[0]-1][index[1]-1]);  
			       }  
			    }  
			//����MATLAB���м���
			this.result=pls.PLS_Java(1, 1,1,X1,1);
			//��MATLAB���صĽ��ת����double[][]
			MWNumericArray a=(MWNumericArray) this.result[0];
			this.concentration=(double[][]) a.toDoubleArray();	
			System.out.println("���ص�Ũ�����������    "+this.concentration[0].length);
}
public double[][] getConcentration(){
	return this.concentration;
}
}
