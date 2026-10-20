package li.com.bobsonclinic.data.server;

import java.util.List;

import li.com.bobsonclinic.data.BOBConsumerList;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBConsumerListResponse extends BOBResponse {

	private List<BOBConsumerList> consumes;

	public List<BOBConsumerList> getConsumes() {
		return consumes;
	}

	public void setConsumes(List<BOBConsumerList> consumes) {
		this.consumes = consumes;
	}
}
