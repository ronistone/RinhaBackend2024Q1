package br.com.ronistone.rinhabackendspringboot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

public class Transaction {

    @JsonProperty("id")
    public Integer id;

    @JsonProperty("cliente_id")
    public Integer clientId;

    @JsonProperty("valor")
    public Integer value;

    @JsonProperty("tipo")
    public String type;

    @JsonProperty("descricao")
    public String description;

    @JsonProperty("realizada_em")
    @DateTimeFormat(pattern="yyyy-MM-ddTHH:mm:ssZ")
    public OffsetDateTime createAt;

}
