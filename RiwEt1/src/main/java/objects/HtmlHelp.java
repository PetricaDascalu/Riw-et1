package objects;

public class HtmlHelp {
	private String name;
	private String content;
	
	public HtmlHelp(String nume, String continut){
		name = nume;
		content = continut;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public String toString(){
		return "Name: " + name + "\tContent: " + content +"\n";
	}
}
