package com.example.digioservice.model;

import lombok.Data;

@Data
public class Compra {
    private Produto produto;
    private int codigo;
    private int quantidade;
}