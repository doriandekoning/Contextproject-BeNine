package test.java.benine.ipcameracontrol;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.verify.VerificationTimes;

/**
 * Test class to test the IP Camera class
 * 
 * @author Bryan
 */
public class IPCameraTest {
	
	@Rule
	public MockServerRule mockServerRule = new MockServerRule(this, 9000);
	
	private MockServerClient mockServerClient;
	
	@Before
	public void setUp() throws Exception {
		mockServerClient.when(HttpRequest.request("/test"))
		.respond(HttpResponse.response().withBody("true"));
	}

	@Test
	public void test() throws MalformedURLException, IOException {
		new URL("http://127.0.0.1:9000/test").openStream();
		mockServerClient.verify(HttpRequest.request("/test"),
				VerificationTimes.once());
	}

}
