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

import net.javacrumbs.smock.common.AbstractMatcher;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.test.server.ResponseMatcher;
import org.w3c.dom.Document;

public class MessageResponseMatcher extends AbstractMatcher implements ResponseMatcher {

	public MessageResponseMatcher(Document controlMessage) {
		super(controlMessage);
	}
	
	public void match(WebServiceMessage response)
	{
		matchInternal(response);
	}

}
