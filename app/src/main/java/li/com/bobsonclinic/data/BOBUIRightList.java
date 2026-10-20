package li.com.bobsonclinic.data;

import java.util.HashMap;
import java.util.Map;

;

public class BOBUIRightList {

	private String id;
	private String title;
	private String contentUrl;
	private String css_path;
	private Map<String, BOBUIRightList> subData = new HashMap<String, BOBUIRightList>();


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	public String getCss_path() {
		return css_path;
	}

	public void setCss_path(String css_path) {
		this.css_path = css_path;
	}

	public Map<String, BOBUIRightList> getSubData() {
		return subData;
	}

	public void setSubData(Map<String, BOBUIRightList> subData) {
		this.subData = subData;
	}
}
