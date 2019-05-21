package train.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import train.service.PingService;
import train.utils.FileUtil;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
public class CdnConfig {
    private static List<String> hosts;

    @Autowired
    private PingService pingService;

    @PostConstruct
    public void init() {
        ClassPathResource classPathResource = new ClassPathResource("cdn.txt");
        try {
            hosts = Arrays.asList(FileUtil.readFile(classPathResource.getFile(), "UTF-8").split(" "));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pingService.initTreeMap();
    }

    public static List<String> getHosts() {
        return hosts;
    }

    public static void setHosts(List<String> hosts) {
        CdnConfig.hosts = hosts;
    }
}
