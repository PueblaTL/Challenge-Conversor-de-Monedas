import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsultaConversion {
    private final Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public String buscaConversion(String monedaBase, String monedaObjetivo, double cantidad) {
        try {
            String url = String.format(
                    "https://v6.exchangerate-api.com/v6/e5dfeba016f4ad8e3723f607/pair/%s/%s/%.0f",
                    monedaBase.toUpperCase(),
                    monedaObjetivo.toUpperCase(),
                    cantidad
            );

            URI direccion = URI.create(url);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(direccion)
                    .GET()
                    .build();

            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Error en la API: Código de estado " + response.statusCode() +
                        "\nURL solicitada: " + url);
            }


            var json = response.body();
            JsonElement jsonElement = JsonParser.parseString(json);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (!jsonObject.has("conversion_result")) {
                throw new RuntimeException("La respuesta de la API no contiene el resultado de la conversión");
            }

            return jsonObject.get("conversion_result").getAsString();

        } catch (IOException e) {
            throw new RuntimeException("Error de conexión: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("La operación fue interrumpida");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error en los códigos de moneda: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado: " + e.getMessage());
        }
    }
}