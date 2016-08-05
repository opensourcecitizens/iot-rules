package drools.runner;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message.Level;
import org.kie.api.io.KieResources;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
 
public class StatelessRuleRunner 
{
    public StatelessRuleRunner()
    {
    }
 
    public Object runRules(String[] rules, Object[] facts)
    {
 
        KieServices kieServices = KieServices.Factory.get();
        KieResources kieResources = kieServices.getResources();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        KieRepository kieRepository = kieServices.getRepository();
  
        for(String ruleFile : rules)
        {
            Resource resource = kieResources.newClassPathResource(ruleFile);
  
            // path has to start with src/main/resources
            // append it with the package from the rule
            kieFileSystem.write("src/main/resources/"+ruleFile, resource);
        }
  
        KieBuilder kb = kieServices.newKieBuilder(kieFileSystem);
  
        kb.buildAll();
  
        if (kb.getResults().hasMessages(Level.ERROR))
        {
            throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
        }
        
        KieContainer kContainer = kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
 
        StatelessKieSession ksession = kContainer.newStatelessKieSession();//kContainer.newStatelessKieSession("defaultStatelessKieSession");
        
        Person person = (Person) facts[0];
        
        ksession.execute( person );
       
       
       System.out.println( "Name: " + person.getAge() );
       System.out.println( "Age: " + person.getName() );
       System.out.println( "Allowed: " + person.isAllowed() );
       
        return person;
    }
}