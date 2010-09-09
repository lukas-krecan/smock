/*
 * Copyright 2005-2010 the original author or authors.
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

package net.javacrumbs.mock;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.custommonkey.xmlunit.XMLAssert;
import org.junit.Test;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;
import org.xml.sax.SAXException;


public class TemplateAwareMessageResponseCreatorTest extends AbstractSmockTest{
		private static final SaajSoapMessageFactory FACTORY = new SaajSoapMessageFactory();
		
		static
		{
			try {
				FACTORY.afterPropertiesSet();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		@Test
		public void callbackTemplate() throws IOException, TransformerException, SAXException
		{
			String template = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\"><xsl:template match=\"/\">" + 
							  "<element xmlns='http://example.com'/></xsl:template></xsl:stylesheet>";
			String request = "<element xmlns='http://example.com'/>";
			String expectedResponse = "<element xmlns='http://example.com'/>";
			doCallbackTest(template, request, expectedResponse);
		}
		@Test
		public void callbackTemplateParameters() throws IOException, TransformerException, SAXException
		{
			String template = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">" + 
			"<xsl:param name=\"a\"/>" +
			"<xsl:template match=\"/\">" +
			"<element xmlns='http://example.com'><xsl:value-of select=\"$a\"/></element>" +
			"</xsl:template></xsl:stylesheet>";
			String expectedResponse = "<element xmlns='http://example.com'>5</element>";
			String request = "<element xmlns='http://example.com'/>";
			
			doCallbackTest(template, request, expectedResponse, Collections.<String, Object>singletonMap("a", 5));
		}
		@Test
		public void callbackTemplateRequest() throws IOException, TransformerException, SAXException
		{
			String template = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">" +
			"<xsl:param name=\"a\"/>" +
			"<xsl:template match=\"/\">" +
			"<element xmlns='http://example.com' xmlns:tns='http://example.com'><xsl:value-of select=\"//tns:request\"/></element>" +
			"</xsl:template></xsl:stylesheet>";
			String expectedResponse = "<element xmlns='http://example.com'>test</element>";
			String request = "<request xmlns='http://example.com'>test</request>";
			
			doCallbackTest(template, request, expectedResponse, Collections.<String, Object>emptyMap());
		}
	
		@Test
		public void callbackNoTemplate() throws IOException, TransformerException, SAXException
		{
			
			String request = "<element xmlns='http://example.com'/>";
			String expectedResponse = "<element xmlns='http://example.com'/>";
			doCallbackTest(expectedResponse, request, expectedResponse);
		}
		
		private void doCallbackTest(String template, String request, String expectedResponse) throws IOException, TransformerException, SAXException {
			doCallbackTest(template, request, expectedResponse, Collections.<String, Object>emptyMap());
			
		}
	
		private void doCallbackTest(String template, String request, String expectedResponse, Map<String, Object> parameters) throws IOException, TransformerException, SAXException {
			TemplateAwareMessageResponseCreator callback = new TemplateAwareMessageResponseCreator(loadDocument(new StringSource(template)), parameters, new XsltTemplateProcessor());
			
			
			WebServiceMessage response = callback.createResponse(TEST_URI, createMessage(request), FACTORY);
			
			StringResult result = new StringResult();
			transform(response.getPayloadSource(), result);
			XMLAssert.assertXMLEqual(expectedResponse, result.toString());
		}
	

		private WebServiceMessage createMessage(String request) throws IOException, TransformerException {
			SaajSoapMessage message = FACTORY.createWebServiceMessage();
			transform(new StringSource(request), message.getPayloadResult());
			return message;
		}
}
