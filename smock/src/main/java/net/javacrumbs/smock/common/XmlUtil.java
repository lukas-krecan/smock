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

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.springframework.ws.soap.SoapVersion;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.TransformerObjectSupport;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.w3c.dom.Document;

/**
 * Helper class for work with XML.
 * @author Lukas Krecan
 *
 */
public class XmlUtil extends TransformerObjectSupport {
	private static final XmlUtil INSTANCE = new XmlUtil();

	private static final Map<String, String> SOAP_NAMESPACES = new HashMap<String, String>();
	
	static{
		SOAP_NAMESPACES.put("soap11", SoapVersion.SOAP_11.getEnvelopeNamespaceUri());
		SOAP_NAMESPACES.put("soap12", SoapVersion.SOAP_12.getEnvelopeNamespaceUri());
	};
	
	
	
	private XmlUtil()
	{
		
	}
	
	public static XmlUtil getInstance() {
		return INSTANCE;
	}

	public Document loadDocument(Source source)
	{
		DOMResult result = new DOMResult();
		doTransform(source, result);
		return (Document)result.getNode();
	}
	
	public boolean isSoap(Document document) {
		for (String prefix: SOAP_NAMESPACES.keySet()) {
			String expression = "/"+prefix+":Envelope";
			XPathExpression xPathExpression = XPathExpressionFactory.createXPathExpression(expression, SOAP_NAMESPACES);
			if (xPathExpression.evaluateAsBoolean(document))
			{
				return true;
			}
		}
		return false;
	}
	
	public String serialize(Document document)
	{
		return serialize(new DOMSource(document));
	}
	String serialize(Source source)
	{
		StringResult result = new StringResult();
		doTransform(source, result);
		return result.toString();
		
	}
	
	public void doTransform(Source source, Result result) {
		try {
			transform(source, result);
		} catch (TransformerException e) {
			throw new IllegalArgumentException("Can not transform",e);
		}
	}
}
