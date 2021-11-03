package br.com.zup.raphaelfeitosa.proposta.carteira;

import br.com.zup.raphaelfeitosa.proposta.cartao.Cartao;
import br.com.zup.raphaelfeitosa.proposta.cartao.CartaoRepository;
import br.com.zup.raphaelfeitosa.proposta.cartao.feign.ServicoCartaoApi;
import br.com.zup.raphaelfeitosa.proposta.util.OfuscaDadoSensivel;
import br.com.zup.raphaelfeitosa.proposta.validations.exceptions.ApiResponseException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cartoes")
public class CriaNovaCarteiraController implements OfuscaDadoSensivel {

    private final Logger logger = LoggerFactory.getLogger(CriaNovaCarteiraController.class);
    private final CartaoRepository cartaoRepository;
    private final CarteiraRepository carteiraRepository;
    private final ServicoCartaoApi servicoCartaoApi;

    public CriaNovaCarteiraController(CartaoRepository cartaoRepository, CarteiraRepository carteiraRepository, ServicoCartaoApi servicoCartaoApi) {
        this.cartaoRepository = cartaoRepository;
        this.carteiraRepository = carteiraRepository;
        this.servicoCartaoApi = servicoCartaoApi;
    }

    @PostMapping("/{id}/carteiras")
    @Transactional
    public ResponseEntity<?> criarNovaCarteira(@PathVariable(name = "id") Long id,
                                               @RequestBody @Valid CarteiraRequest carteiraRequest,
                                               UriComponentsBuilder uirBuilder) {
        Cartao cartao = cartaoRepository.findById(id)
                .orElseThrow(() -> new ApiResponseException(
                        "idCartao", "Id do cartão é inválido", HttpStatus.NOT_FOUND));
        logger.info("Cartão de id: {} localizado!", cartao.getId());

        Optional<Carteira> carteira = carteiraRepository.findByCartaoAndCarteira(cartao, carteiraRequest.getCarteira());
        if (carteira.isPresent())
            throw new ApiResponseException("carteira", "Cartão já associado a carteira", HttpStatus.UNPROCESSABLE_ENTITY);

        associaCartaoACarteira(cartao, carteiraRequest);
        Carteira novaCarteira = carteiraRequest.toCarteira(cartao);
        carteiraRepository.save(novaCarteira);
        URI uri = uirBuilder.path("/api/v1/cartoes/{id}/carteiras").buildAndExpand(novaCarteira.getId()).toUri();
        logger.info("Aviso de viagem cartão: {} cadastrado com sucesso!", ofuscaCartao(cartao.getNumero()));
        return ResponseEntity.created(uri).build();
    }

    private void associaCartaoACarteira(Cartao cartao, CarteiraRequest carteiraRequest) {
        try {
            RetornoCateiraServicoCartaoApi retornoCarteira = servicoCartaoApi.associaCartaoACarteira(
                    cartao.getNumero(), cartao.toSolicitaInclusaoCarteira(carteiraRequest));
            logger.info("Serviço cartão API - Carteira: {} a cartão: {}",
                    retornoCarteira.getResultado(), ofuscaCartao(cartao.getNumero()));

        } catch (FeignException exception) {
            logger.error("não foi possível associar o cartão: {}  a carteira. Erro: {}",
                    ofuscaCartao(cartao.getNumero()), exception.getLocalizedMessage());
            throw new ApiResponseException("Serviço cartão API", "não foi possível associar cartão a carteira", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
