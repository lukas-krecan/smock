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

import java.util.Collections;

import javax.xml.transform.Source;

import net.javacrumbs.smock.common.SmockCommon;
import net.javacrumbs.smock.common.TemplateAwareMessageCompareMatcher;
import net.javacrumbs.smock.common.TemplateAwareMessageCreator;

public class SmockServer extends SmockCommon {
	//TODO add all variants of methods (Source, Resource, String args)
    /**
     * Create a request with the given {@link Source} XML as payload.
     *
     * @param payload the request payload
     * @return the request creator
     */
    public static ParametrizableRequestCreator withContent(Source content) {
    	
    	return new TemplateAwareMessageCreator(loadDocument(content),Collections.<String, Object>emptyMap(), getTemplateProcessor());
    }
    /**
     * Create a request with the given {@link Source} XML as payload.
     *
     * @param payload the request payload
     * @return the request creator
     */
    public static ParametrizableRequestCreator withContent(String contentResource) {
    	return withContent(fromResource(contentResource));
    }
    
    public static ParametrizableResponseMatcher message(String messageResource)
    {
    	return message(fromResource(messageResource));
    }
    
	public static ParametrizableResponseMatcher message(Source content) {
		return new TemplateAwareMessageCompareMatcher(loadDocument(content),Collections.<String, Object>emptyMap(), getTemplateProcessor());
	}
}
