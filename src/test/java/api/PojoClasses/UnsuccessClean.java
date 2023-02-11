package api.PojoClasses;

public class UnsuccessClean {
    private String error;

    public UnsuccessClean() {
    }

    public UnsuccessClean(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
