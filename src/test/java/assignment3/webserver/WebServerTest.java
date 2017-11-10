package assignment3.webserver;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URI;

import javax.json.Json;
import javax.json.JsonArrayBuilder;

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
    @Mock
    private WebServerManager manager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        handler = new WebServerHandler("src/test/data/webroot/", manager);

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
    public void test_GET_RequestKeyDoesNotExist() throws Exception {
        when(exchange.getRequestURI()).thenReturn(new URI("http://127.0.0.1/?requestType=keydoesnotexist"));

        handler.handle(exchange);

        verify(exchange).getResponseBody();

        assertEquals(outputStream.toString(),
                Files.asCharSource(new File("src/test/data/webroot/main.html"), WebServerManager.UTF8).read());
    }

    @Test
    public void test_GET_RequestKeyExists() throws Exception {
        when(exchange.getRequestURI()).thenReturn(new URI("http://127.0.0.1/?requestType=populateForm&formElement=typeofgraph"));

        handler.handle(exchange);

        verify(exchange).getResponseBody();

        JsonArrayBuilder builder = Json.createArrayBuilder();
        WebServerConstants.TYPES_OF_GRAPH.forEach(builder::add);

        assertEquals(outputStream.toString(), builder.build().toString());
    }
}
