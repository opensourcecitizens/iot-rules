package drools.runner;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;

import io.rules.db.RuleDataBase;

public class TestDrools {
	
	/*
	@Test public void runTest(){
		RuleRunner runner = new RuleRunner();
		String [] rules =  {"drools/test/Person.drl"};
	
		Object [] facts = {new Person("John Doe",20)};
		runner.runRules(rules, facts);
	}
	*/
	
	@Test public void runStatelessTest_Allowed(){
		
		StatelessRuleRunner runner = new StatelessRuleRunner();
		String [] rules =  {"drools/test/Person.drl"};
	
		Object [] facts = {new Person("John Doe",21)};
		Person p = (Person) runner.runRules(rules, facts);
		//System.out.println("Allowed? "+p.isAllowed());
		
		assertEquals( p.isAllowed(),true);

	}
	
	@Test public void runStatelessTest_NotAllowed(){
		
		StatelessRuleRunner runner = new StatelessRuleRunner();
		String [] rules =  {"drools/test/Person.drl"};
	
		Object [] facts = {new Person("John Doe",19)};
		Person p = (Person) runner.runRules(rules, facts);
		//System.out.println("Allowed? "+p.isAllowed());
		
		assertEquals( p.isAllowed(),false);
	}
	
	

	
	@Test public void runStatelessResourceTest_Allowed() throws SQLException, IOException{
		
		io.rules.drools.StatelessRuleRunner runner = new io.rules.drools.StatelessRuleRunner ();
		RuleDataBase db = RuleDataBase.singleton();
		db.init();
		
		Resource resource = KieServices.Factory.get().getResources().newByteArrayResource(db.retrieveRule("person_drl").getBytes(),"UTF-8");
		//Resource resource = KieServices.Factory.get().getResources().newClassPathResource("drools/test/Person.drl");
		//Resource resource = KieServices.Factory.get().getResources().newFileSystemResource("/Users/kndungu/git/ds-spark-kafka-consumer/spark_kafka_consumer/test/drools/test/Person.drl");
		//Resource resource = KieServices.Factory.get().getResources().newReaderResource(db.retrieveRuleAsReader("person_drl"));//throws an exception
		//Resource resource = KieServices.Factory.get().getResources().newInputStreamResource(db.retrieveRuleAsStream("person_drl"));//throws an exception
		
		//These two are critical in telling the engine about where to temporarily write the class and the string's language 
		resource.setTargetPath("src/main/resources/"+"person_drl");
		resource.setResourceType(ResourceType.DRL );
		
		Resource [] resources =  { resource };
		
		Object [] facts = {new Person("John Doe",22)};
		
		runner.runRules(resources, facts);
		
		Person p = (Person) facts[0];
		//Person p = (Person) runner.runRules(resources, facts)[0];
		
		System.out.println("Allowed? "+p.isAllowed());
		
		db.cleanup();
		
		assertEquals( p.isAllowed(),true);

	}

	
	@Test public void runStatelessDBTest_Allowed(){
		
		StatelessRuleRunner runner = new StatelessRuleRunner();
		String [] rules =  {"drools/test/Person.drl"};
	
		Object [] facts = {new Person("John Doe",21)};
		Person p = (Person) runner.runRules(rules, facts);
		//System.out.println("Allowed? "+p.isAllowed());
		
		assertEquals( p.isAllowed(),true);

	}
	
	

	
}
