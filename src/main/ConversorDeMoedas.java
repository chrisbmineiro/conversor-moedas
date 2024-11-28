package main;

import services.Conversao;
import services.TaxaCambioService;

public class ConversorDeMoedas {
    public static void main(String[] args) {
        // Iniciando o serviço de taxa de câmbio e a lógica de conversão
        TaxaCambioService taxaCambioService = new TaxaCambioService();
        Conversao conversao = new Conversao(taxaCambioService);
        Menu menu = new Menu(conversao);

        // Exibindo o menu
        menu.exibir();
    }
}
