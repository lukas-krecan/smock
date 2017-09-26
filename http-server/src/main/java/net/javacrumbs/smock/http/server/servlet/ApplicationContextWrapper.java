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
package net.javacrumbs.smock.http.server.servlet;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.web.context.WebApplicationContext;

/**
 * Wraps an application context and exposes it as {@link WebApplicationContext}. 
 * It's used because JAX-WS RI needs {@link WebApplicationContext} and uses getBeansOfType which does not 
 * work with parent application context.
 * @author Lukas Krecan
 *
 */
@SuppressWarnings("rawtypes")
class ApplicationContextWrapper implements WebApplicationContext{
	private final ApplicationContext wrappedApplicationContext;
	private final ServletContext servletContext;
	
	
	public ApplicationContextWrapper(ApplicationContext wrappedApplicationContext, ServletContext servletContext) {
		this.wrappedApplicationContext = wrappedApplicationContext;
		this.servletContext = servletContext;
	}
	
	public ServletContext getServletContext() {
		return servletContext;
	}
	
	public void publishEvent(ApplicationEvent event) {
		wrappedApplicationContext.publishEvent(event);
	}

	public void publishEvent(Object o) {
		wrappedApplicationContext.publishEvent(o);
	}

	public BeanFactory getParentBeanFactory() {
		return wrappedApplicationContext.getParentBeanFactory();
	}

	public boolean containsLocalBean(String name) {
		return wrappedApplicationContext.containsLocalBean(name);
	}

	public String getMessage(String code, Object[] args, String defaultMessage,
			Locale locale) {
		return wrappedApplicationContext.getMessage(code, args, defaultMessage,
				locale);
	}

	public Resource getResource(String location) {
		return wrappedApplicationContext.getResource(location);
	}

	public String getMessage(String code, Object[] args, Locale locale)
			throws NoSuchMessageException {
		return wrappedApplicationContext.getMessage(code, args, locale);
	}

	public boolean containsBeanDefinition(String beanName) {
		return wrappedApplicationContext.containsBeanDefinition(beanName);
	}

	public String getId() {
		return wrappedApplicationContext.getId();
	}

	public String getApplicationName() {
		return wrappedApplicationContext.getApplicationName();
	}

	public ClassLoader getClassLoader() {
		return wrappedApplicationContext.getClassLoader();
	}

	public Resource[] getResources(String locationPattern) throws IOException {
		return wrappedApplicationContext.getResources(locationPattern);
	}

	public String getDisplayName() {
		return wrappedApplicationContext.getDisplayName();
	}

	public String getMessage(MessageSourceResolvable resolvable, Locale locale)
			throws NoSuchMessageException {
		return wrappedApplicationContext.getMessage(resolvable, locale);
	}

	public long getStartupDate() {
		return wrappedApplicationContext.getStartupDate();
	}

	public int getBeanDefinitionCount() {
		return wrappedApplicationContext.getBeanDefinitionCount();
	}

	public ApplicationContext getParent() {
		return wrappedApplicationContext.getParent();
	}

	public String[] getBeanDefinitionNames() {
		return wrappedApplicationContext.getBeanDefinitionNames();
	}

	public String[] getBeanNamesForType(ResolvableType resolvableType) {
		return wrappedApplicationContext.getBeanNamesForType(resolvableType);
	}

	public AutowireCapableBeanFactory getAutowireCapableBeanFactory()
			throws IllegalStateException {
		return wrappedApplicationContext.getAutowireCapableBeanFactory();
	}

	public String[] getBeanNamesForType(Class type) {
		return wrappedApplicationContext.getBeanNamesForType(type);
	}

	public String[] getBeanNamesForType(Class type,
			boolean includeNonSingletons, boolean allowEagerInit) {
		return wrappedApplicationContext.getBeanNamesForType(type,
				includeNonSingletons, allowEagerInit);
	}

	public Object getBean(String name) throws BeansException {
		return wrappedApplicationContext.getBean(name);
	}

	public <T> T getBean(String name, Class<T> requiredType)
			throws BeansException {
		return wrappedApplicationContext.getBean(name, requiredType);
	}

	public <T> Map<String, T> getBeansOfType(Class<T> type)
			throws BeansException {
		return wrappedApplicationContext.getBeansOfType(type);
	}

	public <T> T getBean(Class<T> requiredType) throws BeansException {
		return wrappedApplicationContext.getBean(requiredType);
	}

	public Object getBean(String name, Object... args) throws BeansException {
		return wrappedApplicationContext.getBean(name, args);
	}

	public <T> T getBean(Class<T> aClass, Object... objects) throws BeansException {
		return wrappedApplicationContext.getBean(aClass, objects);
	}

	public <T> Map<String, T> getBeansOfType(Class<T> type,
			boolean includeNonSingletons, boolean allowEagerInit)
			throws BeansException {
		return wrappedApplicationContext.getBeansOfType(type,
				includeNonSingletons, allowEagerInit);
	}

	public String[] getBeanNamesForAnnotation(Class<? extends Annotation> aClass) {
		return wrappedApplicationContext.getBeanNamesForAnnotation(aClass);
	}

	public boolean containsBean(String name) {
		return wrappedApplicationContext.containsBean(name);
	}

	public boolean isSingleton(String name)
			throws NoSuchBeanDefinitionException {
		return wrappedApplicationContext.isSingleton(name);
	}

	public boolean isPrototype(String name)
			throws NoSuchBeanDefinitionException {
		return wrappedApplicationContext.isPrototype(name);
	}

	public boolean isTypeMatch(String s, ResolvableType resolvableType) throws NoSuchBeanDefinitionException {
		return wrappedApplicationContext.isTypeMatch(s, resolvableType);
	}

	public Map<String, Object> getBeansWithAnnotation(
			Class<? extends Annotation> annotationType) throws BeansException {
		return wrappedApplicationContext.getBeansWithAnnotation(annotationType);
	}

	public boolean isTypeMatch(String name, Class targetType)
			throws NoSuchBeanDefinitionException {
		return wrappedApplicationContext.isTypeMatch(name, targetType);
	}

	public <A extends Annotation> A findAnnotationOnBean(String beanName,
			Class<A> annotationType) {
		return wrappedApplicationContext.findAnnotationOnBean(beanName,
				annotationType);
	}

	public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
		return wrappedApplicationContext.getType(name);
	}

	public String[] getAliases(String name) {
		return wrappedApplicationContext.getAliases(name);
	}


	public Environment getEnvironment() {
		return wrappedApplicationContext.getEnvironment();
	}
}
