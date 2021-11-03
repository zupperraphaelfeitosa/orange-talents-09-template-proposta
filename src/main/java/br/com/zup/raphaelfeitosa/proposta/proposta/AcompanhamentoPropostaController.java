package br.com.zup.raphaelfeitosa.proposta.proposta;

import br.com.zup.raphaelfeitosa.proposta.util.OfuscaDadoSensivel;
import br.com.zup.raphaelfeitosa.proposta.validations.exceptions.ApiResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AcompanhamentoPropostaController implements OfuscaDadoSensivel {

    private final Logger logger = LoggerFactory.getLogger(AcompanhamentoPropostaController.class);
    private final PropostaRepository propostaRepository;

    public AcompanhamentoPropostaController(PropostaRepository propostaRepository) {
        this.propostaRepository = propostaRepository;
    }

    @GetMapping("/api/v1/propostas/{id}")
    public ResponseEntity<AcompanhamentoPropostaResponse> consultarProposta(@PathVariable(name = "id") Long id) {

        Proposta proposta = propostaRepository.findById(id)
                .orElseThrow(() -> new ApiResponseException(
                        "idProposta", "Id da proposta é inválido", HttpStatus.NOT_FOUND));

        logger.info("Proposta de id: {} localizada!", proposta.getId());

        return ResponseEntity.ok().body(new AcompanhamentoPropostaResponse(proposta));
    }
}
