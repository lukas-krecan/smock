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
package net.javacrumbs.calc;

import static org.junit.Assert.assertEquals;
import net.javacrumbs.smock.springws.client.AbstractSmockClientTest;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.interceptor.PayloadLoggingInterceptor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:client-config.xml"})
public class CalcInterceptorSimpleTest extends AbstractSmockClientTest{
    @Autowired
    private Calc calc;
    
    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext)
    {
    	createServer(applicationContext, new EndpointInterceptor[]{new PayloadLoggingInterceptor()});
    }
    
    @After
    public void verify()
    {
    	super.verify();
    }

	@Test
	public void testSimple()
	{
		expect(anything()).andRespond(withMessage("response1.xml"));

		int result = calc.plus(1, 2);
		assertEquals(3, result);
	}
}
