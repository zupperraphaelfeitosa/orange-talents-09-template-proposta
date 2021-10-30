package br.com.zup.raphaelfeitosa.proposta.biometria;


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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Base64;

@RestController
public class CriaNovaBiometriaController implements OfuscaDadoSensivel {

    private final Logger logger = LoggerFactory.getLogger(CriaNovaBiometriaController.class);
    private final BiometriaRepository biometriaRepository;
    private final CartaoRepository cartaoRepository;

    public CriaNovaBiometriaController(BiometriaRepository biometriaRepository, CartaoRepository cartaoRepository) {
        this.biometriaRepository = biometriaRepository;
        this.cartaoRepository = cartaoRepository;
    }

    @PostMapping("/api/v1/biometrias/{id}")
    @Transactional
    public ResponseEntity<Void> cadastrarNovaBiometrica(@PathVariable(name = "id") Long id,
                                                        @RequestBody @Valid BiometriaRequest request,
                                                        UriComponentsBuilder uriBuilder) {
        Cartao cartao = cartaoRepository.findById(id)
                .orElseThrow(() -> new ApiResponseException(
                        "idCartao", "Id do cartão é inválido", HttpStatus.NOT_FOUND));

        logger.info("Cartão de id: {} localizado!", cartao.getId());

        validaBiometriaBase64(request.getFingerPrint());

        Biometria novaBiometria = request.toBiometria(cartao);
        biometriaRepository.save(novaBiometria);

        URI uri = uriBuilder.path("/api/v1/biometrias/{id}").buildAndExpand(novaBiometria.getId()).toUri();
        logger.info("Foi registrado uma nova biometria com id={} para o cartão={}",
                novaBiometria.getId(), ofuscaCartao(cartao.getNumero()));
        return ResponseEntity.created(uri).build();
    }

    private void validaBiometriaBase64(String fingerPrint) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            decoder.decode(fingerPrint);
            logger.info("fingerPrint validada com sucesso!");
        } catch (IllegalArgumentException e) {
            logger.error("fingerPrint invalida!");
            throw new ApiResponseException("fingerPrint", "O campo informado não é um base64!", HttpStatus.BAD_REQUEST);
        }
    }
}
