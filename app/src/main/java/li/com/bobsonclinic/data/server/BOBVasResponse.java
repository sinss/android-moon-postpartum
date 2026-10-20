package li.com.bobsonclinic.data.server;

import li.com.bobsonclinic.data.BOBVas;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBVasResponse extends BOBResponse {

	private BOBVas vas_obj;
	private String join_status;

	public BOBVas getVas_obj() {
		return vas_obj;
	}

	public void setVas_obj(BOBVas vas_obj) {
		this.vas_obj = vas_obj;
	}

	public String getJoin_status() {
		return join_status;
	}

	public void setJoin_status(String join_status) {
		this.join_status = join_status;
	}
}
