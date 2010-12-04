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

package net.javacrumbs.smock.jaxws.server;

import java.net.MalformedURLException;
import java.net.URL;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceProvider;

import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.remoting.jaxws.AbstractJaxWsServiceExporter;
import org.springframework.util.StringUtils;
import org.springframework.ws.test.server.RequestCreator;
import org.springframework.ws.test.server.ResponseActions;


public class MockWebServiceClient {

	public MockWebServiceClient(String... webServicePackageNames) {
		scanForWebServices(webServicePackageNames);
	}

	private void scanForWebServices(String... webServicePackageNames) {
		GenericApplicationContext context = new GenericApplicationContext();
		ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(context, false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(WebService.class));
		scanner.scan(webServicePackageNames);
		context.refresh();
		ServiceExporter serviceExporter = new ServiceExporter();
		serviceExporter.setBeanFactory(context);
		try {
			serviceExporter.afterPropertiesSet();
		} catch (Exception e) {
			new IllegalStateException("Unexpected exception",e);
		}
	}
	
	protected  String generateServiceName(Endpoint endpoint, String serviceName) {
		return "local://"+(StringUtils.hasText(serviceName)?serviceName:endpoint.getImplementor().getClass().getSimpleName()+"Service");
	}

	public ResponseActions sendRequest(String serviceName, RequestCreator requestCreator) {
		Service service = Service.create(new QName(serviceName));
		Dispatch<Source> disp = service.createDispatch(new QName("testMethod"), Source.class, Service.Mode.PAYLOAD);
		Source request = new StreamSource("<hello/>");
		Source response = disp.invoke(request);
		return null;
	}
	
	private class ServiceExporter extends AbstractJaxWsServiceExporter
	{
		@Override
		protected void publishEndpoint(Endpoint endpoint, WebService annotation) {
			endpoint.publish(generateServiceName(endpoint,annotation.serviceName()));
		}

		@Override
		protected void publishEndpoint(Endpoint endpoint, WebServiceProvider annotation) {
			endpoint.publish(generateServiceName(endpoint,annotation.serviceName()));
		}
	}
}
