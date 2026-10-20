package li.com.bobsonclinic.data.server;

import java.util.List;

import li.com.bobsonclinic.data.BOBNotifaction;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBNotificationResponse extends BOBResponse {

	private List<BOBNotifaction> notis;

	public List<BOBNotifaction> getNotis() {
		return notis;
	}

	public void setNotis(List<BOBNotifaction> notis) {
		this.notis = notis;
	}
}
