package br.com.zup.raphaelfeitosa.proposta.proposta;

import br.com.zup.raphaelfeitosa.proposta.cartao.RetornoAnaliseCartao;
import br.com.zup.raphaelfeitosa.proposta.cartao.feign.ConsultaDadosFinanceiroSolicitante;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
public class CriaNovaPropostaController {

    private final Logger logger = LoggerFactory.getLogger(Proposta.class);

    private PropostaRepository propostaRepository;

    private ConsultaDadosFinanceiroSolicitante consultaDadosFinanceiroSolicitante;

    public CriaNovaPropostaController(PropostaRepository propostaRepository, ConsultaDadosFinanceiroSolicitante consultaDadosFinanceiroSolicitante) {
        this.propostaRepository = propostaRepository;
        this.consultaDadosFinanceiroSolicitante = consultaDadosFinanceiroSolicitante;
    }

    @PostMapping("/api/v1/propostas")
    @Transactional
    public ResponseEntity<Void> criarNovaProposta(@RequestBody @Valid PropostaRequest request,
                                               UriComponentsBuilder uriBuilder) {

        Proposta novaProposta = request.toProposta();

        propostaRepository.save(novaProposta);

        verificaSolicitacaoDeAnalise(novaProposta);

        propostaCriada(novaProposta);

        URI uri = uriBuilder.path("/api/v1/propostas/{id}").buildAndExpand(novaProposta.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    private void verificaSolicitacaoDeAnalise(Proposta novaProposta) {
        try {
            RetornoAnaliseCartao retornoAnaliseCartao = consultaDadosFinanceiroSolicitante
                    .solicitaVerificacao(novaProposta.toSolicitaAnaliseCartao());
            novaProposta.verificaRetornoAnalise(retornoAnaliseCartao);
            propostaRepository.save(novaProposta);

        } catch (FeignException.UnprocessableEntity exception) {
            novaProposta.adicionaRestricao(StatusProposta.NAO_ELEGIVEL);
            propostaRepository.save(novaProposta);
        }
    }

    private void propostaCriada(Proposta proposta) {
        logger.info("Proposta documento={}, salário={} criada com sucesso!", proposta.getDocument(), proposta.getSalary());
    }
}
