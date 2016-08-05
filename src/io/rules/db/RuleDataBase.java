package io.rules.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.io.IOUtils;

public class RuleDataBase {
	
	private static RuleDataBase db = null;
	private Connection conn = null;
	private ResultSet rs = null;
	private Statement smt = null;

	private RuleDataBase(){}
	
	public static RuleDataBase singleton(){
		if(db==null){
			db = new RuleDataBase();
		}
		return db;
	}

	@PostConstruct
	public void init(){
		String dbURL = "org.apache.derby.jdbc.EmbeddedDriver";

		try {
			Class.forName(dbURL).newInstance();
			conn = DriverManager.getConnection("jdbc:derby:" + "droolsDB;create=true");
		} catch (Exception except) {
			except.printStackTrace();
		}

	}
	
	
	public InputStream retrieveRuleAsStream(String ruleName) throws SQLException, IOException{
		//String sb = null;
		InputStream stream = null;
		smt = conn.createStatement();
		rs = smt.executeQuery("select * from droolstbl where name like '"+ruleName+"'");
		
		while (rs.next()) {

			stream = rs.getClob(2).getAsciiStream();
			
		}

		return stream;
	}
	
	public Reader retrieveRuleAsReader(String ruleName) throws SQLException, IOException{
		//String sb = null;
		BufferedReader reader = null;
		smt = conn.createStatement();
		rs = smt.executeQuery("select * from droolstbl where name like '"+ruleName+"'");
		
		while (rs.next()) {

			 reader = new BufferedReader(rs.getClob(2).getCharacterStream());
		}
		
		
		return reader;
	}
	
	public String retrieveRule(String ruleName) throws SQLException, IOException{
		
		String sb = null;
		
		smt = conn.createStatement();
		rs = smt.executeQuery("select * from droolstbl where name like '"+ruleName+"'");
		
		while (rs.next()) {

			System.out.println("Values in the table are: " + rs.getString(1) + ",");

			StringWriter writer = new StringWriter();

			BufferedReader read = new BufferedReader(rs.getClob(2).getCharacterStream());
			 IOUtils.copy(read, writer);
			 					 
			sb= writer.toString();

			read.close();
		}
		
		rs.close();
		smt.close();
		
		return sb;
	}

	
	
	
	
	@PreDestroy
	public void cleanup(){
		try {
			
			if (rs != null) {
				rs.close();
			}
			if (smt != null) {
				smt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {

		}
	}
}
