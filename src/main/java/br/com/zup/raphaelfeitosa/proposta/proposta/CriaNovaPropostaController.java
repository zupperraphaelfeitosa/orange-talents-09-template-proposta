package br.com.zup.raphaelfeitosa.proposta.proposta;

import br.com.zup.raphaelfeitosa.proposta.cartao.RetornoAnaliseCartao;
import br.com.zup.raphaelfeitosa.proposta.cartao.feign.ServicoAnaliseApi;
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
    private final PropostaRepository propostaRepository;
    private final ServicoAnaliseApi servicoAnaliseApi;

    public CriaNovaPropostaController(PropostaRepository propostaRepository, ServicoAnaliseApi servicoAnaliseApi) {
        this.propostaRepository = propostaRepository;
        this.servicoAnaliseApi = servicoAnaliseApi;
    }

    @PostMapping("/api/v1/propostas")
    @Transactional
    public ResponseEntity<Void> criarNovaProposta(@RequestBody @Valid PropostaRequest request,
                                                  UriComponentsBuilder uriBuilder) {
        Proposta novaProposta = request.toProposta();
        propostaRepository.save(novaProposta);
        verificaSolicitacaoDeAnalise(novaProposta);

        URI uri = uriBuilder.path("/api/v1/propostas/{id}").buildAndExpand(novaProposta.getId()).toUri();
        logger.info("Proposta documento={} criada com sucesso!", novaProposta.getDocument());
        return ResponseEntity.created(uri).build();
    }

    private void verificaSolicitacaoDeAnalise(Proposta novaProposta) {
        try {
            RetornoAnaliseCartao retornoAnaliseCartao = servicoAnaliseApi
                    .solicitaVerificacao(novaProposta.toSolicitaAnaliseCartao());
            novaProposta.verificaRetornoAnalise(retornoAnaliseCartao);
            propostaRepository.save(novaProposta);
            logger.info("Proposta documento={} atualizada para status={} ",
                    novaProposta.getDocument(), novaProposta.getStatus());

        } catch (FeignException.UnprocessableEntity exception) {
            novaProposta.adicionaRestricao(StatusProposta.NAO_ELEGIVEL);
            propostaRepository.save(novaProposta);
            logger.info("Proposta documento={} atualizada para status={} ",
                    novaProposta.getDocument(), novaProposta.getStatus());

        } catch (FeignException.InternalServerError exception) {
            logger.error("Proposta  documento={}, não foi possível acessar o serviço de analise financeira. Erro: {}",
                    novaProposta.getDocument(), exception.getLocalizedMessage());
        }
    }

}
