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
package net.javacrumbs.calc.server;

import net.javacrumbs.calc.model.PlusRequest;
import net.javacrumbs.calc.model.PlusResponse;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

@Endpoint
public class CalcEndpoint {

	@PayloadRoot(localPart = "plusRequest",  namespace = "http://javacrumbs.net/calc")
	public PlusResponse plus(PlusRequest plus)
	{
		if ((long)plus.getA()+(long)plus.getB()>Integer.MAX_VALUE)
		{
			throw new IllegalArgumentException("Values are too big.");
		}
		PlusResponse result = new PlusResponse();
		result.setResult(plus.getA() + plus.getB());
		return result;
	}
}
