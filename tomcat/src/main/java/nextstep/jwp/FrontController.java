package nextstep.jwp;

import nextstep.exception.NotFoundControllerException;
import nextstep.jwp.controller.Controller;
import org.apache.coyote.request.HttpRequest;
import org.apache.coyote.response.HttpResponse;

import java.io.IOException;

public class FrontController {

    private final RequestMapping requestMapping;

    public FrontController(RequestMapping requestMapping) {
        this.requestMapping = requestMapping;
    }

    public void execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String path = httpRequest.getPath();
        Controller controller = requestMapping.getController(httpRequest.getHttpMethod(), path);
        System.out.println(controller.getClass());
        if (controller == null) {
            throw new NotFoundControllerException(httpRequest.getPath() + httpRequest.getRequestBody() + " 처리불가한 요청");
        }
        controller.execute(httpRequest, httpResponse);
    }
}
