package li.com.bobsonclinic.data.server;

import java.util.List;

import li.com.bobsonclinic.data.BOBMomTemperature;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBMomTemperatureResponse extends BOBResponse {

	private List<BOBMomTemperature> exams;

	public List<BOBMomTemperature> getExams() {
		return exams;
	}

	public void setExams(List<BOBMomTemperature> exams) {
		this.exams = exams;
	}
}
