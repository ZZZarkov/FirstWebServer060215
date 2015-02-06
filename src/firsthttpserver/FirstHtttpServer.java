package firsthttpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Lars Mortensen
 */
public class FirstHtttpServer
{
    static int port = 8080;
    static String ip = "127.0.0.1";
    
    public static void main(String[] args) throws Exception
    {
        if (args.length == 2)
        {
            port = Integer.parseInt(args[0]);
            ip = args[0];
        }
        HttpServer server = HttpServer.create(new InetSocketAddress(ip, port), 0);
        server.createContext("/welcome", new RequestHandler());
        server.createContext("/headers", new HeadersHandler());
        server.setExecutor(null); // Use the default executor
        server.start();
        System.out.println("Server started, listening on port: " + port);
    }
    
    static class RequestHandler implements HttpHandler
    {
        @Override
        public void handle(HttpExchange he) throws IOException
        {
            String response = "Welcome to my very first almost home made Web Server :-)";
            StringBuilder sb = new StringBuilder();
            sb.append("<!DOCTYPE html>\n");
            sb.append("<html>\n");
            sb.append("<head>\n");
            sb.append("<title>My fancy Web Site</title>\n");
            sb.append("<meta charset='UTF-8'>\n");
            sb.append("</head>\n");
            sb.append("<body>\n");
            sb.append("<h2>Welcome to my very first home made Web Server :-)</h2>\n");
            sb.append("</body>\n");
            sb.append("</html>\n");
            response = sb.toString();
            Headers h = he.getResponseHeaders();
            h.add("Content-Type", "text/html");
            he.sendResponseHeaders(200, response.length());
            try (PrintWriter pw = new PrintWriter(he.getResponseBody()))
            {
                pw.print(response); //What happens if we use a println instead of print --> Explain
            }
        }
    }
    
    static class HeadersHandler implements HttpHandler
    {
        @Override
        public void handle(HttpExchange he) throws IOException
        {
            StringBuilder grh = new StringBuilder();
            Headers h = he.getRequestHeaders();
            
            grh.append("<!DOCTYPE html>\n");
            grh.append("<html>\n");
            grh.append("<head>\n");
            grh.append("<title>My fancy Web Site</title>\n");
            grh.append("<meta charset='UTF-8'>\n");
            grh.append("</head>\n");
            grh.append("<body>\n");
            grh.append("<table border = \"1\">\n");
            grh.append("<caption>Table Caption</caption>\n");
            grh.append("<tr> <th>Header</th> <th>Value</th> </tr>\n");
            
            for (Map.Entry<String, List<String>> entry : h.entrySet())
            {
                String key = entry.getKey();
                List<String> value = entry.getValue();
                String add = "<tr> <td>" + key + "</td> <td>" + value + "</td> </tr>\n";
                grh.append(add);
            }
            
            grh.append("</table>\n");
            grh.append("</body>\n");
            grh.append("</html>\n");
            String response = grh.toString();
            
            h = he.getResponseHeaders();
            h.add("Content-Type", "text/html");
            he.sendResponseHeaders(200, response.length());
            
            try (PrintWriter pw = new PrintWriter(he.getResponseBody()))
            {
                pw.print(response);
            }
        }
    }
}
