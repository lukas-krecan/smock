package net.javacrumbs.smock.http.server.servlet;

import java.util.Map;
import java.util.WeakHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

/**
 * Utility class that helps with creating servlets. Servlets are cached based on applicationContext.
 * @author Lukas Krecan
 */
public abstract class ServletUtils {
	private static final WeakHashMap<ApplicationContext, HttpServlet> servletCache = new WeakHashMap<ApplicationContext, HttpServlet>();
	
	private ServletUtils() {
	}

	/**
	 * Creates and configures servlet. Adds applicationContext to servletContext
	 * @param servletClass
	 * @param applicationContext
	 * @param basePath
	 * @param initParameters
	 * @return
	 */
	public static HttpServlet createServlet(Class<? extends HttpServlet> servletClass, ApplicationContext applicationContext, String basePath, Map<String, String> initParameters)
	{
		if (servletCache.containsKey(applicationContext))
		{
			return servletCache.get(applicationContext);
		}
		Assert.notNull(servletClass, "servletClass has to be specified.");
		HttpServlet servlet = BeanUtils.instantiate(servletClass);
		MockServletContext context = new MockServletContext(basePath, applicationContext);
		MockServletConfig config = new MockServletConfig(context);
		if (applicationContext!=null)
		{
			config.getServletContext().setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, new ApplicationContextWrapper(applicationContext, config.getServletContext()));
		}
        if (initParameters!=null)
        {
        	for (Map.Entry<String, String> param: initParameters.entrySet())
        	{
        		config.addInitParameter(param.getKey(), param.getValue());
        	}
        }
        try {
			servlet.init(config);
		} catch (ServletException e) {
			throw new IllegalStateException("Error when creating servlet "+servletClass.getName(),e);
		}
		if (applicationContext!=null)
		{
			servletCache.put(applicationContext, servlet);
		}
        return servlet;
	}
	
	/**
	 * Clears the cache
	 */
	public static void clearCache()
	{
		servletCache.clear();
	}
}
