package li.com.bobsonclinic.data.server;

import java.util.List;

import li.com.bobsonclinic.data.BOBMonthActivity;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBMonthActivityResponse extends BOBResponse {

	private List<BOBMonthActivity> activities;

	public List<BOBMonthActivity> getActivities() {
		return activities;
	}

	public void setActivities(List<BOBMonthActivity> activities) {
		this.activities = activities;
	}
}

