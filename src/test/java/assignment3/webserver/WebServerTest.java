package assignment3.webserver;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.io.Files;
import com.sun.net.httpserver.HttpExchange;

import assignment3.webserver.handler.WebServerHandler;

public class WebServerTest {
    private WebServerHandler handler;
    private OutputStream outputStream;

    @Mock
    private HttpExchange exchange;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        handler = new WebServerHandler("src/test/data/webroot/");

        outputStream = new ByteArrayOutputStream();
        when(exchange.getResponseBody()).thenReturn(outputStream);
    }

    @Test
    public void test_FileRequest() throws Exception {
        when(exchange.getRequestURI()).thenReturn(new URI("http://127.0.0.1/"));

        handler.handle(exchange);

        verify(exchange).getResponseBody();

        assertEquals(outputStream.toString(),
                Files.asCharSource(new File("src/test/data/webroot/main.html"), WebServerManager.UTF8).read());
    }

    @Test
    public void test_getVisualisation() {
    }
}
