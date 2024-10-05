package principal;

import modelos.Conversao;
import modelos.Moeda;
import modelos.TaxaCambioService;

import java.util.Scanner;

public class ConversorDeMoedas {

    private final Conversao conversao;

    public ConversorDeMoedas(Conversao conversao) {
        this.conversao = conversao;
    }

    // Exibe o menu e captura a escolha do usuário
    public void exibirMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("****************************************");
            System.out.println("Seja bem-vindo/a ao Conversor de Moedas");
            System.out.println("1) Dólar => Peso Argentino");
            System.out.println("2) Peso Argentino => Dólar");
            System.out.println("3) Dólar => Real Brasileiro");
            System.out.println("4) Real Brasileiro => Dólar");
            System.out.println("5) Dólar => Peso Colombiano");
            System.out.println("6) Peso Colombiano => Dólar");
            System.out.println("7) Sair");
            System.out.println("****************************************");
            System.out.print("Escolha uma opção válida: ");

            int opcao = scanner.nextInt();
            Moeda base = null, destino = null;

            switch (opcao) {
                case 1:
                    base = Moeda.USD;
                    destino = Moeda.ARS;
                    break;
                case 2:
                    base = Moeda.ARS;
                    destino = Moeda.USD;
                    break;
                case 3:
                    base = Moeda.USD;
                    destino = Moeda.BRL;
                    break;
                case 4:
                    base = Moeda.BRL;
                    destino = Moeda.USD;
                    break;
                case 5:
                    base = Moeda.USD;
                    destino = Moeda.COP;
                    break;
                case 6:
                    base = Moeda.COP;
                    destino = Moeda.USD;
                    break;
                case 7:
                    System.out.println("Saindo do programa. Até logo!");
                    return;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
                    continue;
            }

            System.out.print("Digite o valor em " + base + ": ");
            double valor = scanner.nextDouble();
            double valorConvertido = conversao.converter(base, destino, valor);

            if (valorConvertido != -1) {
                System.out.printf("\n****************************************\n%.2f %s é equivalente a %.2f %s.\n****************************************\n\n", valor, base.name(), valorConvertido, destino.name());
            }
        }
    }

    public static void main(String[] args) {
        // Iniciando o serviço de taxa de câmbio e a lógica de conversão
        TaxaCambioService taxaCambioService = new TaxaCambioService();
        Conversao conversao = new Conversao(taxaCambioService);
        ConversorDeMoedas conversorDeMoedas = new ConversorDeMoedas(conversao);

        // Exibindo o menu
        conversorDeMoedas.exibirMenu();
    }
}
