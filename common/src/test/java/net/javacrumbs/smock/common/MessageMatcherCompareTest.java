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

import static net.javacrumbs.smock.common.SmockCommon.fromResource;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.WebServiceMessage;
import org.w3c.dom.Document;


public class MessageMatcherCompareTest extends AbstractSmockTest{
	
	
	@Test
	public void testValid() throws IOException
	{
		Document controlDocument = loadDocument("control-message-test.xml");
		MessageMatcher matcher = new MessageMatcher(controlDocument);
		matcher.matchInternal(null, loadMessage("valid-message.xml"));
	}
	
	public void compareDocuments(String control, String test) throws IOException
	{
		Document controlDocument = loadDocument(control);
		MessageMatcher matcher = new MessageMatcher(controlDocument);
		matcher.matchInternal(null, loadMessage(test));
	}
	@Test
	public void testValidTest2() throws IOException
	{
		compareDocuments("control-message-test2.xml","valid-message-test2.xml");
	}
	@Test
	public void testValidTestDifferentNsPrefixes() throws IOException
	{
		compareDocuments("namespace-message1.xml","namespace-message2.xml");
	}
	@Test
	public void testValidTestDifferentNsPrefixesNoPrefix() throws IOException
	{
		compareDocuments("namespace-message1.xml","namespace-message4-no-prefix.xml");
	}
	@Test(expected=AssertionError.class)
	public void testValidTestDifferentNsPrefixesNoPrefixNoDefaultNamespace() throws IOException
	{
		compareDocuments("namespace-message1.xml","namespace-message6-no-prefix-no-default-namespace.xml");
	}
	@Test(expected=AssertionError.class)
	public void testValidTestDifferentNsPrefixesNotResolved() throws IOException
	{
		compareDocuments("namespace-message1.xml","namespace-message3-ns-not-resolved.xml");
	}
	@Test(expected=AssertionError.class)
	public void testValidTestDifferentNsPrefixesNotResolvedinBothFiles() throws IOException
	{
		compareDocuments("namespace-message5-ns-not-resolved.xml","namespace-message3-ns-not-resolved.xml");
	}
	@Test
	public void testValidDifferent() throws IOException
	{
		compareDocuments("control-message-test.xml","valid-message2.xml");
	}
	@Test
	public void testInvalid() throws Exception
	{
		try
		{		
			compareDocuments("control-message-test.xml","invalid-message.xml");
			fail("Exception expected");
		}
		catch(AssertionError e)
		{
			assertFalse(e.getMessage().contains("[not identical]"));
		}
	}
	

	
	private Document loadDocument(String name)
	{
		return loadDocument(fromResource("xml/"+name));
	}
	
	private WebServiceMessage loadMessage(String name) throws IOException {
		return getMessageFactory().createWebServiceMessage(new ClassPathResource("xml/"+name).getInputStream());
	}
}
