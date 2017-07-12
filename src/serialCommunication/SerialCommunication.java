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
	/*---���ڲ�������---*/
	static String portName="COM4";                                   //��Ҫʹ�õĴ�����
	static int timeout=10;                                           //��ʱ�ȴ�ʱ��:ms
	static int BaudRate=9600;                                        //������
	static int Databits=SerialPort.DATABITS_8;                       //����λ
	static int Stopbits=SerialPort.STOPBITS_1;                       //ֹͣλ
	static int Parity=SerialPort.PARITY_NONE;                        //У��λ
	static byte[] returnBuffer=null;                                 //�����������
	static InputStream IS=null;
	static OutputStream OS=null;
	static CommPortIdentifier PortId=null;
	static SerialPort SPort=null;
	
public static void portWrite(byte[] command){	 
			try {
				//get port ID
				PortId=CommPortIdentifier.getPortIdentifier(portName);
				
				//open port(port holder, timeout),�򿪶˿ڣ����������ߣ���ʱ�ȴ�ʱ��:ms��
				try {
					SPort=(SerialPort) PortId.open("�ɰ���Ƥ����",timeout);
					
					//set port(BaudRate,Databits,Stopbits,Parity),���ô��ڣ������ʣ�����λ��ֹͣλ��У��λ��
					try {
						SPort.setSerialPortParams(BaudRate, Databits, Stopbits, Parity);
					} catch (UnsupportedCommOperationException e) {
						e.printStackTrace();
					}
					
					//���������
					try {
						//д������
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
