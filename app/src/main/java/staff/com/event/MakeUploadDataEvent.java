package staff.com.event;

public class MakeUploadDataEvent {
    private String data;

    public MakeUploadDataEvent(String result) {
        this.data = result;
    }

    public String getResponse() {
        return data;
    }
}
