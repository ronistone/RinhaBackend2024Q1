package br.com.ronistone.rinhabackendspringboot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionCreate {

    @JsonProperty("limite")
    public Integer limit;

    @JsonProperty("saldo")
    public Integer balance;

}
