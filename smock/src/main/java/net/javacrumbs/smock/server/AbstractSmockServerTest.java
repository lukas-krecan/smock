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

import javax.xml.transform.Source;

import org.springframework.core.io.Resource;
import org.w3c.dom.Document;

public class AbstractSmockServerTest extends AbstractWebServiceServerTest {
	
	/**
	 * Create a request with the given {@link Source} XML as payload.
	 *
	 * @param payload the request payload
	 * @return the request creator
	 */
	public  ParametrizableRequestCreator withMessage(String messageResource) {
		return SmockServer.withMessage(messageResource);
	}
	/**
	 * Create a request with the given {@link Source} XML as payload.
	 *
	 * @param payload the request payload
	 * @return the request creator
	 */
	public  ParametrizableRequestCreator withMessage(Resource messageResource) {
		return SmockServer.withMessage(messageResource);
	}
    /**
     * Create a request with the given {@link Source} XML as payload.
     *
     * @param payload the request payload
     * @return the request creator
     */
    public  ParametrizableRequestCreator withMessage(Source message) {
    	return SmockServer.withMessage(message);
    }
    public  ParametrizableRequestCreator withMessage(Document message) {
    	return SmockServer.withMessage(message);
    }
    
    public  ParametrizableResponseMatcher message(String messageResource)
    {
    	return SmockServer.message(messageResource);
    }
    
    public  ParametrizableResponseMatcher message(Resource messageResource)
    {
    	return SmockServer.message(messageResource);
    }
    
	public  ParametrizableResponseMatcher message(Source content) {
		return SmockServer.message(content);
	}
	public  ParametrizableResponseMatcher message(Document message) {
		return SmockServer.message(message);
	}
}
