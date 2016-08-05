package io.rules.drools;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message.Level;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
 
public class StatelessRuleRunner 
{
    public StatelessRuleRunner()
    {
    }
 
    public <T> T[] runRules(Resource[] ruleResources, T[] facts)
    {
 
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        KieRepository kieRepository = kieServices.getRepository();
  
        for(Resource resource : ruleResources)
        {
            kieFileSystem.write( resource);
        }
  
        KieBuilder kb = kieServices.newKieBuilder(kieFileSystem);
  
        kb.buildAll();
  
        if (kb.getResults().hasMessages(Level.ERROR))
        {
            throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
        }
        
        KieContainer kContainer = kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
 
        StatelessKieSession ksession = kContainer.newStatelessKieSession();//kContainer.newStatelessKieSession("defaultStatelessKieSession");
        
        for(T fact : facts){
        	
        	ksession.execute( fact );

        }
       
        return facts;
    }
}