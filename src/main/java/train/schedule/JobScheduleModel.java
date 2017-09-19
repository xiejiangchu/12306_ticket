package train.schedule;

import org.quartz.JobDetail;
import org.quartz.Trigger;

/**
 *  Model containing the JobDetails and Trigger of a Job.
 */
public class JobScheduleModel {

    private JobDetail jobDetail;
    private Trigger trigger;

    public JobScheduleModel(JobDetail jobDetail, Trigger trigger) {
        this.jobDetail = jobDetail;
        this.trigger = trigger;
    }

    public JobDetail getJobDetail() {
        return jobDetail;
    }

    public Trigger getTrigger() {
        return trigger;
    }
}