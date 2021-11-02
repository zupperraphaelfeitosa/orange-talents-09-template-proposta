package br.com.zup.raphaelfeitosa.proposta.proposta;

import br.com.zup.raphaelfeitosa.proposta.cartao.RetornoAnaliseCartaoServicoAnaliseApi;
import br.com.zup.raphaelfeitosa.proposta.cartao.StatusAnaliseCartao;
import br.com.zup.raphaelfeitosa.proposta.cartao.feign.ServicoAnaliseApi;
import com.google.gson.Gson;
import feign.FeignException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class CriaNovaPropostaControllerTest {

    private final String uri = "/api/v1/propostas";

    @Autowired
    private Gson gson;

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServicoAnaliseApi servicoAnaliseApi;

    @BeforeEach
    void setUp() {
        propostaRepository.deleteAll();
    }

    @Test
    @Order(1)
    void deveriaCadastrarNovaPropostaComRetornoSEM_RESTRICAOERetorno201() throws Exception {

        RetornoAnaliseCartaoServicoAnaliseApi retornoAnaliseCartaoServicoAnaliseApi = new RetornoAnaliseCartaoServicoAnaliseApi("761.159.900-33", "John Doe", StatusAnaliseCartao.SEM_RESTRICAO, Long.toString(1L));
        PropostaRequest novaProposta = new PropostaRequest(
                "John Doe", "johndoe@gmail.com", "761.159.900-33", BigDecimal.valueOf(1000), "Rua treze de maio");

        Mockito.when(servicoAnaliseApi.solicitaVerificacao(Mockito.any())).thenReturn(retornoAnaliseCartaoServicoAnaliseApi);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(novaProposta)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isCreated())
                .andExpect(MockMvcResultMatchers
                        .redirectedUrlPattern("http://**/api/v1/propostas/{spring:[0-9]+}"));

        Proposta propostaSalva = propostaRepository.findByEmail("johndoe@gmail.com").get();

        assertEquals("johndoe@gmail.com", propostaSalva.getEmail());
        assertEquals("761.159.900-33", propostaSalva.getDocument());
        assertEquals(StatusProposta.ELEGIVEL, propostaSalva.getStatus());
    }

    @Test
    @Order(2)
    void deveriaCadastrarNovaPropostaComRetornoCOM_RESTRICAOERetorno201() throws Exception {

        PropostaRequest novaProposta = new PropostaRequest(
                "John Doe", "johndoe@gmail.com", "317.357.970-49", BigDecimal.valueOf(1000), "Rua treze de maio");

        FeignException.UnprocessableEntity feignException = Mockito.mock(FeignException.UnprocessableEntity.class);
        Mockito.when(servicoAnaliseApi.solicitaVerificacao(Mockito.any())).thenThrow(feignException);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(novaProposta)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isCreated());

        Proposta propostaSalva = propostaRepository.findByEmail("johndoe@gmail.com").get();

        assertEquals("johndoe@gmail.com", propostaSalva.getEmail());
        assertEquals("317.357.970-49", propostaSalva.getDocument());
        assertEquals(StatusProposta.NAO_ELEGIVEL, propostaSalva.getStatus());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"invalidEmail.com", "@invalid.com", "@.com", "@invalid"})
    @Order(3)
    void naoDeveriaCadastarNovaPropostaComEmailInvalidoComRetorno400(String email) throws Exception {

        PropostaRequest novaProposta = new PropostaRequest(
                "John Doe", email, "761.159.900-33", BigDecimal.valueOf(1000), "Rua treze de maio");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(novaProposta)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
        assertTrue(propostaRepository.findByEmail(email).isEmpty());
    }

    @Test
    @Order(4)
    void naoDeveriaCadastarNovaPropostaComBodyVazioComRetorno400() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ }"))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"999999", "333.333.333-40", "33.333.33/0001-33"})
    @Order(5)
    void naoDeveriaCadastarNovaPropostaComCPFouCNPJInvalidoComRetorno400(String document) throws Exception {

        PropostaRequest novaProposta = new PropostaRequest(
                "John Doe", "johndoe@gmail.com", document, BigDecimal.valueOf(1000), "Rua treze de maio");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(novaProposta)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
        assertTrue(propostaRepository.findByEmail("johndoe@gmail.com").isEmpty());
    }

    @ParameterizedTest
    @ValueSource(doubles = {00.00, -700.00, -100})
    @Order(6)
    void naoDeveriaCadastarNovaPropostaComSalarioNegativoInvalidoComRetorno400(Double salary) throws Exception {

        PropostaRequest novaProposta = new PropostaRequest(
                "John Doe", "johndoe@gmail.com", "761.159.900-33", BigDecimal.valueOf(salary), "Rua treze de maio");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(novaProposta)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
        assertTrue(propostaRepository.findByEmail("johndoe@gmail.com").isEmpty());
    }

    @ParameterizedTest
    @NullSource
    @Order(7)
    void naoDeveriaCadastarNovaPropostaComSalarioNuloComRetorno400(BigDecimal salary) throws Exception {

        PropostaRequest novaProposta = new PropostaRequest(
                "John Doe", "johndoe@gmail.com", "761.159.900-33", salary, "Rua treze de maio");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(novaProposta)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
        assertTrue(propostaRepository.findByEmail("johndoe@gmail.com").isEmpty());
    }

    @Test
    @Order(8)
    void naoDeveriaCadastrarNovaPropostaComPropostaExistenteVinculadaAoDocumentoComRetorno422() throws Exception {

        PropostaRequest novaProposta = new PropostaRequest(
                "John Doe", "johndoe@gmail.com", "317.357.970-49", BigDecimal.valueOf(1000), "Rua treze de maio");

        mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(novaProposta)));

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(novaProposta)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isUnprocessableEntity());

        assertTrue(propostaRepository.findByDocument("317.357.970-49").isPresent());
    }
}
