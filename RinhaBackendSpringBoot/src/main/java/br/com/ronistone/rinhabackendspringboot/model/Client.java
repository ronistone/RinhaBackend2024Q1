package br.com.ronistone.rinhabackendspringboot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Client {

    @JsonProperty("id")
    public Integer id;

    @JsonProperty("limite")
    public Integer limit;

    @JsonProperty("valor")
    public Integer value;

}
