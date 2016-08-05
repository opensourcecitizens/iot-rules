package drools.runner;

public class Person {
	public Person(String _name , int _age){
		name=_name;
		age=_age;
	}
	private String name;
	private int age;
	private boolean allowed;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public boolean isAllowed() {
		return allowed;
	}
	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}
	

}
