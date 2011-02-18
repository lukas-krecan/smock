package net.javacrumbs.smock.common;



import static net.javacrumbs.smock.common.SmockCommon.fromResource;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.w3c.dom.Element;


public class DeserializerTest {
	private Deserializer deserializer = new Deserializer();
	
	@Test
	public void testDom() throws Exception
	{
		Element element = deserializer.deserialize(fromResource("xml/request1.xml"), Element.class);
		assertNotNull(element);
	}
}
