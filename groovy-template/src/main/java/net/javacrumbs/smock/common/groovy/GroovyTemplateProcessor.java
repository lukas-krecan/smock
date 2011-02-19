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
package net.javacrumbs.smock.common.groovy;

import static net.javacrumbs.smock.common.XmlUtil.loadDocument;
import static net.javacrumbs.smock.common.XmlUtil.serialize;
import groovy.text.SimpleTemplateEngine;
import groovy.text.TemplateEngine;
import groovy.util.XmlSlurper;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;

import net.javacrumbs.smock.common.TemplateProcessor;

import org.springframework.xml.transform.StringSource;
import org.w3c.dom.Document;

/**
 * Groovy based template engine.
 * @author Lukas Krecan
 */
public class GroovyTemplateProcessor implements TemplateProcessor {
	private final TemplateEngine templateEngine;
	
	public GroovyTemplateProcessor(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	public GroovyTemplateProcessor() {
		this(new SimpleTemplateEngine());
	}

	public Source processTemplate(Source template, Source input, Map<String, Object> parameters) {
		try {
			String templateText = serialize(template);
			HashMap<String, Object>  binding = new HashMap<String, Object> (parameters);
			if (input!=null)
			{
				Document inputDocument = loadDocument(input);
				binding.put(inputDocument.getFirstChild().getLocalName(), new XmlSlurper().parse(new StringReader(serialize(inputDocument))));
			}
			return new StringSource(templateEngine.createTemplate(templateText).make(binding).toString());
		} catch (Exception e) {
			throw new IllegalArgumentException("Can not process Groovy template.",e);
		}
	}


}
