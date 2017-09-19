package train.schedule;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.quartz.CronScheduleBuilder.cronSchedule;

/**
 * Generates a list of JobScheduleModel from the JobScheduleProperties
 */
@Component
public class JobSchedulerModelGenerator {

    public static final String JOB_NAME = "JobName";
    public static final String GROUP_NAME = "Group";
    public static final String DATA_TO_WRITE = "dataToWrite";

    @Autowired
    private JobScheduleProperties jobScheduleProperties;

    public List<JobScheduleModel> generateModels() {
        List<JobProperties> jobs = jobScheduleProperties.getJobs();
        List<JobScheduleModel> generatedModels = new ArrayList<>();
        for (int i = 0; i < jobs.size(); i++) {
            JobScheduleModel model = generateModelFrom(jobs.get(i), i);
            generatedModels.add(model);
        }
        return generatedModels;
    }

    private JobScheduleModel generateModelFrom(JobProperties job, int jobIndex) {
        JobDetail jobDetail = getJobDetailFor(JOB_NAME + jobIndex, GROUP_NAME, job);

        Trigger trigger = getTriggerFor(job.getCronExpression(), jobDetail);
        JobScheduleModel jobScheduleModel = new JobScheduleModel(jobDetail, trigger);
        return jobScheduleModel;
    }

    private JobDetail getJobDetailFor(String jobName, String groupName, JobProperties job) {
        JobDetail jobDetail = JobBuilder.newJob(JobRunner.class)
                .setJobData(getJobDataMapFrom(job.getDataToWrite()))
                .withDescription("Job with data to write : " + job.getDataToWrite() +
                        " and CRON expression : " + job.getCronExpression())
                .withIdentity(jobName, groupName)
                .build();
        return jobDetail;
    }

    private JobDataMap getJobDataMapFrom(String dataToWrite) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(DATA_TO_WRITE, dataToWrite);
        return jobDataMap;
    }

    private Trigger getTriggerFor(String cronExpression, JobDetail jobDetail) {
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withSchedule(cronSchedule(cronExpression))
                .build();
        return trigger;
    }
}

