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
	ArrayList<String> sPorts=new ArrayList<String>();         //存储所有可用串口名
	String portName="COM4";                                   //需要使用的串口名
	SerialPort sPort;                                         //需要打开的串口
	int timeout=10;                                           //超时等待时间:ms
	byte[] stop={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x10,(byte) 0xff,0x65};   //静止指令
	byte[] forward={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x11,(byte) 0xff,0x65};   //前进指令
	byte[] backward={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x01,(byte) 0xff,0x65};   //后退指令
	byte[] positionCheck={0x01,0x03,0x00,0x00,0x00,0x02,(byte) 0xff,0x65};        //电机复位自检指令
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
 * 获得所有可用串口
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
 * 通过串口向单片机写入数据
 */
public boolean portWriteAndRead(){	
	try {
		//get port ID
		CommPortIdentifier portId=CommPortIdentifier.getPortIdentifier(this.portName);	

		//open port(port holder, timeout),打开端口（串口所有者，超时等待时间:ms）
		try {
			this.sPort=(SerialPort) portId.open("可爱的皮卡丘", this.timeout);

			//set port(BaudRate,Databits,Stopbits,Parity),设置串口（波特率，数据位，停止位，校验位）
			try {
				this.sPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			} catch (UnsupportedCommOperationException e) {
				e.printStackTrace();
			}		
			
			//写入数据
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
			//选择操作
			System.out.println("选择一下操作： 静止按1； 前进按2； 后退按3; 位置自检按4，结束操作,5.");
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
	case SerialPortEvent.BI:/*Break interrupt,通讯中断*/ 
		JOptionPane.showMessageDialog(null, "与串口设备通讯中断", "错误", JOptionPane.INFORMATION_MESSAGE);
        break;
    case SerialPortEvent.OE:/*Overrun error，溢位错误*/ 
    	JOptionPane.showMessageDialog(null, "溢位错误", "错误", JOptionPane.INFORMATION_MESSAGE);
        break;
    case SerialPortEvent.FE:/*Framing error，传帧错误*/
    	JOptionPane.showMessageDialog(null, "传帧错误", "错误", JOptionPane.INFORMATION_MESSAGE);
        break;
    case SerialPortEvent.PE:/*Parity error，校验错误*/
    	JOptionPane.showMessageDialog(null, "校验错误", "错误", JOptionPane.INFORMATION_MESSAGE);
        break;
    case SerialPortEvent.CD:/*Carrier detect，载波检测*/
    case SerialPortEvent.CTS:/*Clear to send，清除发送*/ 
    case SerialPortEvent.DSR:/*Data set ready，数据设备就绪*/ 
    case SerialPortEvent.RI:/*Ring indicator，响铃指示*/
    case SerialPortEvent.OUTPUT_BUFFER_EMPTY:/*Output buffer is empty，输出缓冲区清空*/ 
        break;
	case SerialPortEvent.DATA_AVAILABLE: 
        try {  
        	//建立输入流
        System.out.println("--------------返回数据长度-------------  "+this.IS.available());
            //获取返回数据
        //this.returnBuffer=new byte[9];
         //---------------inputstream.available()方法
           while(IS.available()>0){
        	   //IS.read(returnBuffer);
           }           
        System.out.println("-----------返回数据------------   "+Arrays.toString(returnBuffer));  
        /*
        //-----------inputsream.read（）返回值-1方法
        int a=0;
        do{
        a=IS.read(returnBuffer);
        	System.out.println(Arrays.toString(returnBuffer));
        }while(a!=-1);
           //输出打印返回数据
        */
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
		;
	}
}
}
