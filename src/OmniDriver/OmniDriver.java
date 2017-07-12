package OmniDriver;
import java.io.IOException;

import com.oceanoptics.omnidriver.api.wrapper.Wrapper;
import com.oceanoptics.omnidriver.features.thermoelectric.ThermoElectricWrapper;
public class OmniDriver {
	Wrapper wrapper;     
	int spectrometer_quantity=0;            //���ù���������
	int spectrometer_index=0;               //��һ��������
	String spectrometer_name="";            //ָ�������ǵ�����
	String spectrometer_id="";              //ָ�������ǵ����к�
	String spectrometer_version="";         //ָ�������ǵĹ̼��汾
	double[] lightIntensities;              //ָ�������ǵĹ�ǿ
	double[] waveLengths;                   //ָ�������ǵĲ���
/**
 * ��������
 * �½�������ʵ�����õ����ù����Ǹ���	
 * @throws IOException 
 */
public OmniDriver() throws IOException{	
	//�½�ʵ��
	this.wrapper=new Wrapper();
	//���ù���������,���ع���������
    this.spectrometer_quantity=wrapper.openAllSpectrometers();
    if(this.spectrometer_quantity==-1||this.spectrometer_quantity==0){
    	SpecQuantityException();
    	return;
    }
}
/**
 * ��ù����ǵ���������
 * �򿪷���
 * ����̽���������¶� -25C
 * @author Shupeng Hu
 * @throws IOException 
 */
public void OmniFeatures() throws IOException{
    //��ȡָ�������ǵ�����
	this.spectrometer_name=wrapper.getName(this.spectrometer_index);
	//��ȡָ�������ǵ����к�
	this.spectrometer_id=wrapper.getSerialNumber(this.spectrometer_index);
	//��ȡָ�������ǵĹ̼��汾
	this.spectrometer_version=wrapper.getFirmwareVersion(this.spectrometer_index);
	//�򿪷���
	
	//����̽���������¶�
	if(wrapper.isFeatureSupportedThermoElectric(0)){
		ThermoElectricWrapper tecController=wrapper.getFeatureControllerThermoElectric(0);
		tecController.setTECEnable(true);
		tecController.setDetectorSetPointCelsius(-25);
		//�򿪷���
		tecController.setFanEnable(true);
	}
	
}
/**
 * ͨ��OmniDriver���ù����ǵĲ���
 * ��Ҫ�˹����õĲ������£�
 * @param usec ����ʱ��
 * @param smoothingDegrees ƽ����
 * @param numberOfScansToAverage ƽ������
 * @author Shupeng Hu
 */
public void OmniOperations(int usec,int numberOfScansToAverage,int smoothingDegrees) {	
	//����ָ�������ǵĻ���ʱ��,΢��
	this.wrapper.setIntegrationTime(spectrometer_index, usec);
	//��ָ�������ǻ�õĹ��׽���ƽ��
	this.wrapper.setBoxcarWidth(spectrometer_index,smoothingDegrees);
	//��ָ�������ǽ���numberOfScansToAverageTogether������ɨ������ֵ
	this.wrapper.setScansToAverage(spectrometer_index, numberOfScansToAverage);
	this.wrapper.setCorrectForElectricalDark(0, 0); // disable electric dark correction

}
/**
 * ���ô���ģʽ
 * 0. Normal Mode
 * 1. External Software Trigger Mode
 * 2. External Synchronization Trigger Mode
 * 3. Hardware Trigger Mode
 * 4. Single-Shot Trigger Mode
 * @param modeIndex
 */
public void setMode(int modeIndex){
	wrapper.setExternalTriggerMode(0, modeIndex);
}
/**
 * ������ù�������������
 */
public void SpecQuantityException(){
	 if (spectrometer_quantity == -1)
     {
         System.out.println("Exception message: " + this.wrapper.getLastException());
         System.out.println("Stack trace:\n" + this.wrapper.getLastExceptionStackTrace());
     }
	 else if (spectrometer_quantity == 0)
     {
         System.out.println("No spectrometers were found. Exiting the application.");
     }
}
public void closeSpectrometers(){
	//�ر������Ѿ��򿪵Ĺ�����
	this.wrapper.closeAllSpectrometers();
}
public double[] getLigthIntensities(){
	//��ȡָ�������ǵĹ���
	 this.lightIntensities=wrapper.getSpectrum(this.spectrometer_index);
	return this.lightIntensities;
}
public double[] getWaveLengths(){
	//��ȡָ�������ǵĲ���
	this.waveLengths=wrapper.getWavelengths(this.spectrometer_index);
	return this.waveLengths;
}
public Wrapper getWrapper(){
	return this.wrapper;
}
public int getSpectrum_quantity(){
	return this.spectrometer_quantity;
}
public int getSpectrum_Index(){
	return this.spectrometer_index;
}
public String getName(){
	return this.spectrometer_name;
}
public String getID(){
	return this.spectrometer_id;
}
public String getVersion(){
	return this.spectrometer_version;
}
}
