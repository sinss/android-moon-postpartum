package li.com.bobsonclinic.data;

;

public class BOBBabyCamera {
	private String id;
	private String location;
	private String local_ip_port;
	private String public_ip_port;
	private String baby_id;
	private String created_at;
	private String updated_at;
	private String deleted_at;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocal_ip_port() {
		return local_ip_port;
	}

	public void setLocal_ip_port(String local_ip_port) {
		this.local_ip_port = local_ip_port;
	}

	public String getPublic_ip_port() {
		return public_ip_port;
	}

	public void setPublic_ip_port(String public_ip_port) {
		this.public_ip_port = public_ip_port;
	}

	public String getBaby_id() {
		return baby_id;
	}

	public void setBaby_id(String baby_id) {
		this.baby_id = baby_id;
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
