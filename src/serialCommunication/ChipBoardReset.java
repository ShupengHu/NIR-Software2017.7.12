package serialCommunication;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class ChipBoardReset implements SerialPortEventListener{
	ArrayList<String> sPorts=new ArrayList<String>();         //存储所有可用串口名
	String portName="COM4";                                   //需要使用的串口名
	CommPortIdentifier portId;                                //需要使用的端口
	SerialPort sPort;                                         //需要打开的串口
	int timeout=10;                                           //超时等待时间:ms
	byte[] stop={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x10,(byte) 0xff,0x65};   //静止指令
	byte[] forward={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x11,(byte) 0xff,0x65};   //前进指令
	byte[] backward={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x01,(byte) 0xff,0x65};   //后退指令
	byte[] positionCheck={0x01,0x03,0x00,0x00,0x00,0x02,(byte) 0xff,0x65};        //电机复位自检指令
	InputStream IS=null;
	OutputStream OS=null;
	byte[] returnBuffer=null;
	boolean portShouldClose=false;
	
	public static void main(String[] args) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, InterruptedException {
		ChipBoardReset CBR=new ChipBoardReset();
		if(CBR.getPortStatus().equalsIgnoreCase("closed")){
	        CBR.close();
	        System.out.println("------------复位成功，端口关闭---------------------");
			}
	}
	public ChipBoardReset() throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, InterruptedException {
		sPorts=getAllports();
		openPort();
		startRead();
		positionReset();	
	}
	/**
	 * 电机自动复位
	 * @throws InterruptedException 
	 */
	public void positionReset() throws InterruptedException{
		//----------------发送自检程序指令：010300000002ff65，先发一次，过一秒再发一次，收取第二次电机返回值---------//
		write(positionCheck);
		//等2秒
		Thread.sleep(1*1000);
		//第二次
		write(positionCheck);
		Thread.sleep(1*1000);
		/*
		判断位置：回复的指令010304xxxxxxxxFA33中第4个字节至第7个字节组成的浮点数是否为0
		如果为0，说明初始位置正常，自动校验和复位功能完成，如果不为0，则说明初始位置不正常，
		此时发送一次后退指令,如果收到的电机返回值为010304xxxxxxxxxx，则复位成功。否则，等待电机后退到原位。电机退回原位后会自动发送010304xxxxxxxxxx，表明复位成功.
		*/
		if(this.returnBuffer[2]!=0&&this.returnBuffer[3]==0&&this.returnBuffer[4]==0&&this.returnBuffer[5]==0&&this.returnBuffer[6]==0){
			this.portShouldClose=true;
			write(stop);
			System.out.println("------------在原位---------------------");
		}else{
			this.returnBuffer=null;
			write(backward);
			System.out.println("------------不在原位，后退---------------------");
			Thread.sleep(1*1000);
			do{
				if(this.returnBuffer!=null&&this.returnBuffer[0]==1&&this.returnBuffer[1]==3&&this.returnBuffer[2]==4){
					this.portShouldClose=true;	
					System.out.println("------------后退到原位---------------------");
				}else {
					Thread.sleep(1*1000);			    				
				    write(positionCheck);
				    Thread.sleep(1*1000);
			    if(this.returnBuffer[2]!=0&&this.returnBuffer[3]==0&&this.returnBuffer[4]==0&&this.returnBuffer[5]==0&&this.returnBuffer[6]==0){
						this.portShouldClose=true;
						System.out.println("------------后退到原位---------------------");
			    }
				}
			}while(this.portShouldClose==false);
			write(stop);
		}
	}
	/**
	 * 获得所有可用串口
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList<String> getAllports(){
		ArrayList<String> ports=new ArrayList<String>(); 
		Enumeration portList = CommPortIdentifier.getPortIdentifiers();

		while(portList.hasMoreElements()){
			this.portId=(CommPortIdentifier) portList.nextElement();
			if(this.portId.getPortType()==CommPortIdentifier.PORT_SERIAL){
				 ports.add(this.portId.getName());
			}
		}
		System.out.println(ports.toString());
		return ports;
	}
	/**
     * @throws NoSuchPortException 
	 * @throws PortInUseException 
	 * @throws UnsupportedCommOperationException 
	 * @方法名称 :openPort
     * @功能描述 :打开SerialPort
     * @返回值类型 :void
     */
    public void openPort() throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException{
        this.portId=CommPortIdentifier.getPortIdentifier(this.portName);	
    	this.sPort=(SerialPort) this.portId.open("可爱的皮卡丘", this.timeout);
    	this.sPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
    }
    /**
     * @方法名称 :checkPort
     * @功能描述 :检查端口是否正确连接
     * @返回值类型 :void
     */
    public void checkPort(){
        if(this.portId == null)
            throw new RuntimeException("没有选择端口，请使用 " +
                    "selectPort(String portName) 方法选择端口");
        
        if(this.sPort == null){
            throw new RuntimeException("SerialPort 对象无效！");
        }
    }
    /**
     * @方法名称 :write
     * @功能描述 :向端口发送数据，请在调用此方法前 先选择端口，并确定SerialPort正常打开！
     * @返回值类型 :void
     *    @param message
     */
    public void write(byte[] command) {
        checkPort();
        
        try{
            this.OS = this.sPort.getOutputStream();
        }catch(IOException e){
            throw new RuntimeException("获取端口的OutputStream出错："+e.getMessage());
        }
        
        try{
            this.OS.write(command);
            System.out.println("信息发送成功！");
        }catch(IOException e){
            throw new RuntimeException("向端口发送信息时出错："+e.getMessage());
        }finally{
            try{
                this.OS.close();
            }catch(Exception e){
            }
        }
    }
    /**
     * @方法名称 :startRead
     * @功能描述 :开始监听从端口中接收的数据
     * @返回值类型 :void
     *    @param time  监听程序的存活时间，单位为秒，0 则是一直监听
     */
    public void startRead(){
        checkPort();
        
        try{
            this.IS = new BufferedInputStream(this.sPort.getInputStream());
        }catch(IOException e){
            throw new RuntimeException("获取端口的InputStream出错："+e.getMessage());
        }
        
        try{
            this.sPort.addEventListener(this);
        }catch(TooManyListenersException e){
            throw new RuntimeException(e.getMessage());
        }
        
        this.sPort.notifyOnDataAvailable(true);
        
        System.out.println(String.format("开始监听来自端口的数据--------------", this.portId.getName()));       
    }
    /**
     * 数据接收的监听处理函数
     */
    public void serialEvent(SerialPortEvent arg0){
        switch(arg0.getEventType()){
        case SerialPortEvent.BI:/*Break interrupt,通讯中断*/ 
        case SerialPortEvent.OE:/*Overrun error，溢位错误*/ 
        case SerialPortEvent.FE:/*Framing error，传帧错误*/
        case SerialPortEvent.PE:/*Parity error，校验错误*/
        case SerialPortEvent.CD:/*Carrier detect，载波检测*/
        case SerialPortEvent.CTS:/*Clear to send，清除发送*/ 
        case SerialPortEvent.DSR:/*Data set ready，数据设备就绪*/ 
        case SerialPortEvent.RI:/*Ring indicator，响铃指示*/
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:/*Output buffer is empty，输出缓冲区清空*/ 
            break;
        case SerialPortEvent.DATA_AVAILABLE:/*Data available at the serial port，端口有可用数据。读到缓冲数组，输出到终端*/
        	try {
				System.out.println("--------------返回数据长度-------------  "+this.IS.available());
		        this.returnBuffer= new byte[10];

				while(this.IS.available()>0){			   
					this.IS.read(this.returnBuffer);
				}
				 System.out.println("-----------返回数据------------   "+Arrays.toString(this.returnBuffer));
				
			}catch (IOException e) {
				e.printStackTrace();
			}      
        }
        }
    /**
     * @方法名称 :close
     * @功能描述 :关闭 SerialPort
     * @返回值类型 :void
     */
    public void close(){
       this.sPort.close();
        this.sPort = null;
        this.portId = null;
    }
    /**
     * 返回端口是否需要关闭的状态
     * @return
     */
    public String getPortStatus(){
    	String status="";
    	if(this.portShouldClose==true){
    		status="closed";
    	}else status="open";
    	return status;
    }
}
