package demo.rxtx;

import demo.config.RxtxConfig;
import gnu.io.*;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

/**
 * <hr>
 * <h2>简介</h2> 根据串口上下文设置串口配置，并提供启动和关闭的方法
 * <hr>
 * <table border="1" cellspacing="0" cellpadding="2">
 * <caption><b>文件修改记录</b></caption>
 * <tr>
 * <th>修改日期</th>
 * <th>修改人</th>
 * <th>修改内容</th>
 * </tr>
 * <tbody>
 * <tr>
 * <td>2017年1月4日</td>
 * <td>linchunsen</td>
 * <td>新建文件，并实现基本功能</td>
 * </tr>
 * </tbody>
 * </table>
 */
@Log4j2
public class SerialPortRxtx {
	@Getter
	private RxtxConfig serialContext;
	/**
	 * 标识端口是否已被打开
	 */
	@Getter
	private boolean isOpen;
	/**
	 * 属性描述
	 */
	private CommPortIdentifier portId;
	/**
	 * 属性描述
	 */
	private SerialPort serialPort;
	/**
	 * 输入输出流
	 */
	@Getter
	private InputStream inputStream;
	@Getter
	private OutputStream outputStream;

	public SerialPortRxtx(RxtxConfig serialContext) {
		this.serialContext = serialContext;
	}

	public void open() throws NoSuchPortException, PortInUseException, IOException, UnsupportedCommOperationException {
		log.info("Java的library引用路径："+System.getProperty("java.library.path"));
		log.info("串口信息:"+serialContext);
		portId = CommPortIdentifier.getPortIdentifier(serialContext.getPort());
		serialPort = (SerialPort) portId.open(serialContext.getAppName(), serialContext.getTimeoutMS());
		serialPort.setFlowControlMode(serialContext.getFlowControlMode());
		inputStream = serialPort.getInputStream();
		outputStream = serialPort.getOutputStream();
		isOpen = true;
	}

	public void close() {
		if (isOpen) {
			log.info("关闭串口:"+serialContext);
			try {
				serialPort.notifyOnDataAvailable(false);
				serialPort.removeEventListener();
				inputStream.close();
				outputStream.close();
				serialPort.close();
				isOpen = false;
			} catch (IOException ex) {
				log.error(ex.getMessage(), ex);
			}
		}
	}

	/**
	 * 为串口设置事件监听器
	 * 无返回
	 * @param listener 事件监听器
	 * @author linchunsen
	 * @throws TooManyListenersException 
	 * @throws UnsupportedCommOperationException 
	 */
	public void addEventListener(SerialPortEventListener listener) throws TooManyListenersException, UnsupportedCommOperationException, IOException {
		if (isOpen) {
			this.serialPort.removeEventListener();
			this.serialPort.addEventListener(listener);
			serialPort.notifyOnDataAvailable(true);
			serialPort.setSerialPortParams(
					serialContext.getRate(),
					serialContext.getDataBits(),
					serialContext.getStopBits(),
					serialContext.getParity());
			inputStream = serialPort.getInputStream();
			outputStream = serialPort.getOutputStream();
		}else {
			throw new RuntimeException("串口未打开，请检查是否执行了open()方法");
		}
	}
}
