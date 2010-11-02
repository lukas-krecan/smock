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

import net.javacrumbs.smock.common.AbstractSmockTest;
import net.javacrumbs.smock.common.XmlUtil;

import org.custommonkey.xmlunit.XMLAssert;
import org.junit.Test;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.xml.transform.StringSource;
import org.w3c.dom.Document;


public class MessageRequestCreatorTest extends AbstractSmockTest{
	@Test
	public void testCreateFromMessage() throws Exception
	{
		Document sourceDocument = loadDocument(new StringSource(MESSAGE));
		MessageRequestCreator requestCreator = new MessageRequestCreator(sourceDocument);
		SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
		messageFactory.afterPropertiesSet();
		
		SoapMessage response = (SoapMessage) requestCreator.createRequest(messageFactory);
		
		Document generatedDocument = loadDocument(response.getEnvelope().getSource());
		System.out.println(XmlUtil.getInstance().serialize(generatedDocument));
		System.out.println(XmlUtil.getInstance().serialize(sourceDocument));
		XMLAssert.assertXMLEqual(sourceDocument, generatedDocument);
	}
	@Test
	public void testCreateFromPayload() throws Exception
	{
		Document sourceDocument = loadDocument(new StringSource(PAYLOAD));
		MessageRequestCreator requestCreator = new MessageRequestCreator(sourceDocument);
		SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
		messageFactory.afterPropertiesSet();
		
		SoapMessage response = (SoapMessage) requestCreator.createRequest(messageFactory);
		
		Document generatedDocument = loadDocument(response.getPayloadSource());
		System.out.println(XmlUtil.getInstance().serialize(generatedDocument));
		System.out.println(XmlUtil.getInstance().serialize(sourceDocument));
		XMLAssert.assertXMLEqual(sourceDocument, generatedDocument);
	}
}
