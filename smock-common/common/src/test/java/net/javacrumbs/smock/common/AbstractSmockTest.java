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

package net.javacrumbs.smock.common;

import static net.javacrumbs.smock.common.XmlUtil.doTransform;

import java.net.URI;

import javax.xml.transform.Result;
import javax.xml.transform.Source;

import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.w3c.dom.Document;

public abstract class AbstractSmockTest {

	protected static final URI TEST_URI = URI.create("http://localhost");
	protected static final String MESSAGE = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Header ><m:Trans xmlns:m=\"http://www.w3schools.com/transaction/\" soapenv:mustUnderstand=\"1\">234</m:Trans></soapenv:Header><soapenv:Body><test/></soapenv:Body></soapenv:Envelope>";
	protected static final String PAYLOAD = "<test/>";
	protected static final String MESSAGE2 = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Header ><m:Trans xmlns:m=\"http://www.w3schools.com/transaction/\" soapenv:mustUnderstand=\"1\">234</m:Trans></soapenv:Header><soapenv:Body><test2/></soapenv:Body></soapenv:Envelope>";
	protected static final String PAYLOAD2 = "<test2/>";
	
	protected Document loadDocument(Source source) {
		return XmlUtil.loadDocument(source);
	}
	
	protected void transform(Source source, Result result) {
		doTransform(source, result);
	}
	
	protected SaajSoapMessageFactory getMessageFactory() {
		SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
		messageFactory.afterPropertiesSet();
		return messageFactory;
	}
}