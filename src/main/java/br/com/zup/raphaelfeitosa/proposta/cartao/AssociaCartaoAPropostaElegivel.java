package br.com.zup.raphaelfeitosa.proposta.cartao;

import br.com.zup.raphaelfeitosa.proposta.cartao.feign.ServicoCartaoApi;
import br.com.zup.raphaelfeitosa.proposta.proposta.Proposta;
import br.com.zup.raphaelfeitosa.proposta.proposta.PropostaRepository;
import br.com.zup.raphaelfeitosa.proposta.proposta.StatusProposta;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collection;

@Component
public class AssociaCartaoAPropostaElegivel {

    private final Logger logger = LoggerFactory.getLogger(AssociaCartaoAPropostaElegivel.class);
    private final PropostaRepository propostaRepository;
    private final ServicoCartaoApi servicoCartaoApi;

    public AssociaCartaoAPropostaElegivel(PropostaRepository propostaRepository, ServicoCartaoApi servicoCartaoApi) {
        this.propostaRepository = propostaRepository;
        this.servicoCartaoApi = servicoCartaoApi;
    }

    @Scheduled(fixedDelayString = "${scheduled.periodicidade.executa-operacao}")
    @Transactional
    public void associaCartaoAPropostaElegivel() {
        Collection<Proposta> propostasElegiveisSemCartao = propostaRepository.findByStatusAndCartao(StatusProposta.ELEGIVEL, null);
        propostasElegiveisSemCartao.forEach(proposta -> {
            try {
                RetornoCartaoResponse numeroCartao = servicoCartaoApi.solicitaCartao(proposta.toSolicitaAnaliseCartao());
                Cartao cartao = numeroCartao.toCartao(proposta);
                proposta.associaCartao(cartao);
                propostaRepository.save(proposta);
                logger.info("Proposta documento={} associada ao cartao cartao={} ", proposta.getDocument(), cartao.getNumero());

            } catch (FeignException exception) {
                logger.error("Erro inesperado: {}", exception.getLocalizedMessage());
            }
        });
    }
}
