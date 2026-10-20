package li.com.bobsonclinic.data.server;

import java.util.List;

import li.com.bobsonclinic.data.BOBBabyMilk;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBBabyMilkResponse extends BOBResponse {

	private List<BOBBabyMilk> exams;

	public List<BOBBabyMilk> getExams() {
		return exams;
	}

	public void setExams(List<BOBBabyMilk> exams) {
		this.exams = exams;
	}
}
