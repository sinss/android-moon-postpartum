package li.com.bobsonclinic.data.server;

import li.com.bobsonclinic.data.BOBIntro;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBIntroResponse extends BOBResponse {
	private BOBIntro intro;

	public BOBIntro getIntro() {
		return intro;
	}

	public void setIntro(BOBIntro intro) {
		this.intro = intro;
	}
}
