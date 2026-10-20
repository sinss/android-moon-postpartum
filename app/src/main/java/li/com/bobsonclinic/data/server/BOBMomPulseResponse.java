package li.com.bobsonclinic.data.server;

import java.util.List;

import li.com.bobsonclinic.data.BOBMomPulse;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBMomPulseResponse extends BOBResponse {

	private List<BOBMomPulse> exams;

	public List<BOBMomPulse> getExams() {
		return exams;
	}

	public void setExams(List<BOBMomPulse> exams) {
		this.exams = exams;
	}
}
