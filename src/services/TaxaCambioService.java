package services;

import com.google.gson.Gson;
import models.APIResponse;
import models.Moeda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class TaxaCambioService {
    private static final Properties props = new Properties();

    static {
        try {
            props.load(Files.newBufferedReader(Paths.get(".env")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final String API_KEY = props.getProperty("API_KEY");
    public static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    // Função para buscar a taxa de câmbio de uma moeda base para uma moeda de destino
    public double obterTaxaDeCambio(Moeda base, Moeda destino) {
        try {
            URL url = new URL(API_URL + base.name());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Gson gson = new Gson();
            APIResponse apiResponse = gson.fromJson(response.toString(), APIResponse.class);

            if (apiResponse.conversion_rates().containsKey(destino.name())) {
                return apiResponse.conversion_rates().get(destino.name());
            } else {
                System.out.println("Taxa de câmbio não encontrada.");
                return -1;
            }
        } catch (Exception e) {
            System.out.println("Erro ao consultar API: " + e.getMessage());
            return -1;
        }
    }
}
