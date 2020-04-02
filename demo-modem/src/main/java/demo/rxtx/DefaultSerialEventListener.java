package demo.rxtx;

import demo.rxtx.SerialPortRxtx;
import demo.rxtx.reader.ISerialReader;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import lombok.extern.log4j.Log4j2;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Observable;

/**
 * <hr>
 * <h2>简介</h2> 串口事件监听的抽象实现，对串口监听监听器进行了进一步的封装
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
public class DefaultSerialEventListener extends Observable implements SerialPortEventListener {
	/**
	 * 串口实例
	 */
	private SerialPortRxtx serialPortRxtx;
	/**
	 * 读写延迟
	 */
	private long delayReadTime;
	private ISerialReader serialListener;

	public DefaultSerialEventListener(SerialPortRxtx serialPortRxtx, ISerialReader serialListener) {
		super();
		this.serialPortRxtx = serialPortRxtx;
		this.delayReadTime=serialPortRxtx.getSerialContext().getDelayReadMS();
		this.serialListener = serialListener;
	}

	/**
	 * {@link SerialPortEventListener}接口的实现类
	 */
	@Override
	public void serialEvent(SerialPortEvent event) {
		if (serialPortRxtx.isOpen()) {
			try {
				Thread.sleep(delayReadTime);
			} catch (InterruptedException ignored) {
			}
			switch (event.getEventType()) {
			case SerialPortEvent.BI: // 10
			case SerialPortEvent.OE: // 7
			case SerialPortEvent.FE: // 9
			case SerialPortEvent.PE: // 8
			case SerialPortEvent.CD: // 6
			case SerialPortEvent.CTS: // 3
			case SerialPortEvent.DSR: // 4
			case SerialPortEvent.RI: // 5
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2
				log.debug("modem消息Event："+event.getEventType());
				break;
			case SerialPortEvent.DATA_AVAILABLE: // 1
				InputStream inputStream = serialPortRxtx.getInputStream();
				OutputStream outputStream = serialPortRxtx.getOutputStream();
				if(serialListener!=null)
					serialListener.readerAndWriter(inputStream, outputStream);
				else
					log.warn("没有配置监听器");
				break;
			}
		} else {
			throw new RuntimeException("串口未开启，请查看是否执行了SerialPortRxtx类的open()方法");
		}
	}


}
