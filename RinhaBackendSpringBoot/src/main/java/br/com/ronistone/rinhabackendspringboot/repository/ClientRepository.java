package br.com.ronistone.rinhabackendspringboot.repository;

import br.com.ronistone.rinhabackendspringboot.model.ClientBalance;
import br.com.ronistone.rinhabackendspringboot.model.Extract;
import br.com.ronistone.rinhabackendspringboot.model.Transaction;
import br.com.ronistone.rinhabackendspringboot.model.TransactionBalance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Component
public class ClientRepository {

    private JdbcTemplate jdbcTemplate;

    public ClientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public ClientBalance makeTransaction(Transaction transaction) {
        var clientResul = jdbcTemplate.query("SELECT valor, limite FROM clientes WHERE id = ? LIMIT 1 FOR NO KEY UPDATE", (rs, rowNum) -> {
            Integer value = rs.getInt("valor");
            Integer limit = rs.getInt("limite");
            if (transaction.type.equals("c")) {
                value += transaction.value;
            } else {
                if (value - transaction.value < -limit) {
                    throw new RuntimeException("Insufficient funds");
                }
                value -= transaction.value;
            }
            jdbcTemplate.update("UPDATE clientes SET valor = ? WHERE id = ?", value, transaction.clientId);
            jdbcTemplate.update("INSERT INTO transacoes (cliente_id, valor, tipo, descricao, realizada_em) VALUES (?, ?, ?, ?, now())", transaction.clientId, transaction.value, transaction.type, transaction.description);
            return new ClientBalance(limit, value, transaction.createAt);
        }, transaction.clientId);

        if (clientResul.isEmpty()) {
            throw new RuntimeException("Client not found");
        }
        return clientResul.getFirst();
    }

    public Extract getExtract(Integer clientId) {
        Extract extract = new Extract();
        jdbcTemplate.query("SELECT limite, valor, now() as data_extrato FROM clientes where id = ?", (rs, rowNum) -> {
            ClientBalance clientBalance = new ClientBalance(rs.getInt("limite"), rs.getInt("valor"), rs.getObject("data_extrato", OffsetDateTime.class));
            extract.balance = clientBalance;

            jdbcTemplate.query("SELECT valor, tipo, descricao, realizada_em FROM transacoes where cliente_id = ? ORDER BY realizada_em DESC LIMIT 10", (rs2, rowNum2) -> {
                if(extract.transactions == null) {
                    extract.transactions = new java.util.ArrayList<>();
                }
                extract.transactions.add(new TransactionBalance(rs2.getInt("valor"), rs2.getString("tipo"), rs2.getString("descricao"), rs2.getObject("realizada_em", OffsetDateTime.class)));
                return null;
            }, clientId);
            return null;
        }, clientId);

        return extract;
    }

    public void reset() {
        jdbcTemplate.update("DELETE FROM transacoes");
        jdbcTemplate.update("UPDATE clientes SET valor = 0");
    }

    public void checkHealth() {
        jdbcTemplate.query("SELECT 1", (rs, rowNum) -> null);
    }


}
