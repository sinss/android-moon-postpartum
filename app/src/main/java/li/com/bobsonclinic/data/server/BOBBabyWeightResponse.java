package li.com.bobsonclinic.data.server;

import java.util.List;

import li.com.bobsonclinic.data.BOBBabyWeight;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBBabyWeightResponse extends BOBResponse {

	private List<BOBBabyWeight> exams;

	public List<BOBBabyWeight> getExams() {
		return exams;
	}

	public void setExams(List<BOBBabyWeight> exams) {
		this.exams = exams;
	}
}
