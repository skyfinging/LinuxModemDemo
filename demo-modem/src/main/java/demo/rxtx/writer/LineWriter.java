package demo.rxtx.writer;

import demo.rxtx.SerialPortRxtx;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class LineWriter implements ISerialWriter {

    @Autowired
    SerialPortRxtx serialPortRxtx;


    @Override
    public void write(String message) {
        if(!serialPortRxtx.isOpen()) {
            log.error("串口未打开，无法发送消息");
            return;
        }
        if(StringUtils.isEmpty(message.trim()))
            return;
        byte[] bytes = message.getBytes();
        try {
            serialPortRxtx.getOutputStream().write(bytes);
            log.debug("如果卡在flush，则说明FlowControlMode设置的不对");
            log.debug("如果程序在flush直接崩溃，则说明librxtxSerial.so版本不匹配");
            serialPortRxtx.getOutputStream().flush();
            log.info("发送消息成功："+message);
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            log.info("发送消息失败");
        } catch (InterruptedException ignore) {
        }

    }


}
