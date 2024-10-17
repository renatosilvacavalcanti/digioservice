package com.example.digioservice.controller;

import com.example.digioservice.model.Cliente;
import com.example.digioservice.model.ClienteDetalhado;
import com.example.digioservice.model.CompraDetalhada;
import com.example.digioservice.model.Produto;
import com.example.digioservice.service.MockService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Comparator;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/compras")
public class CompraController {

    private final MockService mockService;

    public CompraController(MockService mockService) {
        this.mockService = mockService;
    }

    // Endpoint 1: Lista de compras ordenadas por valor crescente
    @GetMapping
    public List<ClienteDetalhado> getCompras() {
        List<Produto> produtos = mockService.getProdutos();
        Map<Integer, Produto> produtoMap = produtos.stream().collect(Collectors.toMap(Produto::getCodigo, produto -> produto));

        List<ClienteDetalhado> clientesDetalhados = mockService.getClientes().stream().map(cliente -> {
            double[] valorTotalCompras = {0.0};  // Usando array para contornar o problema de variável final
            List<CompraDetalhada> comprasDetalhadas = cliente.getCompras().stream().map(compra -> {
                Produto produto = produtoMap.get(compra.getCodigo());
                double precoProduto = produto != null ? produto.getPreco() : 0;
                double valorTotal = precoProduto * compra.getQuantidade();
                valorTotalCompras[0] += valorTotal;  // Somando ao valor total

                return new CompraDetalhada(
                		compra.getCodigo(),
                        produto != null ? produto.getTipo_vinho() : "Produto não encontrado",
                        precoProduto,
                        compra.getQuantidade(),
                        valorTotal
                );
            }).collect(Collectors.toList());

            return new ClienteDetalhado(cliente.getNome(), cliente.getCpf(), comprasDetalhadas, valorTotalCompras[0]);
        }).sorted(Comparator.comparingDouble(ClienteDetalhado::getValorTotalCompras)).collect(Collectors.toList());
        
        return clientesDetalhados;
    }

    // Endpoint 2: Maior compra do ano informado
    @GetMapping("/maior-compra/{ano}")
    public ClienteDetalhado getMaiorCompraPorAno(@PathVariable int ano) {
        // Obtenha os produtos e mapeie-os por código para facilitar a busca
        List<Produto> produtos = mockService.getProdutos();
        Map<Integer, Produto> produtoMap = produtos.stream().collect(Collectors.toMap(Produto::getCodigo, produto -> produto));

        // Busque os clientes e crie uma lista de clientes detalhados com todas as suas compras
        return mockService.getClientes().stream()
            .map(cliente -> {
                double[] valorTotalCompras = {0.0};  // Usando array para contornar o problema de variável final
                List<CompraDetalhada> comprasDetalhadas = cliente.getCompras().stream()
                    .filter(compra -> produtoMap.containsKey(compra.getCodigo()) && produtoMap.get(compra.getCodigo()).getAno_compra() == ano) // Filtrar pelo ano
                    .map(compra -> {
                        Produto produto = produtoMap.get(compra.getCodigo());
                        double precoProduto = produto != null ? produto.getPreco() : 0;
                        double valorTotal = precoProduto * compra.getQuantidade();
                        valorTotalCompras[0] += valorTotal;  // Somando ao valor total

                        return new CompraDetalhada(
                        	compra.getCodigo(),
                            produto != null ? produto.getTipo_vinho() : "Produto não encontrado",
                            precoProduto,
                            compra.getQuantidade(),
                            valorTotal
                        );
                    })
                    .collect(Collectors.toList());

                return new ClienteDetalhado(cliente.getNome(), cliente.getCpf(), comprasDetalhadas, valorTotalCompras[0]);
            })
            // Comparar os valores totais das compras para pegar o cliente com a maior compra
            .max(Comparator.comparingDouble(ClienteDetalhado::getValorTotalCompras))
            // Retornar uma exceção se não houver compras no ano informado
            .orElseThrow(() -> new RuntimeException("Nenhuma compra encontrada para o ano informado."));
    }

    // Endpoint 3: Top 3 clientes mais fiéis
    @GetMapping("/clientes-fieis")
    public List<ClienteDetalhado> getClientesFieis() {
        // Obtenha os produtos e mapeie-os por código para facilitar a busca
        List<Produto> produtos = mockService.getProdutos();
        Map<Integer, Produto> produtoMap = produtos.stream().collect(Collectors.toMap(Produto::getCodigo, produto -> produto));

        // Busque os clientes e crie uma lista de clientes detalhados com todas as suas compras
        return mockService.getClientes().stream()
            .map(cliente -> {
                double[] valorTotalCompras = {0.0};  // Usando array para contornar o problema de variável final
                List<CompraDetalhada> comprasDetalhadas = cliente.getCompras().stream()
                    .map(compra -> {
                        Produto produto = produtoMap.get(compra.getCodigo());
                        double precoProduto = produto != null ? produto.getPreco() : 0;
                        double valorTotal = precoProduto * compra.getQuantidade();
                        valorTotalCompras[0] += valorTotal;  // Somando ao valor total

                        return new CompraDetalhada(
                        	compra.getCodigo(),
                            produto != null ? produto.getTipo_vinho() : "Produto não encontrado",
                            precoProduto,
                            compra.getQuantidade(),
                            valorTotal
                        );
                    })
                    .collect(Collectors.toList());

                return new ClienteDetalhado(cliente.getNome(), cliente.getCpf(), comprasDetalhadas, valorTotalCompras[0]);
            })
            // Ordenar pela maior quantidade de compras e pelo maior valor total
            .sorted(Comparator.comparingDouble(ClienteDetalhado::getValorTotalCompras).reversed()) // Ordem decrescente pelo valor total
            .limit(3) // Pegamos apenas os 3 primeiros clientes mais fiéis
            .collect(Collectors.toList());
    }

    // Endpoint 4: Recomendação de vinho por cliente
    @GetMapping("/recomendacao/{clienteCpf}/{tipo}")
    public Produto getRecomendacaoPorCliente(@PathVariable String clienteCpf, @PathVariable String tipo) {
        // Buscar o cliente pelo CPF
        Cliente cliente = mockService.getClientes().stream()
            .filter(c -> c.getCpf().equals(clienteCpf))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Buscar todos os produtos e criar um mapa de produtos
        List<Produto> produtos = mockService.getProdutos();
        Map<Integer, Produto> produtoMap = produtos.stream().collect(Collectors.toMap(Produto::getCodigo, produto -> produto));

        // Agrupar as compras do cliente por tipo de vinho e contar quantos de cada tipo ele comprou
        Map<String, Long> tipoVinhoCount = cliente.getCompras().stream()
            .map(compra -> produtoMap.get(compra.getCodigo())) // Mapear para o produto
            .filter(Objects::nonNull) // Filtrar produtos válidos
            .filter(produto -> produto.getTipo_vinho().equalsIgnoreCase(tipo)) // Filtrar pelo tipo de vinho informado
            .collect(Collectors.groupingBy(Produto::getTipo_vinho, Collectors.counting())); // Agrupar e contar

        // Encontrar o tipo de vinho mais comprado pelo cliente
        return tipoVinhoCount.entrySet().stream()
            .max(Map.Entry.comparingByValue()) // Obter o tipo mais comprado
            .map(Map.Entry::getKey)
            .map(tipoVinhoMaisComprado -> produtos.stream()
                .filter(produto -> produto.getTipo_vinho().equalsIgnoreCase(tipoVinhoMaisComprado))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Recomendação de vinho não encontrada")))
            .orElseThrow(() -> new RuntimeException("Nenhuma compra do tipo informado foi encontrada para o cliente."));
    }
}
