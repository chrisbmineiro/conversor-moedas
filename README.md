
# Conversor de Moedas - Java

Este é um projeto de conversão de moedas desenvolvido em **Java** que utiliza a API **ExchangeRate** para obter as taxas de câmbio em tempo real. O objetivo do projeto é permitir a conversão entre diversas moedas de forma dinâmica, consultando os valores de conversão a partir de uma API externa.

## Funcionalidades

- Conversão de diferentes moedas: Dólar (USD), Peso Argentino (ARS), Real Brasileiro (BRL) e Peso Colombiano (COP).
- Consulta em tempo real das taxas de câmbio utilizando a API **ExchangeRate**.
- Menu interativo para selecionar as opções de conversão.
- Sistema modular utilizando boas práticas de **Orientação a Objetos** e **Java Records**.

## Pré-requisitos

- **Java 17** ou superior
- **Gson** como dependências
- Acesso à **API ExchangeRate**

## Como Funciona

1. O sistema apresenta um menu de opções para o usuário selecionar a conversão de moeda.
2. O valor a ser convertido é inserido pelo usuário.
3. A aplicação consulta a API da **ExchangeRate** para obter a taxa de câmbio atual entre as moedas.
4. O valor convertido é exibido ao usuário.

## Configuração do Projeto

### Dependências

O projeto utiliza o arquivos jar para gerenciamento de dependências. Certifique-se de que o arquivo jar `gson-2.11.0` que contem a biblioteca `Gson` esteja no Project Structure -> Modules -> Dependencies do projeto:

### API Key

Você precisará de uma **API Key** da **ExchangeRate**. Essa chave pode ser obtida criando uma conta no [ExchangeRate API](https://www.exchangerate-api.com/).

Após obter a chave da API, substitua-a no código do projeto:

```java
private static final String API_URL = "https://v6.exchangerate-api.com/v6/YOUR_API_KEY/latest/";
```

Substitua `"YOUR_API_KEY"` pela sua chave de acesso.

## Estrutura do Código

### 1. `APIResponse.java`

Esse `record` é responsável por mapear a resposta JSON da API:

```java
import java.util.Map;

public record APIResponse(String base_code, Map<String, Double> conversion_rates) {
}
```

A API retorna as taxas de câmbio no campo `conversion_rates`, que é um `Map` com a moeda como chave e a taxa de câmbio como valor.

### 2. `Moeda.java`

Enum para representar as moedas suportadas no sistema, pode adicionar mais caso queira expandir:

```java
public enum Moeda {
    USD, ARS, BRL, COP
}
```

### 3. `TaxaCambioService.java`

Essa classe contém a lógica para consultar a API e obter a taxa de câmbio em tempo real. Utiliza a biblioteca **Gson** para fazer o parsing do JSON retornado pela API.

```java
package modelos;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TaxaCambioService {

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/4e4ccca09fe861dbb31eef1a/latest/";

    // Método para buscar a taxa de câmbio de uma moeda base para uma moeda de destino
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
```

### 4. `ConversorMoeda.java`

O ponto de entrada do programa, responsável por gerenciar o menu e interagir com o usuário. O usuário escolhe as moedas para conversão e insere o valor a ser convertido.

```java
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
```

## Como Executar

1. Clone este repositório.
2. Certifique-se de que você possui **Java 17** ou superior instalado.
3. Adicione sua chave da API da ExchangeRate no código (`TaxaCambioService`).
4. Compile e execute o programa

## Melhorias Futuras

- Adicionar suporte para mais moedas.
- Implementar uma interface gráfica (GUI) para melhorar a experiência do usuário.
- Adicionar testes unitários para validação de lógica e integração com a API.
