package net.javacrumbs.smock.common.client.connection;

import org.springframework.ws.test.client.RequestMatcher;
import org.springframework.ws.test.client.ResponseActions;

public interface MockWebServiceServer {

	ResponseActions expect(RequestMatcher requestMatcher);

}
