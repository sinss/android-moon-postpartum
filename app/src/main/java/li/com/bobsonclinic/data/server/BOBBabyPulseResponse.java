package li.com.bobsonclinic.data.server;

import java.util.List;

import li.com.bobsonclinic.data.BOBBabyPulse;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBBabyPulseResponse extends BOBResponse {

	private List<BOBBabyPulse> exams;

	public List<BOBBabyPulse> getExams() {
		return exams;
	}

	public void setExams(List<BOBBabyPulse> exams) {
		this.exams = exams;
	}
}
