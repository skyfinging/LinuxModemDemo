package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Linux环境下的串口通信，用于发送接收短信，使用gradle的bootjar打包成jar即可运行
 * 部分代码参考自https://github.com/demoModel/rxtx
 */
@SpringBootApplication
@EnableScheduling
public class Main {
    /**
     * 启动参数需要加上Djava.library.path，用来指定so、dll文件的位置
     * -Djava.library.path=out\production\resources\library
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
