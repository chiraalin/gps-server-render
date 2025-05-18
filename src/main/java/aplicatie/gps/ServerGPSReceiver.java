package aplicatie.gps;

import bazadate.BazaDate;
import modele.Traseu;
import modele.TraseuDAO;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

public class ServerGPSReceiver {

    private static boolean serverPornit = false;

    public static void startServer() {
        if (serverPornit) {
            System.out.println("ℹ️ Serverul GPS este deja pornit.");
            return;
        }

        try {
            int port = 8080;
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/gps", new GpsHandler());
            server.setExecutor(null);
            server.start();
            serverPornit = true;
            System.out.println("✅ Server GPS pornit pe portul " + port);
        } catch (IOException e) {
            System.err.println("❌ Eroare la pornirea serverului GPS: " + e.getMessage());
        }
    }

    public static class GpsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                sendResponse(exchange, 405, "Doar cereri GET sunt acceptate.");
                return;
            }

            String query = exchange.getRequestURI().getQuery();
            if (query == null || !query.contains("imei=")) {
                sendResponse(exchange, 400, "Parametri lipsă.");
                return;
            }

            Map<String, String> params = parseQuery(query);
            String imei = params.get("imei");

            try {
                double lat = Double.parseDouble(params.getOrDefault("lat", "0"));
                double lon = Double.parseDouble(params.getOrDefault("lon", "0"));
                LocalDateTime dataOra = LocalDateTime.parse(params.get("dataOra"));

                try (Connection conn = BazaDate.conectare()) {
                    int idMasina = -1;
                    String sql = "SELECT id FROM Masina WHERE imei = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, imei);
                        ResultSet rs = stmt.executeQuery();
                        if (rs.next()) {
                            idMasina = rs.getInt("id");
                        }
                    }

                    if (idMasina == -1) {
                        sendResponse(exchange, 404, "Nu s-a găsit nicio mașină cu IMEI: " + imei);
                        return;
                    }

                    TraseuDAO dao = new TraseuDAO(conn);
                    dao.adaugaTraseu(new Traseu(idMasina, dataOra, lat, lon));

                    sendResponse(exchange, 200, "✅ Coordonate GPS salvate cu succes.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(exchange, 500, "❌ Eroare server: " + e.getMessage());
            }
        }

        private Map<String, String> parseQuery(String query) {
            Map<String, String> map = new HashMap<>();
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] kv = pair.split("=");
                if (kv.length == 2) {
                    map.put(kv[0], kv[1]);
                }
            }
            return map;
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            exchange.sendResponseHeaders(statusCode, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    // 🔹 Punctul de intrare pentru Render
    public static void main(String[] args) {
        startServer();
    }
}
