package train.schedule;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Maps to the root of the configuration and has a property of a List of
 * JobProperties objects.
 */
@Configuration
@ConfigurationProperties(prefix = "schedule")
public class JobScheduleProperties {

    private List<JobProperties> jobs;

    public List<JobProperties> getJobs() {
        return jobs;
    }

    public void setJobs(List<JobProperties> jobs) {
        this.jobs = jobs;
    }
}