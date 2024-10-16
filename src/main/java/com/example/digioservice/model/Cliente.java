package com.example.digioservice.model;

import lombok.Data;
import java.util.List;

@Data
public class Cliente {
    private String nome;
    private String cpf;
    private List<Compra> compras;
}