package li.com.bobsonclinic.data;

;

public class BOBNotifaction {
	private String id;
	private String message;
	private String push_at;
	private String created_at;
	private String updated_at;
	private String deleted_at;
	private String status;
	private String mom_id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPush_at() {
		return push_at;
	}

	public void setPush_at(String push_at) {
		this.push_at = push_at;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMom_id() {
		return mom_id;
	}

	public void setMom_id(String mom_id) {
		this.mom_id = mom_id;
	}
}
