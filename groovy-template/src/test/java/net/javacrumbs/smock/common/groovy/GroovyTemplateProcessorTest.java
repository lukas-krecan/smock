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

package net.javacrumbs.smock.common.groovy;

import static java.util.Collections.singletonMap;
import static net.javacrumbs.smock.common.XmlUtil.loadDocument;
import static net.javacrumbs.smock.common.XmlUtil.serialize;
import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

import java.io.IOException;

import javax.xml.transform.Source;

import net.javacrumbs.smock.common.groovy.GroovyTemplateProcessor;

import org.junit.Test;
import org.springframework.xml.transform.StringSource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class GroovyTemplateProcessorTest {

	@Test
	public void testSimple() throws SAXException, IOException
	{
		GroovyTemplateProcessor processor = new GroovyTemplateProcessor();
		Document template = loadDocument(new StringSource("<a>$value</a>"));
		
		
		Document result = processor.processTemplate(template, null, singletonMap("value", (Object)"test"));
		assertXMLEqual("<a>test</a>", serialize(result));
	}
	@Test
	public void testWithRequest() throws SAXException, IOException
	{
		GroovyTemplateProcessor processor = new GroovyTemplateProcessor();
		Document template = loadDocument(new StringSource("<a>$value $requestB.b</a>"));
		Source request = new StringSource("<requestB xmlns:ns=\"http://example.org/abc\"><ns:b>2</ns:b></requestB>");	
		
		Document result = processor.processTemplate(template, request, singletonMap("value", (Object)"test"));
		assertXMLEqual("<a>test 2</a>", serialize(result));
	}
}
