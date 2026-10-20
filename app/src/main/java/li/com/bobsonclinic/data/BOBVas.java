package li.com.bobsonclinic.data;

;

public class BOBVas {
	private String id;
	private String title;
	private String entry;
	private String css_path;
	private String fee;
	private String remind_message;
	private String status;
	private String created_at;
	private String updated_at;
	private String deleted_at;
	private String img;
	private String join_status;

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public String getJoin_status() {
		return join_status;
	}

	public void setJoin_status(String join_status) {
		this.join_status = join_status;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public String getCss_path() {
		return css_path;
	}

	public void setCss_path(String css_path) {
		this.css_path = css_path;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getRemind_message() {
		return remind_message;
	}

	public void setRemind_message(String remind_message) {
		this.remind_message = remind_message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

}
