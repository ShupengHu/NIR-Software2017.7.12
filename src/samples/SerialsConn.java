package samples;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialsConn {

	//private static final Logger logger = Logger.getLogger(SerialsConn.class);
	
	private SerialPort commPort = null;
	private InputStream in = null;
	private OutputStream out = null;

	private int readTimeout = 30000;
	    /**
		 * 连接串口
		 * 
		 * @param portName
		 *            串口名称如：COM1
		 * @param bps
		 *            码率 如：115200
		 */
	    public void connect ( String portName, int bps){
	        CommPortIdentifier portIdentifier;
		try {
		    portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if ( portIdentifier.isCurrentlyOwned() ) {
		    String errorMsg = "系统异常|SYSRROR: 串口|SERIAL["+portName+"]被占用|OCCUPIED";
		   // logger.error(errorMsg);
		} else {
		    commPort = (SerialPort) portIdentifier.open(this.getClass().getName(),2000);
		    commPort.enableReceiveTimeout(readTimeout);
		    if ( commPort instanceof SerialPort ) {
		        SerialPort serialPort = (SerialPort) commPort;
		                serialPort.setSerialPortParams(bps,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
		                in = serialPort.getInputStream();
		                out = serialPort.getOutputStream();
		            } else {
		               // logger.debug("Error: Only serial ports are handled.");
		            }
		    }
	        } catch (NoSuchPortException e) {
		    String errorMsg = "系统异常|SYSRROR: 没有检测到串口|NOT FOUND SERIAL["+portName+"]";
		    //logger.error(errorMsg,e);
	        } catch (PortInUseException e) {
		    String errorMsg = "系统异常: 串口["+portName+"]被占用";
		   // logger.error(errorMsg, e);
		} catch (UnsupportedCommOperationException e) {
		    String errorMsg = "系统异常: 串口操作不支持";
		    //logger.error(errorMsg, e);
		} catch (IOException e) {
		    String errorMsg = "系统异常: 串口打开异常";
		   // logger.error(errorMsg, e);
	        }
	    }
	    
	/** 关闭串口 */
	public void close() {
	    commPort.close();
	}

	public InputStream getIn() {
		return in;
	}

	public OutputStream getOut() {
		return out;
	}
}
