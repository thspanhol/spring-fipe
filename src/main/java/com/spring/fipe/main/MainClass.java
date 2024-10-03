package com.spring.fipe.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.fipe.model.CodeName;
import com.spring.fipe.model.Veiculo;
import com.spring.fipe.service.ConsumoApi;
import com.spring.fipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MainClass {

    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados converteDados = new ConverteDados();
    private Scanner scanner = new Scanner(System.in);

    public void exibeMenu() {

        var stringVeiculo = "";

        while (stringVeiculo.isEmpty()) {
            System.out.println("""
                **************************
                Digite sua opção de veículo:
                1- Carros
                2- Motos
                3- Caminhões
                **************************
                """);
            var opcaoVeiculo = scanner.nextInt();

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

        var link ="https://fipe.parallelum.com.br/api/v2/" + stringVeiculo + "/brands";
        var json = consumoApi.obterDados(link);
        //System.out.println(json);
        //System.out.println(json.length());
        var dados = converteDados.converteLista(json, CodeName.class);
        //System.out.println(dados);
        dados.forEach(d -> System.out.println(d.name() + " - " + d.code()));
        System.out.println("Informe o código da marca desejada:");
        var codigoMarca = scanner.nextInt();

        var link2 ="https://fipe.parallelum.com.br/api/v2/" + stringVeiculo + "/brands/" + codigoMarca + "/models";
        var json2 = consumoApi.obterDados(link2);
        var dados2 = converteDados.converteLista(json2, CodeName.class);
        dados2.forEach(d -> System.out.println(d.name() + " - " + d.code()));

        System.out.println("Informe o código do modelo desejado:");
        var codigoModelo = scanner.nextInt();

        var dados3 = dados2.stream().filter(d -> d.name().toLowerCase().contains("king"));
        //dados3.forEach(System.out::println);

        var link4 ="https://fipe.parallelum.com.br/api/v2/" + stringVeiculo + "/brands/" + codigoMarca + "/models/" + codigoModelo + "/years";
        var json4 = consumoApi.obterDados(link4);
        var dados4 = converteDados.converteLista(json4, CodeName.class);
        System.out.println(dados4);

        var link5 ="https://fipe.parallelum.com.br/api/v2/" + stringVeiculo + "/brands/" + codigoMarca + "/models/" + codigoModelo + "/years/";
        var dados5 = dados4.stream().map(d ->  converteDados.converteDados(consumoApi.obterDados(link5 + d.code()), Veiculo.class));
        
        dados5.forEach(d -> System.out.println(d.model() + " - " + d.modelYear() + " - " + d.price()));



    }
}
