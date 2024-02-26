package br.com.ronistone.rinhabackendspringboot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

public class TransactionBalance {

    @JsonProperty("valor")
    public Integer value;

    @JsonProperty("tipo")
    public String type;

    @JsonProperty("descricao")
    public String description;

    @JsonProperty("realizada_em")
    @DateTimeFormat(pattern="yyyy-MM-ddTHH:mm:ssZ")
    public OffsetDateTime CreateAt;

    public TransactionBalance(int valor, String tipo, String descricao, OffsetDateTime realizadaEm) {
        this.value = valor;
        this.type = tipo;
        this.description = descricao;
        this.CreateAt = realizadaEm;
    }
}
