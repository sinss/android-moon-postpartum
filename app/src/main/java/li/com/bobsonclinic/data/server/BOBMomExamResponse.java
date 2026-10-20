package li.com.bobsonclinic.data.server;

import java.util.LinkedList;

import li.com.bobsonclinic.data.BOBMomExam;
import li.com.bobsonclinic.data.server.base.BOBResponse;

;

public class BOBMomExamResponse extends BOBResponse {

	private LinkedList<BOBMomExam> exams;

	public LinkedList<BOBMomExam> getExams() {
		return exams;
	}

	public void setExams(LinkedList<BOBMomExam> exams) {
		this.exams = exams;
	}
}
