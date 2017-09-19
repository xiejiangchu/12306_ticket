package train.schedule;

import org.springframework.stereotype.Component;

/**
 * Properties for a single job.
 */
@Component
public class JobProperties {

    private String cronExpression;

    private String dataToWrite;

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getDataToWrite() {
        return dataToWrite;
    }

    public void setDataToWrite(String dataToWrite) {
        this.dataToWrite = dataToWrite;
    }
}
