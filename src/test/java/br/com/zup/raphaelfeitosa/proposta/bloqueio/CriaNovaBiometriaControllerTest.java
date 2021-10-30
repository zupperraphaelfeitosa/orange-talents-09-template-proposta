package br.com.zup.raphaelfeitosa.proposta.bloqueio;

import br.com.zup.raphaelfeitosa.proposta.cartao.Cartao;
import br.com.zup.raphaelfeitosa.proposta.cartao.CartaoRepository;
import br.com.zup.raphaelfeitosa.proposta.proposta.Proposta;
import br.com.zup.raphaelfeitosa.proposta.proposta.PropostaRepository;
import br.com.zup.raphaelfeitosa.proposta.proposta.StatusProposta;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
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

    private final String uri = "/api/v1/bloqueios/";
    private Proposta proposta;
    private Cartao cartao;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Autowired
    private BloqueioRepository bloqueioRepository;

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
    void deveriaCadastrarBloqueioDeCartaoComRetorno200() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + cartao.getId())
                        .header("User-Agent", "PostmanRuntime/7.28.4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());

        assertTrue(propostaRepository.findByDocument("99985090098").isPresent());
        assertTrue(cartaoRepository.findByNumero("5812-4804-7265-6806").isPresent());
        assertTrue(bloqueioRepository.findByNumero("5812-4804-7265-6806").isPresent());
    }

    @Test
    @Order(2)
    void naoDeveriaCadastrarUmBloqueioDeCartaoJaBloqueadoComRetorno422() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post(uri + cartao.getId())
                .header("User-Agent", "PostmanRuntime/7.28.4")
                .contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + cartao.getId())
                        .header("User-Agent", "PostmanRuntime/7.28.4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isUnprocessableEntity());
    }

    @Test
    @Order(3)
    void naoDeveriaCadastrarUmBloqueiDeCartaoComIdInvalidoComRetorno402() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + 50L)
                        .header("User-Agent", "PostmanRuntime/7.28.4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound());
    }
}
