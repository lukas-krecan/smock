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

package net.javacrumbs.smock.springws.common;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.ws.WebServiceMessageFactory;

import net.javacrumbs.smock.common.Message;
import net.javacrumbs.smock.common.MessageFactory;

public class WebServiceMessageFactoryWrapper implements MessageFactory {

	private final WebServiceMessageFactory webServiceMessageFactory;
	
	public WebServiceMessageFactoryWrapper(WebServiceMessageFactory webServiceMessageFactory) {
		this.webServiceMessageFactory = webServiceMessageFactory;
	}

	public Message createWebServiceMessage() {
		return new WebServiceMessageWrapper(webServiceMessageFactory.createWebServiceMessage());
	}

	public Message createWebServiceMessage(InputStream inputStream) throws IOException {
		return  new WebServiceMessageWrapper(webServiceMessageFactory.createWebServiceMessage(inputStream));
	}

}
