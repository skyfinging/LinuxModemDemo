package demo.config;

import demo.rxtx.DefaultSerialEventListener;
import demo.rxtx.SerialPortRxtx;
import demo.rxtx.reader.SerialPrintReader;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.TooManyListenersException;

@Configuration
@Getter
@ToString
@Log4j2
public class RxtxConfig {
    @Value("${serial.rxtx.enable}")
    boolean enabled;
    @Value("${serial.rxtx.delayReadMS}")
    Integer delayReadMS;
    @Value("${serial.rxtx.timeoutMS}")
    Integer timeoutMS;
    @Value("${serial.rxtx.port}")
    String port;
    @Value("${serial.rxtx.dataBits}")
    Integer dataBits;
    @Value("${serial.rxtx.stopBits}")
    Integer stopBits;
    @Value("${serial.rxtx.parity}")
    Integer parity;
    @Value("${serial.rxtx.rate}")
    Integer rate;
    @Value("${serial.rxtx.flowControlMode}")
    Integer flowControlMode;
    @Value("${serial.rxtx.appName}")
    String appName;

    @Bean(destroyMethod = "close")
    public SerialPortRxtx getSerialPortRxtx(){
        SerialPortRxtx serialPortRxtx = new SerialPortRxtx(this);
        try {
            if(enabled) {
                serialPortRxtx.open();
                serialPortRxtx.addEventListener(new DefaultSerialEventListener(serialPortRxtx, new SerialPrintReader()));
                log.info("串口连接成功");
            }else{
                log.info("串口开关未打开");
            }
        } catch (NoSuchPortException | PortInUseException | IOException
                | TooManyListenersException | UnsupportedCommOperationException e) {
            log.error(e.getMessage(), e);
        }
        return serialPortRxtx;
    }
}
