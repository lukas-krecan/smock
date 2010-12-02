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

package net.javacrumbs.smock.common;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Test;
import org.springframework.xml.transform.StringSource;
import org.w3c.dom.Document;


public class UniversalMessageMatcherTest extends AbstractSmockTest{

	
	@Test
	public void testSame()
	{
		Document document = loadDocument(new StringSource(MESSAGE));
		UniversalMessageMatcher matcher = new UniversalMessageMatcher(document);
		
		Message request = createMock(Message.class);
		expect(request.getEnvelopeSource()).andReturn(new StringSource(MESSAGE));
		replay(request);
		
		matcher.match(null, request);
		
		verify(request);
	}
	@Test(expected=AssertionError.class)
	public void testDifferent()
	{
		Document document = loadDocument(new StringSource(MESSAGE));
		UniversalMessageMatcher matcher = new UniversalMessageMatcher(document);
		
		Message request = createMock(Message.class);
		expect(request.getEnvelopeSource()).andReturn(new StringSource(MESSAGE2));
		replay(request);
		
		matcher.match(null, request);
		
		verify(request);
	}
	@Test
	public void testPayload()
	{
		Document document = loadDocument(new StringSource(PAYLOAD));
		UniversalMessageMatcher matcher = new UniversalMessageMatcher(document);
		
		Message request = createMock(Message.class);
		expect(request.getPayloadSource()).andReturn(new StringSource(PAYLOAD));
		replay(request);
		
		matcher.match(null, request);
		
		verify(request);
	}
	@Test(expected=AssertionError.class)
	public void testPayloadDifferent()
	{
		Document document = loadDocument(new StringSource(PAYLOAD));
		UniversalMessageMatcher matcher = new UniversalMessageMatcher(document);
		
		Message request = createMock(Message.class);
		expect(request.getPayloadSource()).andReturn(new StringSource(PAYLOAD2));
		replay(request);
		
		matcher.match(null, request);
		
		verify(request);
	}
}
