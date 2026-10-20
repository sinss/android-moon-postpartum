package li.com.bobsonclinic.data.server.base;

/**
 * Created by Zack on 8/8/16.
 */
public class BOBResponse {

    protected String status;
    protected String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
