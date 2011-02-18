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
import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.test.support.MockStrategiesHelper;
import org.springframework.xml.transform.ResourceSource;
import org.w3c.dom.Document;

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

/**
 * Adds extra features to {@link WebServiceMock}. 
 */
public abstract class SmockCommon  {
			
	private static TemplateProcessor templateProcessor = new XsltTemplateProcessor();
	
	private static ResourceLoader resourceLoader = new DefaultResourceLoader();
	
	/**
	 * Loads {@link Document} from {@link Source} 
	 * @param message
	 * @return
	 */
	protected static Document loadDocument(Source message) {
		return XmlUtil.loadDocument(message);
	}	
    
	/**
	 * Creates {@link Source} from {@link Resource}.
	 * @param resource
	 * @return
	 */
    protected static Source createSource(Resource resource) {
        try {
        	//we need to read the source multiple times thus using DOMSource
        	return new DOMSource(loadDocument(new ResourceSource(resource)));
        }
        catch (IOException ex) {
            throw new IllegalArgumentException(resource + " could not be opened", ex);
        }
    }
    
    /**
     * Loads resource using resourceLoader set by {@link #setResourceLoader(ResourceLoader)}.
     * @param location Location of the resource 
     */
    public static Source fromResource(String location)
    {
    	return createSource(resource(location));
    }
    /**
     * Loads resource using resourceLoader set by {@link #setResourceLoader(ResourceLoader)}.
     * @param location Location of the resource 
     */
    public static Resource resource(String location)
    {
    	return resourceLoader.getResource(location);
    }
    
    public static TemplateProcessor getTemplateProcessor() {
		return templateProcessor;
	}
    
    /**
     * Sets {@link TemplateProcessor} used by Smock.
     * @param templateProcessor
     */
    public static void setTemplateProcessor(TemplateProcessor templateProcessor) {
    	Assert.notNull(templateProcessor, "'templateProcessor' can not be null");
		SmockCommon.templateProcessor = templateProcessor;
	}
    public static ResourceLoader getResourceLoader() {
		return resourceLoader;
	}
    /**
     * Sets {@link ResourceLoader} to be used by Smock.
     * @param resourceLoader
     */
    public static void setResourceLoader(ResourceLoader resourceLoader) {
    	Assert.notNull(resourceLoader, "'resourceLoader' can not be null");
    	SmockCommon.resourceLoader = resourceLoader;
	}
    /**
     * Creates a {@link WebServiceMessageFactory} using {@link MockStrategiesHelper}.
     * @param applicationContext
     * @return
     */
    public static WebServiceMessageFactory withMessageFactory(ApplicationContext applicationContext)
    {
    	if (applicationContext==null) return withMessageFactory();
    	return new MockStrategiesHelper(applicationContext).getStrategy(WebServiceMessageFactory.class, SaajSoapMessageFactory.class);
    }

    /**
     * Creates SAAJ based message factory for SOAP 1.1.
     * @return
     */
    public static WebServiceMessageFactory withMessageFactory()
    {
    	return withMessageFactory(SoapVersion.SOAP_11);
    }
    /**
     * Creates SAAJ based message factory.
     * @param soapVersion
     * @return
     */
    public static WebServiceMessageFactory withMessageFactory(SoapVersion soapVersion)
    {
    	SaajSoapMessageFactory saajSoapMessageFactory = new SaajSoapMessageFactory();
   		saajSoapMessageFactory.setSoapVersion(soapVersion);
    	saajSoapMessageFactory.afterPropertiesSet();
    	return saajSoapMessageFactory;
    }
    
}
