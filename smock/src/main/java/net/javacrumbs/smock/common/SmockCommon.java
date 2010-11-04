package net.javacrumbs.smock.common;
import java.io.IOException;

import javax.xml.transform.Source;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
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
 * Adds extra common features to {@link WebServiceMock}. 
 */
public abstract class SmockCommon  {
			
	private static TemplateProcessor templateProcessor = new XsltTemplateProcessor();
	
	private static ResourceLoader resourceLoader = new DefaultResourceLoader();
	
	
	protected static Document loadDocument(Source message) {
		return XmlUtil.getInstance().loadDocument(message);
	}	
    
    protected static ResourceSource createResourceSource(Resource resource) {
        try {
            return new ResourceSource(resource);
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
    	return createResourceSource(resource(location));
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
    public static void setResourceLoader(ResourceLoader resourceLoader) {
    	Assert.notNull(resourceLoader, "'resourceLoader' can not be null");
    	SmockCommon.resourceLoader = resourceLoader;
	}
}
