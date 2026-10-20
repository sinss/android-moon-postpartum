package li.com.bobsonclinic.data.server;

import java.util.List;

import li.com.bobsonclinic.data.BOBMomPressure;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBMomPressureResponse extends BOBResponse {

	private List<BOBMomPressure> exams;

	public List<BOBMomPressure> getExams() {
		return exams;
	}

	public void setExams(List<BOBMomPressure> exams) {
		this.exams = exams;
	}
}
