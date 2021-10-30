package br.com.zup.raphaelfeitosa.proposta.proposta;

import br.com.zup.raphaelfeitosa.proposta.cartao.Cartao;
import br.com.zup.raphaelfeitosa.proposta.cartao.CartaoRepository;
import com.google.gson.Gson;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class AcompanhamentoPropostaControllerTest {

    private final String uri = "/api/v1/propostas/";

    @Autowired
    private Gson gson;

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void deveriaRetornar404ComIdPropostaInvalido() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri + 50L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound());
    }

    @Test
    @Order(2)
    void deveriaRetornarAcompanhamentoDaProposta() throws Exception {

        propostaRepository.deleteAll();
        cartaoRepository.deleteAll();

        Proposta propostaElegivelSemCartao = new Proposta(
                "John Doe",
                "johndoe@gmail.com",
                "99985090098",
                BigDecimal.valueOf(1500), "Travessa quinze");

        propostaElegivelSemCartao.adicionaRestricao(StatusProposta.ELEGIVEL);
        propostaRepository.save(propostaElegivelSemCartao);

        Cartao cartao = new Cartao(
                "5812-4804-7265-6806",
                LocalDateTime.now(),
                "John Doe",
                5000, propostaElegivelSemCartao);
        cartaoRepository.save(cartao);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri + propostaElegivelSemCartao.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());
    }
}
