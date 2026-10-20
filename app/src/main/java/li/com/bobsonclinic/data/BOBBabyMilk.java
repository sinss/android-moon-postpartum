package li.com.bobsonclinic.data;

;

public class BOBBabyMilk {
	private int mom_bottle_milk;
	private int formula_milk;
	private String mom_breast_milk;
	private String exam_datetime;


	public int getMom_bottle_milk() {
		return mom_bottle_milk;
	}

	public void setMom_bottle_milk(int mom_bottle_milk) {
		this.mom_bottle_milk = mom_bottle_milk;
	}

	public int getFormula_milk() {
		return formula_milk;
	}

	public void setFormula_milk(int formula_milk) {
		this.formula_milk = formula_milk;
	}

	public boolean getMom_breast_milk() {
		if("yes".equals(mom_breast_milk)){
			return true;
		}
		return false;
	}

	public void setMom_breast_milk(String mom_breast_milk) {
		this.mom_breast_milk = mom_breast_milk;
	}

	public String getExam_datetime() {
		return exam_datetime;
	}

	public void setExam_datetime(String exam_datetime) {
		this.exam_datetime = exam_datetime;
	}
}
