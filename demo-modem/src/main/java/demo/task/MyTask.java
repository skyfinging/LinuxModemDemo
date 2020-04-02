package demo.task;

import demo.rxtx.writer.LineWriter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class MyTask implements InitializingBean {
    private List<String> lines;
    @Autowired
    ResourceLoader resourceLoader;
    @Autowired
    LineWriter lineWriter;
    private final String end = new String(new byte[]{0x1A});

    @Scheduled(cron = "${task.cron.write}")
    public void write() {
        log.info("定时发送AT命令，如果modem正常，应该回应OK");
        lineWriter.write("AT\r\n");

        if(lines!=null && !lines.isEmpty()){
            log.info("发送sendLine.txt文件中的内容");
            lines.forEach(lineWriter::write);
        }
    }

    @Override
    public void afterPropertiesSet()  {
        try {
            String content = IOUtils.toString(
                    resourceLoader.getResource("classpath:sendLine.txt").getInputStream(), "UTF-8");
            lines = Arrays.asList(content.split("\r\n"));
            lines = lines.stream().filter(MyTask::filter).map(l->l.replaceAll("\\\\[Z]",end)+"\r\n")
                    .collect(Collectors.toList());
        }catch (Exception e){
            log.error("找不到sendLine.txt");
        }
    }

    private static boolean filter(String line){
        return !line.startsWith("#");
    }


}
