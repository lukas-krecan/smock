package net.javacrumbs.calc.server;

import javax.jws.WebService;

import net.javacrumbs.calc.model.PlusRequest;
import net.javacrumbs.calc.model.PlusResponse;

@WebService(serviceName="CalculatorService", targetNamespace="http://javacrumbs.net/calc")
public class CalcEndpoint {

	
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
