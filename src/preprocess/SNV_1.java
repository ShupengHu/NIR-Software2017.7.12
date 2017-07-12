package preprocess;
/**
 * SNV(Standard Normal Variate Transformation)标准正太变量变换
 * @author Shupeng Hu
 */
public class SNV_1 {
int m=0;                                     //波长点数
double Absorbance_average=0;                 //平均吸光度
double Absorbance_sd=0;                      //吸光度标准差
double Absorbance_SNV=0;                     //经过SNV处理后的某一波长处的吸光度
double[] absorbance_SNV=new double[256];     //经过SNV处理后，得到新的一组吸光度数据
/**
 * 使用SNV处理每个波长处的吸光度，得到新的一组吸光度数据
 * @param wavelengthPoints 波长点数
 */
public SNV_1(double[] absorbance){
	this.m=absorbance.length;
	calculateA_average(absorbance);
	calcalateA_sd(absorbance);
	calcualteA_SNV(absorbance);	
}
/**
 * 计算平均吸光度
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
 * 计算吸光度标准差（standard deviation）
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
 * 计算经过SNV处理后的某一波长处的吸光度
 * @param absorbance
 */
public void calcualteA_SNV(double[] absorbance){
	for(int i=0;i<absorbance.length;i++){
	this.Absorbance_SNV=(absorbance[i]-this.Absorbance_average)/this.Absorbance_sd;
	this.absorbance_SNV[i]=this.Absorbance_SNV;
	}
}
/**
 * 获得平均吸光度
 * @return
 */
public double getA_average(){
	return this.Absorbance_average;
}
/**
 * 获得吸光度标准差
 * @return
 */
public double getA_sd(){
	return this.Absorbance_sd;
}
/**
 * 获得经过SNV处理后，得到新的一组吸光度数据
 * @return
 */
public double[] getabsorbance_SNV(){
	return this.absorbance_SNV;
}
}
