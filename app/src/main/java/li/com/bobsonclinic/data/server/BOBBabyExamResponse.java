package li.com.bobsonclinic.data.server;

import java.util.LinkedList;

import li.com.bobsonclinic.data.BOBBabyExam;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBBabyExamResponse extends BOBResponse {
	private LinkedList<BOBBabyExam> exams;

	public LinkedList<BOBBabyExam> getExams() {
		return exams;
	}

	public void setExams(LinkedList<BOBBabyExam> exams) {
		this.exams = exams;
	}
}
