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


import net.javacrumbs.smock.common.client.ClientTestHelper;

import org.mockito.ArgumentMatcher;
import org.springframework.ws.test.client.RequestMatcher;

public class SmockMockitoClient extends ClientTestHelper{
	/**
	 * Matches a RequestMatcher.
	 * @param matcher
	 * @return
	 */
	public static ArgumentMatcher<?> is(final RequestMatcher matcher)
	{
		return new SmockArgumentMatcher(matcher);
	}
}
