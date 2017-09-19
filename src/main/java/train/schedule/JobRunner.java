package train.schedule;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import retrofit2.Retrofit;
import train.service.PingService;

/**
 * Created by Ice on 11/4/2016.
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class JobRunner implements Job {

    private String dataToWrite;

    @Autowired
    private Retrofit retrofit;

    @Autowired
    private PingService pingService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        pingService.printString(dataToWrite);
    }


    public String getDataToWrite() {
        return dataToWrite;
    }

    public void setDataToWrite(String dataToWrite) {
        this.dataToWrite = dataToWrite;
    }
}
