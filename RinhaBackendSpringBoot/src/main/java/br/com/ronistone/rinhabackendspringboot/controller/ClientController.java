package br.com.ronistone.rinhabackendspringboot.controller;

import br.com.ronistone.rinhabackendspringboot.model.ClientBalance;
import br.com.ronistone.rinhabackendspringboot.model.Extract;
import br.com.ronistone.rinhabackendspringboot.model.Transaction;
import br.com.ronistone.rinhabackendspringboot.model.TransactionClientBalance;
import br.com.ronistone.rinhabackendspringboot.repository.ClientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.Thread.sleep;

@RestController
@RequestMapping(path = "/clientes")
public class ClientController {

    private final ClientRepository clientRepository;
    private boolean isHealthy = false;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
        System.out.println("ClientController");
    }

    @PostMapping("/{id}/transacoes")
    public ResponseEntity<TransactionClientBalance> createTransaction(@PathVariable("id") Integer id, @RequestBody Transaction transaction) {

        if((!transaction.type.equals("d") && !transaction.type.equals("c"))
                || transaction.description == null || transaction.description.isEmpty() || transaction.description.length() > 10) {
            return ResponseEntity.unprocessableEntity().build();
        }

        try {
            transaction.clientId = id;
            ClientBalance result = clientRepository.makeTransaction(transaction);
            TransactionClientBalance transactionClientBalance = new TransactionClientBalance(result.limit, result.value);
            return ResponseEntity.ok(transactionClientBalance);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @GetMapping("/{id}/extrato")
    public ResponseEntity<Extract> getExtract(@PathVariable("id") Integer id, @RequestParam(name = "skip", required = false) boolean skip) {
        if(!isHealthy && !skip) {
            return ResponseEntity.status(503).build();
        }

        if(id < 1 || id > 5) {
            return ResponseEntity.notFound().build();
        }
        Extract extract = clientRepository.getExtract(id);
        return ResponseEntity.ok(extract);
    }

    @DeleteMapping("/reset")
    public ResponseEntity<Void> reset() {
        clientRepository.reset();
        isHealthy = true;
        return ResponseEntity.ok().build();
    }

}
