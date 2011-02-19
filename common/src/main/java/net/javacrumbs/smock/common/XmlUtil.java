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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.TransformerHelper;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.w3c.dom.Document;

/**
 * Helper class for work with XML.
 * @author Lukas Krecan
 *
 */
public class XmlUtil {
	public static final Charset UTF8_CHARSET = Charset.availableCharsets().get("UTF-8");

	private static final Map<String, String> SOAP_NAMESPACES = new HashMap<String, String>();
	
	static{
		SOAP_NAMESPACES.put("soap11", SoapVersion.SOAP_11.getEnvelopeNamespaceUri());
		SOAP_NAMESPACES.put("soap12", SoapVersion.SOAP_12.getEnvelopeNamespaceUri());
	};
		
	private XmlUtil()
	{
		
	}
	
	/**
	 * Loads document from source.
	 * @param source
	 * @return
	 */
	public static Document loadDocument(Source source)
	{
		DOMResult result = new DOMResult();
		transform(source, result);
		return (Document)result.getNode();
	}
	
	/**
	 * Returns true if the documents root is SOAP envelope.
	 * @param document
	 * @return
	 */
	public static boolean isSoap(Source source) {
		for (String prefix: SOAP_NAMESPACES.keySet()) {
			String expression = "/"+prefix+":Envelope";
			Jaxp13XPathTemplate xpathTemplate = new Jaxp13XPathTemplate();
			xpathTemplate.setNamespaces(SOAP_NAMESPACES);
			if (xpathTemplate.evaluateAsBoolean(expression, source))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Converts {@link document} to {@link String}.
	 * @param document
	 * @return
	 */
	public static String serialize(Document document)
	{
		return serialize(new DOMSource(document));
	}
	
	/**
	 * Returns source of message envelope 
	 * @param message
	 * @return
	 */
	public static Source getEnvelopeSource(WebServiceMessage message) {
		return ((SoapMessage)message).getEnvelope().getSource();
	}
	
	/**
	 * Converts {@link Source} to {@link String}.
	 * @param source
	 * @return
	 */
	public static String serialize(Source source)
	{
		StringResult result = new StringResult();
		transform(source, result);
		return result.toString();
		
	}
	
	/**
	 * Does transofrmation.
	 * @param source
	 * @param result
	 */
	public static void transform(Source source, Result result) {
		try {
			new TransformerHelper().transform(source, result);
		} catch (TransformerException e) {
			throw new IllegalArgumentException("Can not transform",e);
		}
	}
	
	/**
	 * Streams the document as UTF-8 encoded stream.
	 * @param document
	 * @return
	 */
	public static InputStream getDocumentAsStream(Document document)
	{
		return new ByteArrayInputStream(serialize(document).getBytes(UTF8_CHARSET));
	}
	
	/**
	 * Streams the source as UTF-8 encoded stream.
	 * @param document
	 * @return
	 */
	public static InputStream getSourceAsStream(Source source)
	{
		return new ByteArrayInputStream(serialize(source).getBytes(UTF8_CHARSET));
	}
}
