package staff.com.event;

public class StockInfoStoreEvent {
    private int result;

    public StockInfoStoreEvent(int result) {
        this.result = result;
    }

    public int getResponse() {
        return result;
    }
}
