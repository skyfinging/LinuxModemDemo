package demo.rxtx.reader;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 接收消息方式：
 * modem自动触发事件的监听
 */
public interface ISerialReader {

    void readerAndWriter(InputStream inputStream, OutputStream outputStream);
}
