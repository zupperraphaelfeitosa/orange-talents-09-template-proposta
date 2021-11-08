package br.com.zup.raphaelfeitosa.proposta.bloqueio;

import br.com.zup.raphaelfeitosa.proposta.cartao.Cartao;
import br.com.zup.raphaelfeitosa.proposta.cartao.CartaoRepository;
import br.com.zup.raphaelfeitosa.proposta.feign.ServicoCartaoApi;
import br.com.zup.raphaelfeitosa.proposta.config.util.OfuscaDadoSensivel;
import br.com.zup.raphaelfeitosa.proposta.validations.exceptions.ApiResponseException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cartoes")
public class SolicitaBloqueioController implements OfuscaDadoSensivel {

    private final Logger logger = LoggerFactory.getLogger(SolicitaBloqueioController.class);
    private final CartaoRepository cartaoRepository;
    private final BloqueioRepository bloqueioRepository;
    private final ServicoCartaoApi servicoCartaoApi;

    public SolicitaBloqueioController(CartaoRepository cartaoRepository, BloqueioRepository bloqueioRepository, ServicoCartaoApi servicoCartaoApi) {
        this.cartaoRepository = cartaoRepository;
        this.bloqueioRepository = bloqueioRepository;
        this.servicoCartaoApi = servicoCartaoApi;
    }

    @PostMapping("/{id}/bloqueios")
    @Transactional
    public ResponseEntity<?> solicitaBloqueio(@PathVariable(name = "id") Long id,
                                                 @RequestHeader(value = "User-Agent") String userAgent,
                                                 HttpServletRequest request) {
        Cartao cartao = cartaoRepository.findById(id)
                .orElseThrow(() -> new ApiResponseException(
                        "idCartao", "Id do cartão é inválido", HttpStatus.NOT_FOUND));
        logger.info("Cartão de id: {} localizado!", cartao.getId());

        Optional<Bloqueio> bloqueio = bloqueioRepository.findByCartao_id(id);
        if (bloqueio.isPresent())
            throw new ApiResponseException("bloqueio", "Esse cartão já está bloqueado", HttpStatus.UNPROCESSABLE_ENTITY);

        notificacaoBloqueioCartao(cartao, userAgent, request.getRemoteAddr());
        logger.info("Bloqueio do cartão: {} realizado com sucesso!", ofuscaCartao(cartao.getNumero()));
        return ResponseEntity.ok().build();
    }

    private void notificacaoBloqueioCartao(Cartao cartao, String userAgent, String ipCliente) {
        try {
            RetornoBloqueioServicoCartaoApi retornoBloqueioServicoCartaoApi = servicoCartaoApi
                    .notificacaoBloqueio(cartao.getNumero(), cartao.toNotificacaoBloqueio());

            Bloqueio bloqueiaCartao = new Bloqueio(ipCliente, userAgent, cartao);
            cartao.bloquear();
            bloqueioRepository.save(bloqueiaCartao);
            logger.info("Serviço cartão API - Cartão: {} ", retornoBloqueioServicoCartaoApi.getResultado());

        } catch (FeignException exception) {
            logger.error("não foi possível realizar a notificaçao de bloqueio do cartão: {}  Erro: {}",
                    ofuscaCartao(cartao.getNumero()), exception.getLocalizedMessage());
            throw new ApiResponseException("cartoes", "Não foi possível realizar a notificação de bloqueio", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
