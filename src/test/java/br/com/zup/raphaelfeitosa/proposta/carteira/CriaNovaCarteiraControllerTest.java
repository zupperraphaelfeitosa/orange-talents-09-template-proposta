package br.com.zup.raphaelfeitosa.proposta.carteira;

import br.com.zup.raphaelfeitosa.proposta.cartao.Cartao;
import br.com.zup.raphaelfeitosa.proposta.cartao.CartaoRepository;
import br.com.zup.raphaelfeitosa.proposta.cartao.feign.ServicoCartaoApi;
import br.com.zup.raphaelfeitosa.proposta.proposta.Proposta;
import br.com.zup.raphaelfeitosa.proposta.proposta.PropostaRepository;
import br.com.zup.raphaelfeitosa.proposta.proposta.StatusProposta;
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
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class CriaNovaCarteiraControllerTest {

    private final String uri = "/api/v1/cartoes/";
    private Proposta proposta;
    private Cartao cartao;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Autowired
    private CarteiraRepository carteiraRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private PropostaRepository propostaRepository;

    @MockBean
    private ServicoCartaoApi servicoCartaoApi;

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

    @Order(1)
    @ParameterizedTest
    @ValueSource(strings = {"PAYPAL", "SAMSUNG_PAY"})
    void deveriaCadastrarUmNovaCarteiraComRetorno200(String carteira) throws Exception {
        RetornoCateiraServicoCartaoApi retornoCarteira = Mockito.mock(RetornoCateiraServicoCartaoApi.class);
        Mockito.when(retornoCarteira.getResultado()).thenReturn(StatusCarteira.ASSOCIADA);
        Mockito.when(servicoCartaoApi.associaCartaoACarteira(Mockito.any(), Mockito.any())).thenReturn(retornoCarteira);

        CarteiraRequest novaCarteira = new CarteiraRequest("johndoe@gmail.com", TipoCarteira.valueOf(carteira));

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + cartao.getId() + "/carteiras")
                        .content(gson.toJson(novaCarteira))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isCreated());
        assertTrue(carteiraRepository.findByCartaoAndCarteira(cartao, TipoCarteira.valueOf(carteira)).isPresent());
    }

    @Test
    @Order(2)
    void naoDeveriaCadastrarUmaNovaCarteiraComRetorno422() throws Exception {
        FeignException feignException = Mockito.mock(FeignException.class);
        Mockito.when(servicoCartaoApi.associaCartaoACarteira(Mockito.any(), Mockito.any())).thenThrow(feignException);
        CarteiraRequest novaCarteira = new CarteiraRequest("johndoe@gmail.com", TipoCarteira.PAYPAL);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + cartao.getId() + "/carteiras")
                        .content(gson.toJson(novaCarteira))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isUnprocessableEntity());
        assertTrue(carteiraRepository.findByCartaoAndCarteira(cartao, TipoCarteira.PAYPAL).isEmpty());
    }

    @Order(3)
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"invalidEmail.com", "@invalid.com", "@.com", "@invalid"})
    void naoDeveriaCadastrarUmaNovaCarteiraComEmailInvalidoComRetorno400(String email) throws Exception {
        CarteiraRequest novaCarteira = new CarteiraRequest(email, TipoCarteira.PAYPAL);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + cartao.getId() + "/carteiras")
                        .content(gson.toJson(novaCarteira))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }

    @Order(4)
    @ParameterizedTest
    @NullSource
    void naoDeveriaCadastrarUmaNovaCarteiraComEmailInvalidoComRetorno400(TipoCarteira carteira) throws Exception {
        CarteiraRequest novaCarteira = new CarteiraRequest("johndoe@gmail.com", carteira);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + cartao.getId() + "/carteiras")
                        .content(gson.toJson(novaCarteira))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }

    @Test
    @Order(5)
    void naoDeveriaCadastrarUmAvisoDeViagemComIdCartaoInvalidoComRetorno404() throws Exception {
        CarteiraRequest novaCarteira = new CarteiraRequest("johndoe@gmail.com", TipoCarteira.PAYPAL);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + 50L + "/carteiras")
                        .content(gson.toJson(novaCarteira))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound());
    }
}
