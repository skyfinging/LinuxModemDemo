package demo.rxtx.reader;

import lombok.extern.log4j.Log4j2;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

@Log4j2
public class SerialPrintReader implements ISerialReader {

    private byte[] datas = new byte[1024];

    @Override
    public void readerAndWriter(InputStream inputStream, OutputStream outputStream) {
        try {
            if (inputStream.available() > 0) {
                int size = inputStream.read(datas);
                StringBuilder dataBuilder = new StringBuilder();
                String iString;
                for (int i = 0; i < size; i++) {
                    iString = Integer.toHexString(datas[i] & 0xFF);
                    if (iString.length() == 1) {
                        iString = "0" + iString;
                    }
                    dataBuilder.append(iString);
                }
                String message = dataBuilder.toString().toUpperCase();
                String[] strings = byteToString(message).split("\r\n");
                Arrays.stream(strings).filter(l-> !StringUtils.isEmpty(l)).forEach(l->log.info("收到消息："+l));
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private static  String byteToString(String str){
        if((str.length()%2)!=0) {
            return str;
        }
        byte[] bytes = new byte[str.length()/2];
        for(int i=0;i<bytes.length;i++){
            bytes[i] = (byte) Integer.parseInt(str.substring(i*2,i*2+2),16);
        }
        return new String(bytes);
    }
}
