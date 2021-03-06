/**
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.smock.common;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;
import org.springframework.ws.WebServiceMessage;
import org.springframework.xml.transform.StringSource;

public class TemplateAwareMessageMatcherTest extends AbstractSmockTest {
	@Test
	public void match() throws Exception {
		String template = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\"><xsl:template match=\"/\">"
				+ "<element xmlns='http://example.com'/></xsl:template></xsl:stylesheet>";
		String xml = "<element xmlns='http://example.com'/>";
		doTest(template, xml, Collections.<String, Object> emptyMap());
	}
	@Test
	public void matchWhitespace() throws Exception {
		String template = "<element xmlns='http://example.com'>\n<element2/>\n</element>";
		String xml = "<element xmlns='http://example.com'><element2/></element>";
		doTest(template, xml, Collections.<String, Object> emptyMap());
	}

	@Test
	public void matchParam() throws Exception {
		String template = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">"
				+ "<xsl:param name=\"a\"/>"
				+ "<xsl:template match=\"/\">"
				+ "<element xmlns='http://example.com'><xsl:value-of select=\"$a\"/></element>"
				+ "</xsl:template></xsl:stylesheet>";
		String xml = "<element xmlns='http://example.com'>5</element>";
		doTest(template, xml, Collections.<String, Object> singletonMap("a", 5));
	}

	@Test
	public void matchTemplateFromRequest() throws Exception {
		String template = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">"
			+ "<xsl:template match=\"/\">"
			+ "<element xmlns='http://example.com'><xsl:value-of select=\"//x\" /></element>"
			+ "</xsl:template></xsl:stylesheet>";
		String input = "<x>6</x>";
		String xml = "<element xmlns='http://example.com'>6</element>";
		doTest(template, xml, Collections.<String, Object> emptyMap(), input);
	}
	
	@Test
	public void matchNoTemplate() throws Exception {
		String xml = "<element xmlns='http://example.com'/>";
		doTest(xml, xml, Collections.<String, Object> emptyMap());
	}

	private void doTest(String template, String message, Map<String, Object> parameters) throws IOException {
		doTest(template, message, parameters, "<a/>");
	}
	private void doTest(String template, String message, Map<String, Object> parameters, String input) throws IOException {
		WebServiceMessage requestMessage = createMock(WebServiceMessage.class);
		WebServiceMessage responseMessage = createMock(WebServiceMessage.class);
		expect(requestMessage.getPayloadSource()).andReturn(new StringSource(input));
		expect(responseMessage.getPayloadSource()).andReturn(new StringSource(message));
		replay(requestMessage, responseMessage);

		TemplateAwareMessageMatcher matcher = new TemplateAwareMessageMatcher(new StringSource(template), parameters, new XsltTemplateProcessor());
		matcher.match(requestMessage, responseMessage);

		verify(requestMessage, responseMessage);
	}
}
