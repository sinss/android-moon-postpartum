package li.com.bobsonclinic.data.server;

import java.util.List;

import li.com.bobsonclinic.data.BOBIntroList;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBIntroListResponse extends BOBResponse {
	private List<BOBIntroList> intro_list;

	public List<BOBIntroList> getIntro_list() {
		return intro_list;
	}

	public void setIntro_list(List<BOBIntroList> intro_list) {
		this.intro_list = intro_list;
	}
}
