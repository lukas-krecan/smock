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

package net.javacrumbs.smock.mockito.client;

import java.io.IOException;

import org.mockito.ArgumentMatcher;
import org.springframework.ws.test.client.RequestMatcher;

class SmockArgumentMatcher  extends ArgumentMatcher<Object> {
	private final RequestMatcher matcher;

	public SmockArgumentMatcher(RequestMatcher matcher) {
		this.matcher = matcher;
	}

	public boolean matches(Object argument) {
		try {
			matcher.match(null, SmockMockitoClient.serialize(argument));
		} catch (IOException e) {
			throw new IllegalArgumentException("Can not match the request.",e);
		} 
		return true;
	}
}