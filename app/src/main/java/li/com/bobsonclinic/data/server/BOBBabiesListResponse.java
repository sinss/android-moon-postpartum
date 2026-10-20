package li.com.bobsonclinic.data.server;

import java.util.List;

import li.com.bobsonclinic.data.BOBBabies;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBBabiesListResponse extends BOBResponse {

	private List<BOBBabies> babies;

	public List<BOBBabies> getBabies() {
		return babies;
	}

	public void setBabies(List<BOBBabies> babies) {
		this.babies = babies;
	}
}
