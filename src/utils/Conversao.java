package utils;

import models.Moeda;
import services.TaxaCambioService;

public class Conversao {

    private final TaxaCambioService taxaCambioService;

    // Construtor que recebe o serviço de taxa de câmbio
    public Conversao(TaxaCambioService taxaCambioService) {
        this.taxaCambioService = taxaCambioService;
    }

    // Função que realiza a conversão de moeda
    public double converter(Moeda base, Moeda destino, double valor) {
        double taxa = taxaCambioService.obterTaxaDeCambio(base, destino);
        if (taxa != -1) {
            return valor * taxa;
        }
        return -1;
    }
}
