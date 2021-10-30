package br.com.zup.raphaelfeitosa.proposta.bloqueio;

import br.com.zup.raphaelfeitosa.proposta.cartao.Cartao;
import br.com.zup.raphaelfeitosa.proposta.cartao.CartaoRepository;
import br.com.zup.raphaelfeitosa.proposta.util.OfuscaDadoSensivel;
import br.com.zup.raphaelfeitosa.proposta.validations.exceptions.ApiResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;

@RestController
public class SolicitaBloqueioController implements OfuscaDadoSensivel {

    private final Logger logger = LoggerFactory.getLogger(SolicitaBloqueioController.class);
    private final CartaoRepository cartaoRepository;
    private final BloqueioRepository bloqueioRepository;

    public SolicitaBloqueioController(CartaoRepository cartaoRepository, BloqueioRepository bloqueioRepository) {
        this.cartaoRepository = cartaoRepository;
        this.bloqueioRepository = bloqueioRepository;
    }

    @PostMapping("/api/v1/bloqueios/{id}")
    @Transactional
    public ResponseEntity<Void> solicitaBloqueio(@PathVariable(name = "id") Long id,
                                                 @RequestHeader(value = "User-Agent") String userAgent,
                                                 HttpServletRequest request) {
        Cartao cartao = cartaoRepository.findById(id)
                .orElseThrow(() -> new ApiResponseException(
                        "idCartao", "Id do cartão é inválido", HttpStatus.NOT_FOUND));
        logger.info("Cartão de id: {} localizado!", cartao.getId());

        Optional<Bloqueio> bloqueio = bloqueioRepository.findByNumero(cartao.getNumero());
        if (bloqueio.isPresent())
            throw new ApiResponseException("bloqueio", "Esse cartão já está bloqueado", HttpStatus.UNPROCESSABLE_ENTITY);

        Bloqueio bloqueiaCartao = new Bloqueio(request.getRemoteAddr(), userAgent, cartao.getNumero(), cartao);
        cartao.bloquear();
        bloqueioRepository.save(bloqueiaCartao);
        logger.info("Bloqueio do cartão: {} realizado com sucesso!", ofuscaCartao(cartao.getNumero()));
        return ResponseEntity.ok().build();
    }
}
