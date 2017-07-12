package OmniDriver;
import java.io.IOException;

import com.oceanoptics.omnidriver.api.wrapper.Wrapper;
import com.oceanoptics.omnidriver.features.thermoelectric.ThermoElectricWrapper;
public class OmniDriver {
	Wrapper wrapper;     
	int spectrometer_quantity=0;            //可用光谱仪数量
	int spectrometer_index=0;               //第一个光谱仪
	String spectrometer_name="";            //指定光谱仪的名称
	String spectrometer_id="";              //指定光谱仪的序列号
	String spectrometer_version="";         //指定光谱仪的固件版本
	double[] lightIntensities;              //指定光谱仪的光强
	double[] waveLengths;                   //指定光谱仪的波长
/**
 * 方法构造
 * 新建光谱仪实例，得到可用光谱仪个数	
 * @throws IOException 
 */
public OmniDriver() throws IOException{	
	//新建实例
	this.wrapper=new Wrapper();
	//可用光谱仪数量,返回光谱仪索引
    this.spectrometer_quantity=wrapper.openAllSpectrometers();
    if(this.spectrometer_quantity==-1||this.spectrometer_quantity==0){
    	SpecQuantityException();
    	return;
    }
}
/**
 * 获得光谱仪的特征参数
 * 打开风扇
 * 设置探测器工作温度 -25C
 * @author Shupeng Hu
 * @throws IOException 
 */
public void OmniFeatures() throws IOException{
    //获取指定光谱仪的名称
	this.spectrometer_name=wrapper.getName(this.spectrometer_index);
	//获取指定光谱仪的序列号
	this.spectrometer_id=wrapper.getSerialNumber(this.spectrometer_index);
	//获取指定光谱仪的固件版本
	this.spectrometer_version=wrapper.getFirmwareVersion(this.spectrometer_index);
	//打开风扇
	
	//设置探测器工作温度
	if(wrapper.isFeatureSupportedThermoElectric(0)){
		ThermoElectricWrapper tecController=wrapper.getFeatureControllerThermoElectric(0);
		tecController.setTECEnable(true);
		tecController.setDetectorSetPointCelsius(-25);
		//打开风扇
		tecController.setFanEnable(true);
	}
	
}
/**
 * 通过OmniDriver设置光谱仪的参数
 * 需要人工设置的参数如下：
 * @param usec 积分时间
 * @param smoothingDegrees 平滑度
 * @param numberOfScansToAverage 平均次数
 * @author Shupeng Hu
 */
public void OmniOperations(int usec,int numberOfScansToAverage,int smoothingDegrees) {	
	//设置指定光谱仪的积分时间,微秒
	this.wrapper.setIntegrationTime(spectrometer_index, usec);
	//对指定光谱仪获得的光谱进行平滑
	this.wrapper.setBoxcarWidth(spectrometer_index,smoothingDegrees);
	//对指定光谱仪进行numberOfScansToAverageTogether次连续扫描后求均值
	this.wrapper.setScansToAverage(spectrometer_index, numberOfScansToAverage);
	this.wrapper.setCorrectForElectricalDark(0, 0); // disable electric dark correction

}
/**
 * 设置触发模式
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
 * 报告可用光谱仪数量错误
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
	//关闭所有已经打开的光谱仪
	this.wrapper.closeAllSpectrometers();
}
public double[] getLigthIntensities(){
	//获取指定光谱仪的光谱
	 this.lightIntensities=wrapper.getSpectrum(this.spectrometer_index);
	return this.lightIntensities;
}
public double[] getWaveLengths(){
	//获取指定光谱仪的波长
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
