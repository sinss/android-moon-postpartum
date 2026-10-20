package li.com.bobsonclinic.data.server;

import java.util.List;

import li.com.bobsonclinic.data.BOBMomWeight;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBMomWeightResponse extends BOBResponse {

	private List<BOBMomWeight> exams;

	public List<BOBMomWeight> getExams() {
		return exams;
	}

	public void setExams(List<BOBMomWeight> exams) {
		this.exams = exams;
	}
}
