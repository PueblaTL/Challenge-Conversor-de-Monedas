import com.google.gson.JsonSyntaxException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        Scanner lectura = new Scanner(System.in);
        int opcionElegida = 0;

        ConsultaConversion consulta = new ConsultaConversion();
        CalcularMoneda calculos = new CalcularMoneda(consulta);
        GeneradorDeArchivos generador = new GeneradorDeArchivos();

        List<String> respuestas = new ArrayList<>();

        String menu = """
                ************************* 
                Bienvenidos Al conversor de monedas
                
                Ingresa la conversión que deseas realizar
                
                1. Dólar estadounidense a Peso Argentino
                2. Peso Argentino a Dólar
                3. Dólar estadounidense a Real Brasileño
                4. Real Brasileño a Dólar
                5. Dólar estadounidense a Yen Japonés
                6. Yen Japonés a Dólar estadounidense
                7. Convertir otra moneda
                8. Salir
                """;

        while (opcionElegida != 8) {
            try {
                System.out.println(menu);
                String input = lectura.nextLine();

                // Valida que la entrada sea un número
                try {
                    opcionElegida = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Error: Por favor ingrese un número del 1 al 8.");
                    continue;
                }

                // Obtiene la marca de tiempo actual
                LocalDateTime myDateObj = LocalDateTime.now();
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedDate = myDateObj.format(myFormatObj);

                // Valida rango de opciones
                if (opcionElegida < 1 || opcionElegida > 8) {
                    System.out.println("Error: Por favor ingrese un número del 1 al 8.");
                    continue;
                }

                try {
                    switch (opcionElegida) {
                        case 1:
                            calculos.almacenarValores("USD", "ARS");
                            respuestas.add(formattedDate + " - " + calculos.obtenerMensajeRespuesta());
                            break;
                        case 2:
                            calculos.almacenarValores("ARS", "USD");
                            respuestas.add(formattedDate + " - " + calculos.obtenerMensajeRespuesta());
                            break;
                        case 3:
                            calculos.almacenarValores("USD", "BRL");
                            respuestas.add(formattedDate + " - " + calculos.obtenerMensajeRespuesta());
                            break;
                        case 4:
                            calculos.almacenarValores("BRL", "USD");
                            respuestas.add(formattedDate + " - " + calculos.obtenerMensajeRespuesta());
                            break;
                        case 5:
                            calculos.almacenarValores("USD", "JPY");
                            respuestas.add(formattedDate + " - " + calculos.obtenerMensajeRespuesta());
                            break;
                        case 6:
                            calculos.almacenarValores("JPY", "USD");
                            respuestas.add(formattedDate + " - " + calculos.obtenerMensajeRespuesta());
                            break;
                        case 7:
                            calculos.almacenarValoresPersonalizados();
                            respuestas.add(formattedDate + " - " + calculos.obtenerMensajeRespuesta());
                            break;
                        case 8:
                            System.out.println("Saliendo...");
                            break;
                    }
                } catch (JsonSyntaxException e) {
                    System.out.println("Error: No se pudo procesar la respuesta de la API. Verifique su conexión a internet.");
                    System.out.println("Detalles: " + e.getMessage());
                } catch (NullPointerException e) {
                    System.out.println("Error: Los códigos de moneda ingresados no son válidos o no están soportados.");
                    System.out.println("Por favor, use códigos de moneda válidos de 3 letras (ejemplo: USD, EUR, GBP)");
                }
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
            }
        }

        try {
            generador.guardarJson(respuestas);
            System.out.println("Historial guardado exitosamente");
        } catch (Exception e) {
            System.out.println("Error al guardar el historial: " + e.getMessage());
        }

        System.out.println("Finalizando programa");
        lectura.close();
    }
}