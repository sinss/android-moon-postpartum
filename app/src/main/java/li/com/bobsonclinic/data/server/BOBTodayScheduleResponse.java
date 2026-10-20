package li.com.bobsonclinic.data.server;

import java.util.List;

import li.com.bobsonclinic.data.BOBTodaySchedule;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBTodayScheduleResponse extends BOBResponse {

	private List<BOBTodaySchedule> today_schedule_list;

	public List<BOBTodaySchedule> getToday_schedule_list() {
		return today_schedule_list;
	}

	public void setToday_schedule_list(List<BOBTodaySchedule> today_schedule_list) {
		this.today_schedule_list = today_schedule_list;
	}
}
