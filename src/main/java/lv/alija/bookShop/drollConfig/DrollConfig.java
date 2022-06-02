package lv.alija.bookShop.drollConfig;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class DrollConfig {

    private final Logger LOG = LoggerFactory.getLogger(DrollConfig.class);
    private KieServices kieService = KieServices.Factory.get();

    private KieFileSystem getKieFileSystem() throws IOException {
        KieFileSystem kieFileSystem = kieService.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newClassPathResource("discount.drl"));
        return kieFileSystem;
    }
    @Bean
    public KieContainer getKieContainer() throws IOException{
        LOG.info("Container created");
        getKieRepository();
        KieBuilder kb = kieService.newKieBuilder(getKieFileSystem());
        kb.buildAll();
        KieModule kieModule = kb.getKieModule();
        KieContainer kieContainer = kieService.newKieContainer(kieModule.getReleaseId());
        return kieContainer;
    }

    private void getKieRepository(){
        final KieRepository kieRepository =kieService.getRepository();
        kieRepository.addKieModule(new KieModule() {
            @Override
            public ReleaseId getReleaseId() {
                return kieRepository.getDefaultReleaseId();
            }
        });
    }
    @Bean
    public KieSession getKieSession() throws IOException{
        LOG.info("Session created...");
        return getKieContainer().newKieSession();
    }
}

