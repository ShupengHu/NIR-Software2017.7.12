package serialCommunication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.TooManyListenersException;

import javax.swing.JOptionPane;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class SerialCommunicationTest implements SerialPortEventListener{
	ArrayList<String> sPorts=new ArrayList<String>();         //�洢���п��ô�����
	String portName="COM4";                                   //��Ҫʹ�õĴ�����
	SerialPort sPort;                                         //��Ҫ�򿪵Ĵ���
	int timeout=10;                                           //��ʱ�ȴ�ʱ��:ms
	byte[] stop={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x10,(byte) 0xff,0x65};   //��ָֹ��
	byte[] forward={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x11,(byte) 0xff,0x65};   //ǰ��ָ��
	byte[] backward={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x01,(byte) 0xff,0x65};   //����ָ��
	byte[] positionCheck={0x01,0x03,0x00,0x00,0x00,0x02,(byte) 0xff,0x65};        //�����λ�Լ�ָ��
	InputStream IS=null;
	OutputStream OS=null;
	byte[] returnBuffer=null;
	static boolean flag=true;
	static Scanner s=new Scanner(System.in);
	
public static void main(String args[]){
	SerialCommunicationTest SC=new SerialCommunicationTest();
    SC.getAllports();
    /*
    do{
	flag=SC.portWriteAndRead();
    }while(flag==true);
    s.close();
    */
    SC.portWriteAndRead();
    //s.close();

	
}
/**
 * ������п��ô���
 */
public ArrayList<String> getAllports(){
	ArrayList<String> ports=new ArrayList<String>(); 
	@SuppressWarnings("rawtypes") 
	Enumeration portList = CommPortIdentifier.getPortIdentifiers();
	CommPortIdentifier portId;

	while(portList.hasMoreElements()){
		portId=(CommPortIdentifier) portList.nextElement();
		if(portId.getPortType()==CommPortIdentifier.PORT_SERIAL){
			 ports.add(portId.getName());
		}
	}
	System.out.println(ports.toString());
	return ports;
}
/**
 * ͨ��������Ƭ��д������
 */
public boolean portWriteAndRead(){	
	try {
		//get port ID
		CommPortIdentifier portId=CommPortIdentifier.getPortIdentifier(this.portName);	

		//open port(port holder, timeout),�򿪶˿ڣ����������ߣ���ʱ�ȴ�ʱ��:ms��
		try {
			this.sPort=(SerialPort) portId.open("�ɰ���Ƥ����", this.timeout);

			//set port(BaudRate,Databits,Stopbits,Parity),���ô��ڣ������ʣ�����λ��ֹͣλ��У��λ��
			try {
				this.sPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			} catch (UnsupportedCommOperationException e) {
				e.printStackTrace();
			}		
			
			//д������
			try {
				this.OS = sPort.getOutputStream();
				this.IS=sPort.getInputStream();
				sPort.addEventListener(this);
				sPort.notifyOnDataAvailable(true);	
			} catch (IOException | TooManyListenersException e) {
				e.printStackTrace();
			}
			this.OS.write(positionCheck);
			/*
			//ѡ�����
			System.out.println("ѡ��һ�²����� ��ֹ��1�� ǰ����2�� ���˰�3; λ���Լ찴4����������,5.");
			int command=s.nextInt();
			
				switch(command){
				case 1: try {
					this.OS.write(this.stop);
					System.out.println("------stop------");
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
				case 2: try {
					this.OS.write(this.forward);
					System.out.println("------forward------");
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
				case 3: try {
					this.OS.write(this.backward);
					System.out.println("------backward------");
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
				case 4: try{
					this.OS.write(this.positionCheck);
					System.out.println("------positionCheck------");
				}catch (IOException e) {
					e.printStackTrace();
				}
				break;
				case 5: flag=false;
				break;
			}	
			*/	
			try {
				this.OS.flush();
				this.OS.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//this.sPort.close();
			
		} catch (PortInUseException | IOException e) {
			e.printStackTrace();
		}	
	} catch (NoSuchPortException e) {
		e.printStackTrace();
	}
	return flag;
}
public void serialEvent(SerialPortEvent arg0) {
	switch(arg0.getEventType()){
	case SerialPortEvent.BI:/*Break interrupt,ͨѶ�ж�*/ 
		JOptionPane.showMessageDialog(null, "�봮���豸ͨѶ�ж�", "����", JOptionPane.INFORMATION_MESSAGE);
        break;
    case SerialPortEvent.OE:/*Overrun error����λ����*/ 
    	JOptionPane.showMessageDialog(null, "��λ����", "����", JOptionPane.INFORMATION_MESSAGE);
        break;
    case SerialPortEvent.FE:/*Framing error����֡����*/
    	JOptionPane.showMessageDialog(null, "��֡����", "����", JOptionPane.INFORMATION_MESSAGE);
        break;
    case SerialPortEvent.PE:/*Parity error��У�����*/
    	JOptionPane.showMessageDialog(null, "У�����", "����", JOptionPane.INFORMATION_MESSAGE);
        break;
    case SerialPortEvent.CD:/*Carrier detect���ز����*/
    case SerialPortEvent.CTS:/*Clear to send���������*/ 
    case SerialPortEvent.DSR:/*Data set ready�������豸����*/ 
    case SerialPortEvent.RI:/*Ring indicator������ָʾ*/
    case SerialPortEvent.OUTPUT_BUFFER_EMPTY:/*Output buffer is empty��������������*/ 
        break;
	case SerialPortEvent.DATA_AVAILABLE: 
        try {  
        	//����������
        System.out.println("--------------�������ݳ���-------------  "+this.IS.available());
            //��ȡ��������
        //this.returnBuffer=new byte[9];
         //---------------inputstream.available()����
           while(IS.available()>0){
        	   //IS.read(returnBuffer);
           }           
        System.out.println("-----------��������------------   "+Arrays.toString(returnBuffer));  
        /*
        //-----------inputsream.read��������ֵ-1����
        int a=0;
        do{
        a=IS.read(returnBuffer);
        	System.out.println(Arrays.toString(returnBuffer));
        }while(a!=-1);
           //�����ӡ��������
        */
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
		;
	}
}
}
