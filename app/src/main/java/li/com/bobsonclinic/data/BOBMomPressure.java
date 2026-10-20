package li.com.bobsonclinic.data;

;

public class BOBMomPressure {
	private int systolic_pressure;
	private int diastolic_pressure;
	private String exam_date;

	public int getSystolic_pressure() {
		return systolic_pressure;
	}

	public void setSystolic_pressure(int systolic_pressure) {
		this.systolic_pressure = systolic_pressure;
	}

	public int getDiastolic_pressure() {
		return diastolic_pressure;
	}

	public void setDiastolic_pressure(int diastolic_pressure) {
		this.diastolic_pressure = diastolic_pressure;
	}

	public String getExam_date() {
		return exam_date;
	}

	public void setExam_date(String exam_date) {
		this.exam_date = exam_date;
	}
}
