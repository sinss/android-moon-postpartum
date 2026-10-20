package li.com.bobsonclinic.data.server;

;import li.com.bobsonclinic.data.server.base.BOBResponse;

public class BOBVasCheckResponse extends BOBResponse {

	private String schedule_id;

	public String getSchedule_id() {
		return schedule_id;
	}

	public void setSchedule_id(String schedule_id) {
		this.schedule_id = schedule_id;
	}
}
