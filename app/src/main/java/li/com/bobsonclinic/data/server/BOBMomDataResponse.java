package li.com.bobsonclinic.data.server;

import li.com.bobsonclinic.data.BOBMomData;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBMomDataResponse extends BOBResponse {

	private BOBMomData mom;

	public BOBMomData getMom() {
		return mom;
	}

	public void setMom(BOBMomData mom) {
		this.mom = mom;
	}
}
