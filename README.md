Smock
=====

Simplify your WS tests

Smock simplifies your SOAP Web Service tests. You can use it if you need to

  * [Extend](https://github.com/lukas-krecan/smock/wiki/Common-Features) existing [Spring WS 2.0](http://static.springsource.org/spring-ws/sites/2.0/) testing capabilities
  * Write WS [UnitTesting unit] or integration tests
  * Test Axis 2 client or server code
  * Test CXF client or server code
  * Test JAX-WS RI (Metro) client or server code

Although Smock builds on [Spring WS 2.0](http://static.springsource.org/spring-ws/sites/2.0/) testing framework it can be used to test other SOAP stacks in similar way. 

*I would really like to hear your feedback. If you have anything to tell me, please contact me on [smock-info@googlegroups.com](mailto:smock-info@googlegroups.com ) ([Google group](http://groups.google.com/group/smock-info))*

## Server test
If you want to test your WS server code, you can use following code


      @Test
      public void testCompare() throws Exception {
         client.sendRequest(withMessage("request1.xml")).andExpect(message("response1.xml"));
      }
      @Test
      public void testValidateResponse() throws Exception {
        client.sendRequest(withMessage("request1.xml")).andExpect(noFault()).andExpect(validPayload(resource("xsd/calc.xsd")));
      }


## Client tests
To test your client code you can do the following

      @Test
      public void testSimple()
      {
      	mockServer.expect(anything()).andRespond(withMessage("response1.xml"));
      	int result = calc.plus(1, 2);
      	assertEquals(3, result);
      }
      
      @Test
      public void testVerifyRequest()
      {
      	mockServer.expect(message("request1.xml")).andRespond(withMessage("response1.xml"));
      		
      	int result = calc.plus(1, 2);
      	assertEquals(3, result);
      }

You can start by reading [CommonFeatures](https://github.com/lukas-krecan/smock/wiki/Common-Features) and then you can follow to specific setting for you SOAP stack. 


