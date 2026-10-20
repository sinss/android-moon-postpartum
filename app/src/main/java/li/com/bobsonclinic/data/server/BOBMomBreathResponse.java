package li.com.bobsonclinic.data.server;

import java.util.List;

import li.com.bobsonclinic.data.BOBMomBreath;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBMomBreathResponse extends BOBResponse {

	private List<BOBMomBreath> exams;

	public List<BOBMomBreath> getExams() {
		return exams;
	}

	public void setExams(List<BOBMomBreath> exams) {
		this.exams = exams;
	}
}
