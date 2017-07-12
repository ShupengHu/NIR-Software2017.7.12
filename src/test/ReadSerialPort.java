package test;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class ReadSerialPort implements Runnable, SerialPortEventListener {
    private String appName = "dodo����������";
    private int timeout = 2000;//open �˿�ʱ�ĵȴ�ʱ��
    private int threadTime = 0;
    private String sport;
    private CommPortIdentifier commPort;
    private SerialPort serialPort;
    private InputStream inputStream;
    private OutputStream outputStream;
    byte[] stop={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x10,(byte) 0xff,0x65};   //��ָֹ��
	byte[] forward={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x11,(byte) 0xff,0x65};   //ǰ��ָ��
	byte[] backward={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x01,(byte) 0xff,0x65};   //����ָ��
	byte[] positionCheck={0x01,0x03,0x00,0x00,0x00,0x02,(byte) 0xff,0x65};        //�����λ�Լ�ָ��
 
	public ReadSerialPort() {
		
	}
    /**
     * @�������� :listPort
     * @�������� :�г����п��õĴ���
     * @����ֵ���� :void
     */
    @SuppressWarnings("rawtypes")
    public void listPort(){
        CommPortIdentifier cpid;
        Enumeration en = CommPortIdentifier.getPortIdentifiers();
        
        System.out.println("now to list all Port of this PC��" +en);
        
        while(en.hasMoreElements()){
            cpid = (CommPortIdentifier)en.nextElement();
            if(cpid.getPortType() == CommPortIdentifier.PORT_SERIAL){
                System.out.println(cpid.getName() + ", " + cpid.getCurrentOwner());
            }
        }
    }   
    /**
     * @�������� :selectPort
     * @�������� :ѡ��һ���˿ڣ����磺COM1
     * @����ֵ���� :void
     *    @param portName
     */
    @SuppressWarnings("rawtypes")
    public void selectPort(String portName){
        
        this.commPort = null;
        CommPortIdentifier cpid;
        Enumeration en = CommPortIdentifier.getPortIdentifiers();
        
        while(en.hasMoreElements()){
            cpid = (CommPortIdentifier)en.nextElement();
            if(cpid.getPortType() == CommPortIdentifier.PORT_SERIAL
                    && cpid.getName().equals(portName)){
                this.commPort = cpid;
                break;
            }
        }
        
        openPort();
    }
    
    /**
     * @�������� :openPort
     * @�������� :��SerialPort
     * @����ֵ���� :void
     */
    private void openPort(){
        if(commPort == null)
            System.out.println(String.format("�޷��ҵ�����Ϊ'%1$s'�Ĵ��ڣ�", commPort.getName()));
        else{
        	System.out.println("�˿�ѡ��ɹ�����ǰ�˿ڣ�"+commPort.getName()+",����ʵ���� SerialPort:");
            
            try{
                serialPort = (SerialPort)commPort.open(appName, timeout);
                System.out.println("ʵ�� SerialPort �ɹ���");
            }catch(PortInUseException e){
                throw new RuntimeException(String.format("�˿�'%1$s'����ʹ���У�", 
                        commPort.getName()));
            }
        }
    }
    
    /**
     * @�������� :checkPort
     * @�������� :���˿��Ƿ���ȷ����
     * @����ֵ���� :void
     */
    private void checkPort(){
        if(commPort == null)
            throw new RuntimeException("û��ѡ��˿ڣ���ʹ�� " +
                    "selectPort(String portName) ����ѡ��˿�");
        
        if(serialPort == null){
            throw new RuntimeException("SerialPort ������Ч��");
        }
    }
    
    /**
     * @�������� :write
     * @�������� :��˿ڷ������ݣ����ڵ��ô˷���ǰ ��ѡ��˿ڣ���ȷ��SerialPort�����򿪣�
     * @����ֵ���� :void
     *    @param message
     */
    public void write(String message) {
        checkPort();
        
        try{
            outputStream = new BufferedOutputStream(serialPort.getOutputStream());
        }catch(IOException e){
            throw new RuntimeException("��ȡ�˿ڵ�OutputStream����"+e.getMessage());
        }
        
        try{
            outputStream.write(message.getBytes());
            System.out.println("��Ϣ���ͳɹ���");
        }catch(IOException e){
            throw new RuntimeException("��˿ڷ�����Ϣʱ����"+e.getMessage());
        }finally{
            try{
                outputStream.close();
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
    public void startRead(int time){
        checkPort();
        
        try{
            inputStream = new BufferedInputStream(serialPort.getInputStream());
        }catch(IOException e){
            throw new RuntimeException("��ȡ�˿ڵ�InputStream����"+e.getMessage());
        }
        
        try{
            serialPort.addEventListener(this);
        }catch(TooManyListenersException e){
            throw new RuntimeException(e.getMessage());
        }
        
        serialPort.notifyOnDataAvailable(true);
        
        System.out.println(String.format("��ʼ��������'%1$s'������--------------", commPort.getName()));
        if(time > 0){
            this.threadTime = time*1000;
            Thread t = new Thread(this);
            t.start();
            System.out.println(String.format("����������%1$d���رա�������", threadTime));
        }
    }
    public void startRead(){
        checkPort();
        
        try{
            inputStream = new BufferedInputStream(serialPort.getInputStream());
        }catch(IOException e){
            throw new RuntimeException("��ȡ�˿ڵ�InputStream����"+e.getMessage());
        }
        
        try{
            serialPort.addEventListener(this);
        }catch(TooManyListenersException e){
            throw new RuntimeException(e.getMessage());
        }
        
        serialPort.notifyOnDataAvailable(true);
        
        System.out.println(String.format("��ʼ��������'%1$s'������--------------", commPort.getName()));
        
    }
    
    /**
     * @�������� :close
     * @�������� :�ر� SerialPort
     * @����ֵ���� :void
     */
    public void close(){
        serialPort.close();
        serialPort = null;
        commPort = null;
    }

    /**
     * ���ݽ��յļ���������
     */
    @Override
    public void serialEvent(SerialPortEvent arg0) {
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
            byte[] readBuffer = new byte[1024];
            String readStr="";
            String s2 = "";
            try {
                
                while (inputStream.available() > 0) {
                    inputStream.read(readBuffer);
                    readStr += new String(readBuffer).trim();
                }
                
                s2 = new String(readBuffer).trim();
                
                System.out.println("���յ��˿ڷ�������(����Ϊ"+readStr.length()+")��"+readStr);
                System.out.println(s2);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void run() {
        try{
            Thread.sleep(threadTime);
            serialPort.close();
            System.out.println(String.format("�˿�''�����ر��ˣ�", commPort.getName()));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }
}