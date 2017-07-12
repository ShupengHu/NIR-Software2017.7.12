package computation;

public class Reflectivity_Absorbance {
	double[] reflectivity=null;      //反射率
	double[] absorbance=null;        //吸光度
/**
 * 方法构造
 * @param R_lightIntensities 参考光谱
 * @param B_lightIntensities 背景光谱
 * @param S_lightIntensities 样品光谱
 */
public Reflectivity_Absorbance(double[] R_lightIntensities,double[] B_lightIntensities,double[] S_lightIntensities){
	this.reflectivity=new double[S_lightIntensities.length];
	this.absorbance=new double[S_lightIntensities.length];
	R_Computation(R_lightIntensities, B_lightIntensities, S_lightIntensities);
	A_Computation();
}
/**
 * 计算反射率: 反射率=(样本光谱-背景光谱)/(参考光谱-背景光谱)*100%
 * @param R_lightIntensities 参考光谱
 * @param B_lightIntensities 背景光谱
 * @param s_lightIntensities 样品光谱
 */
public void R_Computation(double[] R_lightIntensities,double[] B_lightIntensities,double[] S_lightIntensities){
	double[] a = new double[S_lightIntensities.length];    //样本光谱-背景光谱
	double[] b = new double[S_lightIntensities.length];    //参考光谱-背景光谱
	for(int i=0;i<S_lightIntensities.length;i++){
		a[i]=S_lightIntensities[i]-B_lightIntensities[i];
		b[i]=R_lightIntensities[i]-B_lightIntensities[i];
		this.reflectivity[i]=a[i]/b[i];
	}
}
/**
 * 计算吸光度: =-log10(反射率)
 */
public void A_Computation(){
	for(int i=0;i<this.absorbance.length;i++){
		this.absorbance[i]=-Math.log10(this.reflectivity[i]);
	}
}
public double[] getReflectivity(){
	return this.reflectivity;
}
public double[] getAbsorbance(){
	return this.absorbance;
}
}
