package li.com.bobsonclinic.data.server;

import java.util.List;

import li.com.bobsonclinic.data.BOBVasList;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBVasListResponse extends BOBResponse {

	private List<BOBVasList> vas_list;

	public List<BOBVasList> getVas_list() {
		return vas_list;
	}

	public void setVas_list(List<BOBVasList> vas_list) {
		this.vas_list = vas_list;
	}
}
