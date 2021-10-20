package br.com.zup.raphaelfeitosa.proposta.proposta;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CriaNovaPropostaControllerTest {

    private final String uri = "/api/v1/propostas";

    @Autowired
    private Gson gson;

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        propostaRepository.deleteAll();
    }

    @Test
    @Order(1)
    void deveriaCadastrarNovaPropostaComRetorno201() throws Exception {

        PropostaRequest novaProposta = new PropostaRequest(
                "John Doe", "johndoe@gmail.com", "761.159.900-33", BigDecimal.valueOf(1000), "Rua treze de maio");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(novaProposta)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isCreated())
                .andExpect(MockMvcResultMatchers
                        .redirectedUrlPattern("http://localhost/api/v1/propostas/*"));

        assertTrue(propostaRepository.findByEmail("johndoe@gmail.com").isPresent());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"invalidEmail.com", "@invalid.com", "@.com", "@invalid"})
    @Order(2)
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
    @Order(3)
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
    @Order(4)
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
    @Order(5)
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
    @Order(6)
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
}
