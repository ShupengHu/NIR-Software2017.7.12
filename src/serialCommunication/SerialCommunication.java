package serialCommunication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public abstract class SerialCommunication{
	/*---串口参数设置---*/
	static String portName="COM4";                                   //需要使用的串口名
	static int timeout=10;                                           //超时等待时间:ms
	static int BaudRate=9600;                                        //波特率
	static int Databits=SerialPort.DATABITS_8;                       //数据位
	static int Stopbits=SerialPort.STOPBITS_1;                       //停止位
	static int Parity=SerialPort.PARITY_NONE;                        //校验位
	static byte[] returnBuffer=null;                                 //电机返回数据
	static InputStream IS=null;
	static OutputStream OS=null;
	static CommPortIdentifier PortId=null;
	static SerialPort SPort=null;
	
public static void portWrite(byte[] command){	 
			try {
				//get port ID
				PortId=CommPortIdentifier.getPortIdentifier(portName);
				
				//open port(port holder, timeout),打开端口（串口所有者，超时等待时间:ms）
				try {
					SPort=(SerialPort) PortId.open("可爱的皮卡丘",timeout);
					
					//set port(BaudRate,Databits,Stopbits,Parity),设置串口（波特率，数据位，停止位，校验位）
					try {
						SPort.setSerialPortParams(BaudRate, Databits, Stopbits, Parity);
					} catch (UnsupportedCommOperationException e) {
						e.printStackTrace();
					}
					
					//建立输出流
					try {
						//写入数据
						OS = SPort.getOutputStream();
						OS.write(command);
						OS.flush();
						OS.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					SPort.close();
				} catch (PortInUseException e) {
					e.printStackTrace();
				}
			} catch (NoSuchPortException e) {
				e.printStackTrace();
			}
}
}
