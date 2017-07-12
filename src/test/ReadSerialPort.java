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
    private String appName = "dodo传感器测试";
    private int timeout = 2000;//open 端口时的等待时间
    private int threadTime = 0;
    private String sport;
    private CommPortIdentifier commPort;
    private SerialPort serialPort;
    private InputStream inputStream;
    private OutputStream outputStream;
    byte[] stop={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x10,(byte) 0xff,0x65};   //静止指令
	byte[] forward={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x11,(byte) 0xff,0x65};   //前进指令
	byte[] backward={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x01,(byte) 0xff,0x65};   //后退指令
	byte[] positionCheck={0x01,0x03,0x00,0x00,0x00,0x02,(byte) 0xff,0x65};        //电机复位自检指令
 
	public ReadSerialPort() {
		
	}
    /**
     * @方法名称 :listPort
     * @功能描述 :列出所有可用的串口
     * @返回值类型 :void
     */
    @SuppressWarnings("rawtypes")
    public void listPort(){
        CommPortIdentifier cpid;
        Enumeration en = CommPortIdentifier.getPortIdentifiers();
        
        System.out.println("now to list all Port of this PC：" +en);
        
        while(en.hasMoreElements()){
            cpid = (CommPortIdentifier)en.nextElement();
            if(cpid.getPortType() == CommPortIdentifier.PORT_SERIAL){
                System.out.println(cpid.getName() + ", " + cpid.getCurrentOwner());
            }
        }
    }   
    /**
     * @方法名称 :selectPort
     * @功能描述 :选择一个端口，比如：COM1
     * @返回值类型 :void
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
     * @方法名称 :openPort
     * @功能描述 :打开SerialPort
     * @返回值类型 :void
     */
    private void openPort(){
        if(commPort == null)
            System.out.println(String.format("无法找到名字为'%1$s'的串口！", commPort.getName()));
        else{
        	System.out.println("端口选择成功，当前端口："+commPort.getName()+",现在实例化 SerialPort:");
            
            try{
                serialPort = (SerialPort)commPort.open(appName, timeout);
                System.out.println("实例 SerialPort 成功！");
            }catch(PortInUseException e){
                throw new RuntimeException(String.format("端口'%1$s'正在使用中！", 
                        commPort.getName()));
            }
        }
    }
    
    /**
     * @方法名称 :checkPort
     * @功能描述 :检查端口是否正确连接
     * @返回值类型 :void
     */
    private void checkPort(){
        if(commPort == null)
            throw new RuntimeException("没有选择端口，请使用 " +
                    "selectPort(String portName) 方法选择端口");
        
        if(serialPort == null){
            throw new RuntimeException("SerialPort 对象无效！");
        }
    }
    
    /**
     * @方法名称 :write
     * @功能描述 :向端口发送数据，请在调用此方法前 先选择端口，并确定SerialPort正常打开！
     * @返回值类型 :void
     *    @param message
     */
    public void write(String message) {
        checkPort();
        
        try{
            outputStream = new BufferedOutputStream(serialPort.getOutputStream());
        }catch(IOException e){
            throw new RuntimeException("获取端口的OutputStream出错："+e.getMessage());
        }
        
        try{
            outputStream.write(message.getBytes());
            System.out.println("信息发送成功！");
        }catch(IOException e){
            throw new RuntimeException("向端口发送信息时出错："+e.getMessage());
        }finally{
            try{
                outputStream.close();
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
    public void startRead(int time){
        checkPort();
        
        try{
            inputStream = new BufferedInputStream(serialPort.getInputStream());
        }catch(IOException e){
            throw new RuntimeException("获取端口的InputStream出错："+e.getMessage());
        }
        
        try{
            serialPort.addEventListener(this);
        }catch(TooManyListenersException e){
            throw new RuntimeException(e.getMessage());
        }
        
        serialPort.notifyOnDataAvailable(true);
        
        System.out.println(String.format("开始监听来自'%1$s'的数据--------------", commPort.getName()));
        if(time > 0){
            this.threadTime = time*1000;
            Thread t = new Thread(this);
            t.start();
            System.out.println(String.format("监听程序将在%1$d秒后关闭。。。。", threadTime));
        }
    }
    public void startRead(){
        checkPort();
        
        try{
            inputStream = new BufferedInputStream(serialPort.getInputStream());
        }catch(IOException e){
            throw new RuntimeException("获取端口的InputStream出错："+e.getMessage());
        }
        
        try{
            serialPort.addEventListener(this);
        }catch(TooManyListenersException e){
            throw new RuntimeException(e.getMessage());
        }
        
        serialPort.notifyOnDataAvailable(true);
        
        System.out.println(String.format("开始监听来自'%1$s'的数据--------------", commPort.getName()));
        
    }
    
    /**
     * @方法名称 :close
     * @功能描述 :关闭 SerialPort
     * @返回值类型 :void
     */
    public void close(){
        serialPort.close();
        serialPort = null;
        commPort = null;
    }

    /**
     * 数据接收的监听处理函数
     */
    @Override
    public void serialEvent(SerialPortEvent arg0) {
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
            byte[] readBuffer = new byte[1024];
            String readStr="";
            String s2 = "";
            try {
                
                while (inputStream.available() > 0) {
                    inputStream.read(readBuffer);
                    readStr += new String(readBuffer).trim();
                }
                
                s2 = new String(readBuffer).trim();
                
                System.out.println("接收到端口返回数据(长度为"+readStr.length()+")："+readStr);
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
            System.out.println(String.format("端口''监听关闭了！", commPort.getName()));
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