package li.com.bobsonclinic.data.server;

import java.util.List;

import li.com.bobsonclinic.data.BOBSubIntroList;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBSubIntroListResponse extends BOBResponse {

	private List<BOBSubIntroList> sub_intro_list;

	public List<BOBSubIntroList> getSub_intro_list() {
		return sub_intro_list;
	}

	public void setSub_intro_list(List<BOBSubIntroList> sub_intro_list) {
		this.sub_intro_list = sub_intro_list;
	}
}
