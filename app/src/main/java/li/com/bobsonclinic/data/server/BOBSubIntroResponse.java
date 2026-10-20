package li.com.bobsonclinic.data.server;

import li.com.bobsonclinic.data.BOBSubIntro;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBSubIntroResponse extends BOBResponse {

	private BOBSubIntro sub_intro;

	public BOBSubIntro getSub_intro() {
		return sub_intro;
	}

	public void setSub_intro(BOBSubIntro sub_intro) {
		this.sub_intro = sub_intro;
	}
}
