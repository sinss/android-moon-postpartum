package li.com.bobsonclinic.data;

;

public class BOBBabies {
	private String id;
	private String name;
	private String birth_date;
	private String mom_id;
	private String created_at;
	private String updated_at;
	private String deleted_at;
	private String camera_id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirth_date() {
		return birth_date;
	}

	public void setBirth_date(String birth_date) {
		this.birth_date = birth_date;
	}

	public String getMom_id() {
		return mom_id;
	}

	public void setMom_id(String mom_id) {
		this.mom_id = mom_id;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public String getDeleted_at() {
		return deleted_at;
	}

	public void setDeleted_at(String deleted_at) {
		this.deleted_at = deleted_at;
	}

	public String getCamera_id() {
		return camera_id;
	}

	public void setCamera_id(String camera_id) {
		this.camera_id = camera_id;
	}
}
