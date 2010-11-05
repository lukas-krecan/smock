package net.javacrumbs.calc.server;

import static net.javacrumbs.smock.server.SmockServer.message;
import static net.javacrumbs.smock.server.SmockServer.withMessage;
import static org.springframework.ws.test.server.MockWebServiceClient.createClient;
import static org.springframework.ws.test.server.ResponseMatchers.xpath;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.ws.test.server.MockWebServiceClient;

@RunWith(Parameterized.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring-ws-servlet.xml"})
public class ParametrizedTest {

	
	private static final Map<String, String> NS_MAP = Collections.singletonMap("ns", "http://javacrumbs.net/calc");
	private MockWebServiceClient wsMockClient;
	private int a;
	private int b;
	private TestContextManager testContextManager;
	
	@Autowired
	public void setApplicationContext(ApplicationContext context)
	{
		wsMockClient = createClient(context);
	}
	
	@Before
	public void injectDependencies() throws Throwable {
		this.testContextManager.prepareTestInstance(this);
	}
	
	
	public ParametrizedTest(int a, int b) {
		this.testContextManager = new TestContextManager(getClass());
		this.a = a;
		this.b = b;
	}
	

	@Parameters
	public static Collection<Integer[]> inputData() {
		return Arrays.asList(new Integer[][] { 
				{ 1, 2}, 
				{ 0, 0},
				{-1, 2},
		});
	}
	
	@Test
	public void testAssertTemplate() throws Exception {
		wsMockClient.sendRequest(withMessage("request-context-xslt.xml").withParameter("a",a).withParameter("b", b)).andExpect(message("response-context-xslt.xml").withParameter("result", a+b));
	}
	@Test
	public void testAssertXpath() throws Exception {
		wsMockClient.sendRequest(withMessage("request-context-xslt.xml").withParameter("a",a).withParameter("b", b)).andExpect(xpath("//ns:result",NS_MAP).evaluatesTo(a+b));
	}
}