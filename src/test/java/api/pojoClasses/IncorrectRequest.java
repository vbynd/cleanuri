package api.pojoClasses;

public class IncorrectRequest {
    private String someParameter;

    public IncorrectRequest() {
    }

    public IncorrectRequest(String someParameter) {
        this.someParameter = someParameter;
    }

    public String getSomeParameter() {
        return someParameter;
    }
}
