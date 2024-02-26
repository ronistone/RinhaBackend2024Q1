package br.com.ronistone.rinhabackendspringboot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Extract {

    @JsonProperty("saldo")
    public ClientBalance balance;

    @JsonProperty("ultimas_transacoes")
    public List<TransactionBalance> transactions;

}
