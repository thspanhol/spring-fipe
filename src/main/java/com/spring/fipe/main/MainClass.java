package com.spring.fipe.main;

import com.spring.fipe.model.CodeName;
import com.spring.fipe.model.Veiculo;
import com.spring.fipe.service.ConsumoApi;
import com.spring.fipe.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class MainClass {

    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados converteDados = new ConverteDados();
    private Scanner scanner = new Scanner(System.in);

    public void exibeMenu() {

        int opcaoVeiculo = 0;
        List<Integer> listaCodigoVeiculos = Arrays.asList(1, 2, 3);
        var stringVeiculo = "";
        List<String> listaCodigoMarcas;
        long codigoMarca = 0;
        List<String> listaCodigoModelos;
        long codigoModelo = 0;

        while (!listaCodigoVeiculos.contains(opcaoVeiculo)) {
            System.out.println("""
                **************************
                Digite sua opção de veículo:
                1- Carros
                2- Motos
                3- Caminhões
                **************************
                """);
            opcaoVeiculo = scanner.nextInt();

            switch (opcaoVeiculo) {
                case 1:
                    stringVeiculo = "cars";
                    break;
                case 2:
                    stringVeiculo = "motorcycles";
                    break;
                case 3:
                    stringVeiculo = "trucks";
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }

        var link = "https://fipe.parallelum.com.br/api/v2/" + stringVeiculo + "/brands";
        var json = consumoApi.obterDados(link);
        var dados = converteDados.converteLista(json, CodeName.class);

        listaCodigoMarcas = dados.stream().map(d -> d.code()).collect(Collectors.toList());

        while (!listaCodigoMarcas.contains(String.valueOf(codigoMarca))) {
            dados.forEach(d -> System.out.println(d.name() + " - Código: " + d.code()));
            System.out.println("Informe o código da marca desejada:");
            codigoMarca = scanner.nextLong();
            if (!listaCodigoMarcas.contains(String.valueOf(codigoMarca))) System.out.println("Código de marca não encontrado, tente novamente.");
        }

        link = "https://fipe.parallelum.com.br/api/v2/" + stringVeiculo + "/brands/" + codigoMarca + "/models";
        json = consumoApi.obterDados(link);
        dados = converteDados.converteLista(json, CodeName.class);
        listaCodigoModelos = dados.stream().map(d -> d.code()).collect(Collectors.toList());

        while (!listaCodigoModelos.contains(String.valueOf(codigoModelo))) {
            dados.forEach(d -> System.out.println(d.name() + " - Código: " + d.code()));
            System.out.println("Informe o código do modelo desejado:");
            codigoModelo = scanner.nextLong();
            if (!listaCodigoModelos.contains(String.valueOf(codigoModelo))) System.out.println("Código de modelo não encontrado, tente novamente.");
        }

        link = "https://fipe.parallelum.com.br/api/v2/" + stringVeiculo + "/brands/" + codigoMarca + "/models/" + codigoModelo + "/years";
        json = consumoApi.obterDados(link);
        dados = converteDados.converteLista(json, CodeName.class);

        var finalLink = "https://fipe.parallelum.com.br/api/v2/" + stringVeiculo + "/brands/" + codigoMarca + "/models/" + codigoModelo + "/years/";
        var finalDados = dados.stream()
                .map(d ->  converteDados.converteDados(consumoApi.obterDados(finalLink + d.code()), Veiculo.class))
                .sorted(Comparator.comparing(Veiculo::modelYear));

        System.out.println("Valor de mercado atual:");
        finalDados.forEach(d -> System.out.println(d.model() + " - Ano: " + d.modelYear() + " - Preço: " + d.price()));
    }
}
