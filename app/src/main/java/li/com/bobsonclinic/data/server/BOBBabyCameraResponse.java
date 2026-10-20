package li.com.bobsonclinic.data.server;

import li.com.bobsonclinic.data.BOBBabyCamera;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBBabyCameraResponse extends BOBResponse {
	private BOBBabyCamera camera;

	public BOBBabyCamera getCamera() {
		return camera;
	}

	public void setCamera(BOBBabyCamera camera) {
		this.camera = camera;
	}
}
