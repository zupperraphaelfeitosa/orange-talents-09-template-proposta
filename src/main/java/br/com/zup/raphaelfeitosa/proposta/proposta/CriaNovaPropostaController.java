package br.com.zup.raphaelfeitosa.proposta.proposta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class CriaNovaPropostaController {

    private final Logger logger = LoggerFactory.getLogger(Proposta.class);

    private PropostaRepository propostaRepository;

    public CriaNovaPropostaController(PropostaRepository propostaRepository) {
        this.propostaRepository = propostaRepository;
    }

    @PostMapping("/api/v1/propostas")
    public ResponseEntity<?> criarNovaProposta(@RequestBody @Valid PropostaRequest request,
                                               UriComponentsBuilder uriBuilder) {

        Proposta novaProposta = request.toProposta();

        propostaRepository.save(novaProposta);

        propostaCriada(novaProposta);

        URI uri = uriBuilder.path("/api/v1/propostas/{id}").buildAndExpand(novaProposta.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    public void propostaCriada(Proposta proposta) {
        logger.info("Proposta documento={}, salário={} criada com sucesso!", proposta.getDocument(), proposta.getSalary());
    }
}
