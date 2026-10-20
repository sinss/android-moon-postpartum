package li.com.bobsonclinic.data.server;

import java.util.List;

import li.com.bobsonclinic.data.BOBBabyJaundice;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBBabyJaundiceResponse extends BOBResponse {

	private List<BOBBabyJaundice> exams;

	public List<BOBBabyJaundice> getExams() {
		return exams;
	}

	public void setExams(List<BOBBabyJaundice> exams) {
		this.exams = exams;
	}
}
