package br.com.zup.raphaelfeitosa.proposta.aviso;

import br.com.zup.raphaelfeitosa.proposta.cartao.Cartao;
import br.com.zup.raphaelfeitosa.proposta.cartao.CartaoRepository;
import br.com.zup.raphaelfeitosa.proposta.util.OfuscaDadoSensivel;
import br.com.zup.raphaelfeitosa.proposta.validations.exceptions.ApiResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/cartoes")
public class CriaNovoAvisoViagemController implements OfuscaDadoSensivel {

    private final Logger logger = LoggerFactory.getLogger(CriaNovoAvisoViagemController.class);
    private final CartaoRepository cartaoRepository;
    private final AvisoViagemRepository avisoViagemRepository;

    public CriaNovoAvisoViagemController(CartaoRepository cartaoRepository, AvisoViagemRepository avisoViagemRepository) {
        this.cartaoRepository = cartaoRepository;
        this.avisoViagemRepository = avisoViagemRepository;
    }

    @PostMapping("/{id}/aviso-viagem")
    @Transactional
    public ResponseEntity<Void> criarNovoAvisoViagem(@PathVariable(name = "id") Long id,
                                                     @RequestHeader(value = "User-Agent") String userAgent,
                                                     HttpServletRequest request,
                                                     @RequestBody @Valid AvisoViagemRequest avisoViagemRequest) {
        Cartao cartao = cartaoRepository.findById(id)
                .orElseThrow(() -> new ApiResponseException(
                        "idCartao", "Id do cartão é inválido", HttpStatus.NOT_FOUND));
        logger.info("Cartão de id: {} localizado!", cartao.getId());

        AvisoViagem novoAvisoViagem = avisoViagemRequest.toAvisoViagem(request.getRemoteAddr(), userAgent, cartao);
        avisoViagemRepository.save(novoAvisoViagem);
        logger.info("Aviso de viagem cartão: {} cadastrado com sucesso!", ofuscaCartao(cartao.getNumero()));

        return ResponseEntity.ok().build();
    }
}
