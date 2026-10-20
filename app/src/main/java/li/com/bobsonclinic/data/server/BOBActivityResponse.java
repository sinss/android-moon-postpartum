package li.com.bobsonclinic.data.server;

import li.com.bobsonclinic.data.BOBActivity;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBActivityResponse extends BOBResponse {
	private int member_count;
	private String join_status;
	private BOBActivity activity;


	public int getMember_count() {
		return member_count;
	}

	public void setMember_count(int member_count) {
		this.member_count = member_count;
	}

	public String getJoin_status() {
		return join_status;
	}

	public void setJoin_status(String join_status) {
		this.join_status = join_status;
	}

	public BOBActivity getActivity() {
		return activity;
	}

	public void setActivity(BOBActivity activity) {
		this.activity = activity;
	}
}

