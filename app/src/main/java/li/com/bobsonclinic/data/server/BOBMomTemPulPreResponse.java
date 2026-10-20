package li.com.bobsonclinic.data.server;

import java.util.List;

import li.com.bobsonclinic.data.BOBMomTemPulPre;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBMomTemPulPreResponse extends BOBResponse {

	private List<BOBMomTemPulPre> exams;

	public List<BOBMomTemPulPre> getExams() {
		return exams;
	}

	public void setExams(List<BOBMomTemPulPre> exams) {
		this.exams = exams;
	}
}
