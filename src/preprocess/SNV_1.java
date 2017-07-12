package preprocess;
/**
 * SNV(Standard Normal Variate Transformation)��׼��̫�����任
 * @author Shupeng Hu
 */
public class SNV_1 {
int m=0;                                     //��������
double Absorbance_average=0;                 //ƽ�������
double Absorbance_sd=0;                      //����ȱ�׼��
double Absorbance_SNV=0;                     //����SNV������ĳһ�������������
double[] absorbance_SNV=new double[256];     //����SNV����󣬵õ��µ�һ�����������
/**
 * ʹ��SNV����ÿ��������������ȣ��õ��µ�һ�����������
 * @param wavelengthPoints ��������
 */
public SNV_1(double[] absorbance){
	this.m=absorbance.length;
	calculateA_average(absorbance);
	calcalateA_sd(absorbance);
	calcualteA_SNV(absorbance);	
}
/**
 * ����ƽ�������
 * @param absorbance
 */
public void calculateA_average(double[] absorbance){
	double A_sum=0;
	for(double x:absorbance){
		A_sum=A_sum+x;
	}
	this.Absorbance_average=A_sum/this.m;	
}
/**
 * ��������ȱ�׼�standard deviation��
 * @param absorbance
 */
public void calcalateA_sd(double[] absorbance){
	double A_sum = 0,a=0,b=0,c=0;
	for(double x:absorbance){
		a=x-this.Absorbance_average;
		b=Math.pow(a, 2);
		A_sum=A_sum+b;
	}
	c=A_sum/(this.m-1);
	this.Absorbance_sd=Math.sqrt(c);
}
/**
 * ���㾭��SNV������ĳһ�������������
 * @param absorbance
 */
public void calcualteA_SNV(double[] absorbance){
	for(int i=0;i<absorbance.length;i++){
	this.Absorbance_SNV=(absorbance[i]-this.Absorbance_average)/this.Absorbance_sd;
	this.absorbance_SNV[i]=this.Absorbance_SNV;
	}
}
/**
 * ���ƽ�������
 * @return
 */
public double getA_average(){
	return this.Absorbance_average;
}
/**
 * �������ȱ�׼��
 * @return
 */
public double getA_sd(){
	return this.Absorbance_sd;
}
/**
 * ��þ���SNV����󣬵õ��µ�һ�����������
 * @return
 */
public double[] getabsorbance_SNV(){
	return this.absorbance_SNV;
}
}
