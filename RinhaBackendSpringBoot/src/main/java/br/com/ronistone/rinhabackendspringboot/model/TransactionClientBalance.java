package br.com.ronistone.rinhabackendspringboot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

public class TransactionClientBalance {

    @JsonProperty("limite")
    public Integer limit;

    @JsonProperty("saldo")
    public Integer value;

    public TransactionClientBalance(Integer limit, Integer value) {
        this.limit = limit;
        this.value = value;
    }
}
