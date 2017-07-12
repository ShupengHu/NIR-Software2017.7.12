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
	ArrayList<String> sPorts=new ArrayList<String>();         //�洢���п��ô�����
	String portName="COM4";                                   //��Ҫʹ�õĴ�����
	CommPortIdentifier portId;                                //��Ҫʹ�õĶ˿�
	SerialPort sPort;                                         //��Ҫ�򿪵Ĵ���
	int timeout=10;                                           //��ʱ�ȴ�ʱ��:ms
	byte[] stop={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x10,(byte) 0xff,0x65};   //��ָֹ��
	byte[] forward={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x11,(byte) 0xff,0x65};   //ǰ��ָ��
	byte[] backward={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x01,(byte) 0xff,0x65};   //����ָ��
	byte[] positionCheck={0x01,0x03,0x00,0x00,0x00,0x02,(byte) 0xff,0x65};        //�����λ�Լ�ָ��
	InputStream IS=null;
	OutputStream OS=null;
	byte[] returnBuffer=null;
	boolean portShouldClose=false;
	
	public static void main(String[] args) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, InterruptedException {
		ChipBoardReset CBR=new ChipBoardReset();
		if(CBR.getPortStatus().equalsIgnoreCase("closed")){
	        CBR.close();
	        System.out.println("------------��λ�ɹ����˿ڹر�---------------------");
			}
	}
	public ChipBoardReset() throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, InterruptedException {
		sPorts=getAllports();
		openPort();
		startRead();
		positionReset();	
	}
	/**
	 * ����Զ���λ
	 * @throws InterruptedException 
	 */
	public void positionReset() throws InterruptedException{
		//----------------�����Լ����ָ�010300000002ff65���ȷ�һ�Σ���һ���ٷ�һ�Σ���ȡ�ڶ��ε������ֵ---------//
		write(positionCheck);
		//��2��
		Thread.sleep(1*1000);
		//�ڶ���
		write(positionCheck);
		Thread.sleep(1*1000);
		/*
		�ж�λ�ã��ظ���ָ��010304xxxxxxxxFA33�е�4���ֽ�����7���ֽ���ɵĸ������Ƿ�Ϊ0
		���Ϊ0��˵����ʼλ���������Զ�У��͸�λ������ɣ������Ϊ0����˵����ʼλ�ò�������
		��ʱ����һ�κ���ָ��,����յ��ĵ������ֵΪ010304xxxxxxxxxx����λ�ɹ������򣬵ȴ�������˵�ԭλ������˻�ԭλ����Զ�����010304xxxxxxxxxx��������λ�ɹ�.
		*/
		if(this.returnBuffer[2]!=0&&this.returnBuffer[3]==0&&this.returnBuffer[4]==0&&this.returnBuffer[5]==0&&this.returnBuffer[6]==0){
			this.portShouldClose=true;
			write(stop);
			System.out.println("------------��ԭλ---------------------");
		}else{
			this.returnBuffer=null;
			write(backward);
			System.out.println("------------����ԭλ������---------------------");
			Thread.sleep(1*1000);
			do{
				if(this.returnBuffer!=null&&this.returnBuffer[0]==1&&this.returnBuffer[1]==3&&this.returnBuffer[2]==4){
					this.portShouldClose=true;	
					System.out.println("------------���˵�ԭλ---------------------");
				}else {
					Thread.sleep(1*1000);			    				
				    write(positionCheck);
				    Thread.sleep(1*1000);
			    if(this.returnBuffer[2]!=0&&this.returnBuffer[3]==0&&this.returnBuffer[4]==0&&this.returnBuffer[5]==0&&this.returnBuffer[6]==0){
						this.portShouldClose=true;
						System.out.println("------------���˵�ԭλ---------------------");
			    }
				}
			}while(this.portShouldClose==false);
			write(stop);
		}
	}
	/**
	 * ������п��ô���
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
	 * @�������� :openPort
     * @�������� :��SerialPort
     * @����ֵ���� :void
     */
    public void openPort() throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException{
        this.portId=CommPortIdentifier.getPortIdentifier(this.portName);	
    	this.sPort=(SerialPort) this.portId.open("�ɰ���Ƥ����", this.timeout);
    	this.sPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
    }
    /**
     * @�������� :checkPort
     * @�������� :���˿��Ƿ���ȷ����
     * @����ֵ���� :void
     */
    public void checkPort(){
        if(this.portId == null)
            throw new RuntimeException("û��ѡ��˿ڣ���ʹ�� " +
                    "selectPort(String portName) ����ѡ��˿�");
        
        if(this.sPort == null){
            throw new RuntimeException("SerialPort ������Ч��");
        }
    }
    /**
     * @�������� :write
     * @�������� :��˿ڷ������ݣ����ڵ��ô˷���ǰ ��ѡ��˿ڣ���ȷ��SerialPort�����򿪣�
     * @����ֵ���� :void
     *    @param message
     */
    public void write(byte[] command) {
        checkPort();
        
        try{
            this.OS = this.sPort.getOutputStream();
        }catch(IOException e){
            throw new RuntimeException("��ȡ�˿ڵ�OutputStream����"+e.getMessage());
        }
        
        try{
            this.OS.write(command);
            System.out.println("��Ϣ���ͳɹ���");
        }catch(IOException e){
            throw new RuntimeException("��˿ڷ�����Ϣʱ����"+e.getMessage());
        }finally{
            try{
                this.OS.close();
            }catch(Exception e){
            }
        }
    }
    /**
     * @�������� :startRead
     * @�������� :��ʼ�����Ӷ˿��н��յ�����
     * @����ֵ���� :void
     *    @param time  ��������Ĵ��ʱ�䣬��λΪ�룬0 ����һֱ����
     */
    public void startRead(){
        checkPort();
        
        try{
            this.IS = new BufferedInputStream(this.sPort.getInputStream());
        }catch(IOException e){
            throw new RuntimeException("��ȡ�˿ڵ�InputStream����"+e.getMessage());
        }
        
        try{
            this.sPort.addEventListener(this);
        }catch(TooManyListenersException e){
            throw new RuntimeException(e.getMessage());
        }
        
        this.sPort.notifyOnDataAvailable(true);
        
        System.out.println(String.format("��ʼ�������Զ˿ڵ�����--------------", this.portId.getName()));       
    }
    /**
     * ���ݽ��յļ���������
     */
    public void serialEvent(SerialPortEvent arg0){
        switch(arg0.getEventType()){
        case SerialPortEvent.BI:/*Break interrupt,ͨѶ�ж�*/ 
        case SerialPortEvent.OE:/*Overrun error����λ����*/ 
        case SerialPortEvent.FE:/*Framing error����֡����*/
        case SerialPortEvent.PE:/*Parity error��У�����*/
        case SerialPortEvent.CD:/*Carrier detect���ز����*/
        case SerialPortEvent.CTS:/*Clear to send���������*/ 
        case SerialPortEvent.DSR:/*Data set ready�������豸����*/ 
        case SerialPortEvent.RI:/*Ring indicator������ָʾ*/
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:/*Output buffer is empty��������������*/ 
            break;
        case SerialPortEvent.DATA_AVAILABLE:/*Data available at the serial port���˿��п������ݡ������������飬������ն�*/
        	try {
				System.out.println("--------------�������ݳ���-------------  "+this.IS.available());
		        this.returnBuffer= new byte[10];

				while(this.IS.available()>0){			   
					this.IS.read(this.returnBuffer);
				}
				 System.out.println("-----------��������------------   "+Arrays.toString(this.returnBuffer));
				
			}catch (IOException e) {
				e.printStackTrace();
			}      
        }
        }
    /**
     * @�������� :close
     * @�������� :�ر� SerialPort
     * @����ֵ���� :void
     */
    public void close(){
       this.sPort.close();
        this.sPort = null;
        this.portId = null;
    }
    /**
     * ���ض˿��Ƿ���Ҫ�رյ�״̬
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
