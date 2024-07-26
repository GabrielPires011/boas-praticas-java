package br.com.alura.domain;

public class Pet {
    private Long id;
    private String tipo;
    private String name;
    private String raca;
    private Integer idade;
    private String cor;
    private Float peso;

    public Pet(String tipo, String name, String raca, Integer idade, String cor, Float peso) {
        this.tipo = tipo;
        this.name = name;
        this.raca = raca;
        this.idade = idade;
        this.cor = cor;
        this.peso = peso;
    }
}
