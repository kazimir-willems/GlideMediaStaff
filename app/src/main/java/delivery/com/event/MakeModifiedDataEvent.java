package delivery.com.event;

public class MakeModifiedDataEvent {
    private String data;

    public MakeModifiedDataEvent(String result) {
        this.data = result;
    }

    public String getResponse() {
        return data;
    }
}
