package testmodels.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.runner.Runner;

@Data
@AllArgsConstructor
public class IncorrectRequest extends Request {

    private String someParameter;
}
