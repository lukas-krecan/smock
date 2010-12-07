package net.javacrumbs.smock.common.client.connection;

import static net.javacrumbs.smock.common.client.CommonSmockClient.withMessage;
import static org.junit.Assert.assertNotNull;
import static org.springframework.ws.test.client.RequestMatchers.connectionTo;

import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.core.SourceExtractor;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

//TODO create static variant
public class ThreadLocalMockWebServiceServerTest {

	private static final String ADDRESS = "http://localhost:8080";

	@Test
	public void testUse()
	{
		System.setProperty("java.protocol.handler.pkgs", "net.javacrumbs.smock.common.client.connection.http.protocol");
		ThreadLocalMockWebServiceServer server = new ThreadLocalMockWebServiceServer(getMessageFactory());
		server.expect(connectionTo(ADDRESS)).andRespond(withMessage("response.xml"));
		
		WebServiceTemplate template = new WebServiceTemplate();
		template.setDefaultUri(ADDRESS);
		template.afterPropertiesSet();
		
		
		Source response = template.sendSourceAndReceive(request(), new SourceExtractor<Source>() {
			public Source extractData(Source source) throws IOException, TransformerException {
				return source;
			}
		});
		
		assertNotNull(response);
		
		
	}

	private WebServiceMessageFactory getMessageFactory() {
		SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
		messageFactory.afterPropertiesSet();
		return messageFactory;
	}

	private StreamSource request() {
		return new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("request.xml"));
	}
}
