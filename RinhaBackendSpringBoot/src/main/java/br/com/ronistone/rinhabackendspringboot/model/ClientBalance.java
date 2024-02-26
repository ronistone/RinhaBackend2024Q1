package br.com.ronistone.rinhabackendspringboot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;

public class ClientBalance {

    @JsonProperty("limite")
    public Integer limit;

    @JsonProperty("total")
    public Integer value;

    @JsonProperty("data_extrato")
    @DateTimeFormat(pattern="yyyy-MM-ddTHH:mm:ssZ")
    public OffsetDateTime dateExtract;

    public ClientBalance(Integer limit, Integer value, OffsetDateTime createAt) {
        this.limit = limit;
        this.value = value;
        this.dateExtract = createAt;
    }
}
