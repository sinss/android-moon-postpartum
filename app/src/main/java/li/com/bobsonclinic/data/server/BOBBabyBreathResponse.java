package li.com.bobsonclinic.data.server;

import java.util.List;

import li.com.bobsonclinic.data.BOBBabyBreath;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBBabyBreathResponse extends BOBResponse {

	private List<BOBBabyBreath> exams;

	public List<BOBBabyBreath> getExams() {
		return exams;
	}

	public void setExams(List<BOBBabyBreath> exams) {
		this.exams = exams;
	}
}
