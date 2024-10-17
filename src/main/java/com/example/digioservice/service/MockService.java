package com.example.digioservice.service;

import com.example.digioservice.model.Cliente;
import com.example.digioservice.model.Produto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

@Service
public class MockService {
    private final String produtosUrl = "https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/produtos-mnboX5IPl6VgG390FECTKqHsD9SkLS.json";
    private final String clientesUrl = "https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/clientes-Vz1U6aR3GTsjb3W8BRJhcNKmA81pVh.json";
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Produto> getProdutos() {
        Produto[] produtos = restTemplate.getForObject(produtosUrl, Produto[].class);
        return Arrays.asList(produtos);
    }

    public List<Cliente> getClientes() {
        Cliente[] clientes = restTemplate.getForObject(clientesUrl, Cliente[].class);
        return Arrays.asList(clientes);
    }
}
