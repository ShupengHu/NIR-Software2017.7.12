package computation;

public class Reflectivity_Absorbance {
	double[] reflectivity=null;      //������
	double[] absorbance=null;        //�����
/**
 * ��������
 * @param R_lightIntensities �ο�����
 * @param B_lightIntensities ��������
 * @param S_lightIntensities ��Ʒ����
 */
public Reflectivity_Absorbance(double[] R_lightIntensities,double[] B_lightIntensities,double[] S_lightIntensities){
	this.reflectivity=new double[S_lightIntensities.length];
	this.absorbance=new double[S_lightIntensities.length];
	R_Computation(R_lightIntensities, B_lightIntensities, S_lightIntensities);
	A_Computation();
}
/**
 * ���㷴����: ������=(��������-��������)/(�ο�����-��������)*100%
 * @param R_lightIntensities �ο�����
 * @param B_lightIntensities ��������
 * @param s_lightIntensities ��Ʒ����
 */
public void R_Computation(double[] R_lightIntensities,double[] B_lightIntensities,double[] S_lightIntensities){
	double[] a = new double[S_lightIntensities.length];    //��������-��������
	double[] b = new double[S_lightIntensities.length];    //�ο�����-��������
	for(int i=0;i<S_lightIntensities.length;i++){
		a[i]=S_lightIntensities[i]-B_lightIntensities[i];
		b[i]=R_lightIntensities[i]-B_lightIntensities[i];
		this.reflectivity[i]=a[i]/b[i];
	}
}
/**
 * ���������: =-log10(������)
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
