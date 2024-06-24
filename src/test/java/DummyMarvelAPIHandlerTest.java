import marvel.model.*;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class DummyMarvelAPIHandlerTest {

    @Test
    public void getRequestNullOrEmptyParameterTest(){
        InputAPI input = new DummyMarvelAPIHandler();
        assertThat(input.getRequest(null),equalTo(null));
        assertThat(input.getRequest(""),equalTo(null));
    }

    @Test
    public void getRequestInvalidParameterTest(){
        InputAPI input = new DummyMarvelAPIHandler();
        assertThat(input.getRequest("123"),equalTo(null));
        assertThat(input.getRequest("www.google.com"),equalTo(null));
    }
}
