package train.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xie on 17/9/19.
 */
@Component
public class TaskTest {

    private Logger logger = LoggerFactory.getLogger(TaskTest.class);


    @Scheduled(cron = "0/3 * * * * ? ")
    public void taskTest() {
        System.out.println("======================  TaskTest  ");
    }

    @Scheduled(cron = "0/5 * * * * ? ")   //每5秒执行一次
    public void testCron() {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info(sdf.format(new Date()) + "*********每5秒执行一次");
    }
}
