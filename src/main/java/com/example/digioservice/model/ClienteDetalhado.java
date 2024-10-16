package com.example.digioservice.model;

import java.util.List;

public class ClienteDetalhado {
    private String nomeCliente;
    private String cpfCliente;
    private List<CompraDetalhada> compras;
    private double valorTotalCompras;

    public ClienteDetalhado(String nomeCliente, String cpfCliente, List<CompraDetalhada> compras, double valorTotalCompras) {
        this.nomeCliente = nomeCliente;
        this.cpfCliente = cpfCliente;
        this.compras = compras;
        this.valorTotalCompras = valorTotalCompras;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getCpfCliente() {
        return cpfCliente;
    }

    public void setCpfCliente(String cpfCliente) {
        this.cpfCliente = cpfCliente;
    }

    public List<CompraDetalhada> getCompras() {
        return compras;
    }

    public void setCompras(List<CompraDetalhada> compras) {
        this.compras = compras;
    }

    public double getValorTotalCompras() {
        return valorTotalCompras;
    }

    public void setValorTotalCompras(double valorTotalCompras) {
        this.valorTotalCompras = valorTotalCompras;
    }
}

