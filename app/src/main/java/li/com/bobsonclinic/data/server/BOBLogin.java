package li.com.bobsonclinic.data.server;

;import li.com.bobsonclinic.data.server.base.BOBResponse;

public class BOBLogin extends BOBResponse {

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
