package li.com.bobsonclinic.data.server;

import java.util.List;

import li.com.bobsonclinic.data.BOBBabyTemperature;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBBabyTemperatureResponse extends BOBResponse {

	private List<BOBBabyTemperature> exams;

	public List<BOBBabyTemperature> getExams() {
		return exams;
	}

	public void setExams(List<BOBBabyTemperature> exams) {
		this.exams = exams;
	}
}
