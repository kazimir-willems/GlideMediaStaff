package delivery.com.event;

public class DespatchStoreEvent {
    private int result;

    public DespatchStoreEvent(int result) {
        this.result = result;
    }

    public int getResponse() {
        return result;
    }
}
