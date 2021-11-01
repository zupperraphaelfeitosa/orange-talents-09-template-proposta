package br.com.zup.raphaelfeitosa.proposta.biometria;

import br.com.zup.raphaelfeitosa.proposta.cartao.Cartao;
import br.com.zup.raphaelfeitosa.proposta.cartao.CartaoRepository;
import br.com.zup.raphaelfeitosa.proposta.proposta.Proposta;
import br.com.zup.raphaelfeitosa.proposta.proposta.PropostaRepository;
import br.com.zup.raphaelfeitosa.proposta.proposta.StatusProposta;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class CriaNovaBiometriaControllerTest {

    private final String uri = "/api/v1/cartoes/";
    private Proposta proposta;
    private Cartao cartao;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Autowired
    private BiometriaRepository biometriaRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private PropostaRepository propostaRepository;

    @BeforeEach
    void setUp() {
        proposta = new Proposta(
                "John Doe",
                "johndoe@gmail.com",
                "99985090098",
                BigDecimal.valueOf(1500), "Travessa quinze");

        proposta.adicionaRestricao(StatusProposta.ELEGIVEL);
        propostaRepository.save(proposta);

        cartao = new Cartao(
                "5812-4804-7265-6806",
                LocalDateTime.now(),
                "John Doe",
                5000, proposta);
        cartaoRepository.save(cartao);
    }

    @Test
    @Order(1)
    void deveriaCadastrarUmaFingerPrintComRetorno201() throws Exception {

        BiometriaRequest novaBiometria = new BiometriaRequest("YmlvbWV0cmlh");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + cartao.getId() + "/biometrias")
                        .content(gson.toJson(novaBiometria))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isCreated())
                .andExpect(MockMvcResultMatchers
                        .redirectedUrlPattern("http://**/api/v1/biometrias/{spring:[0-9]+}"));

        assertTrue(propostaRepository.findByDocument("99985090098").isPresent());
        assertTrue(cartaoRepository.findByNumero("5812-4804-7265-6806").isPresent());
        assertTrue(biometriaRepository.findById(1L).isPresent());
    }

    @Order(2)
    @ParameterizedTest
    @ValueSource(strings = {"InvalidBase64"})
    @NullAndEmptySource
    void naoDeveriaCadastrarUmaFingerPrintComCampoNuloOuVazioOuBase64InvalidaComRetorno400(String fingerPrint) throws Exception {

        BiometriaRequest novaBiometria = new BiometriaRequest(fingerPrint);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + cartao.getId() + "/biometrias")
                        .content(gson.toJson(novaBiometria))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }

    @Test
    @Order(3)
    void naoDeveriaCadastrarUmaFingerPrintComIdDoCartaoInvalidoComRetorno404() throws Exception {

        BiometriaRequest novaBiometria = new BiometriaRequest("YmlvbWV0cmlh");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + "50/biometrias")
                        .content(gson.toJson(novaBiometria))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound());
    }
}
