package net.javacrumbs.calc;

import net.javacrumbs.calc.model.PlusRequest;
import net.javacrumbs.calc.model.PlusResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.WebServiceOperations;

public class Calc {
	@Autowired
	private WebServiceOperations wsTemplate;
	
	public int plus(int a, int b)
	{
		PlusRequest request = new PlusRequest();
		request.setA(a);
		request.setB(b);
		PlusResponse result = (PlusResponse) wsTemplate.marshalSendAndReceive(request);
		return result.getResult();
	}
}
