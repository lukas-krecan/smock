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

package net.javacrumbs.smock.server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.xml.transform.TransformerException;

import net.javacrumbs.smock.common.AbstractSmockTest;
import net.javacrumbs.smock.common.XsltTemplateProcessor;

import org.custommonkey.xmlunit.XMLAssert;
import org.junit.Test;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;
import org.xml.sax.SAXException;


public class TemplateAwareMessageRequestCreatorTest extends AbstractSmockTest{
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
	public void createTemplate() throws IOException, TransformerException, SAXException
	{
		String template = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\"><xsl:template match=\"/\">" + 
						  "<element xmlns='http://example.com'/></xsl:template></xsl:stylesheet>";
		String expectedRequest = "<element xmlns='http://example.com'/>";
		doCreateTest(template, expectedRequest);
	}
	@Test
	public void createWithTemplateParameters() throws IOException, TransformerException, SAXException
	{
		String template = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">" + 
		"<xsl:param name=\"a\"/>" +
		"<xsl:template match=\"/\">" +
		"<element xmlns='http://example.com'><xsl:value-of select=\"$a\"/></element>" +
		"</xsl:template></xsl:stylesheet>";
		String expectedRequest = "<element xmlns='http://example.com'>5</element>";
		
		doCreateTest(template, expectedRequest, Collections.<String, Object>singletonMap("a", 5));
	}
	
	@Test
	public void createNoTemplate() throws IOException, TransformerException, SAXException
	{
		
		String request = "<element xmlns='http://example.com'/>";
		String expectedRequest = "<element xmlns='http://example.com'/>";
		doCreateTest(request, expectedRequest);
	}
	@Test
	public void testAddParameters() throws IOException, TransformerException, SAXException
	{
		String request = "<element xmlns='http://example.com'/>";
		TemplateAwareMessageRequestCreator creator = new TemplateAwareMessageRequestCreator(loadDocument(new StringSource(request)), Collections.<String, Object>emptyMap(), new XsltTemplateProcessor());
		TemplateAwareMessageRequestCreator creator2 = (TemplateAwareMessageRequestCreator) creator.withParameter("a", 1);
		assertEquals(Collections.singletonMap("a",1),creator2.getParameters());
	}
	
	private void doCreateTest(String template, String expectedRequest) throws IOException, TransformerException, SAXException {
		doCreateTest(template, expectedRequest, Collections.<String, Object>emptyMap());
		
	}

	private void doCreateTest(String template, String expectedRequest, Map<String, Object> parameters) throws IOException, TransformerException, SAXException {
		TemplateAwareMessageRequestCreator creator = new TemplateAwareMessageRequestCreator(loadDocument(new StringSource(template)), parameters, new XsltTemplateProcessor());
		
		
		WebServiceMessage response = creator.createRequest(FACTORY);
		
		StringResult result = new StringResult();
		transform(response.getPayloadSource(), result);
		XMLAssert.assertXMLEqual(expectedRequest, result.toString());
	}
	

}
