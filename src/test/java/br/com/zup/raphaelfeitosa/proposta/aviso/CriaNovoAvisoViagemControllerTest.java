package br.com.zup.raphaelfeitosa.proposta.aviso;

import br.com.zup.raphaelfeitosa.proposta.cartao.Cartao;
import br.com.zup.raphaelfeitosa.proposta.cartao.CartaoRepository;
import br.com.zup.raphaelfeitosa.proposta.feign.ServicoCartaoApi;
import br.com.zup.raphaelfeitosa.proposta.proposta.Proposta;
import br.com.zup.raphaelfeitosa.proposta.proposta.PropostaRepository;
import br.com.zup.raphaelfeitosa.proposta.proposta.StatusProposta;
import com.google.gson.Gson;
import feign.FeignException;
import org.junit.jupiter.api.*;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class CriaNovoAvisoViagemControllerTest {

    private final String uri = "/api/v1/cartoes/";
    private Proposta proposta;
    private Cartao cartao;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Autowired
    private AvisoViagemRepository avisoViagemRepository;

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

    @Test
    @Order(1)
    void deveriaCadastrarUmNovoAvisoViagemComRetorno200() throws Exception {
        RetornoAvisoViagemServicoCartaoApi retornoAvisoViagem = Mockito.mock(RetornoAvisoViagemServicoCartaoApi.class);
        Mockito.when(retornoAvisoViagem.getResultado()).thenReturn(StatusAvisoViagem.CRIADO);
        Mockito.when(servicoCartaoApi.avisoViagem(Mockito.any(), Mockito.any())).thenReturn(retornoAvisoViagem);

        String novoAvisoViagem = "{\"destino\" : \"Belem\", \"validoAte\" : \"" + LocalDate.now().plusDays(15) + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + cartao.getId() + "/aviso-viagem")
                        .header("User-Agent", "PostmanRuntime/7.28.4")
                        .content(novoAvisoViagem)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());
        assertTrue(avisoViagemRepository.findById(1L).isPresent());
    }

    @Test
    @Order(2)
    void naoDeveriaCadastrarUmNovoAvisoViagemComRetorno422() throws Exception {
        FeignException feignException = Mockito.mock(FeignException.class);
        Mockito.when(servicoCartaoApi.avisoViagem(Mockito.any(), Mockito.any())).thenThrow(feignException);

        String novoAvisoViagem = "{\"destino\" : \"Belem\", \"validoAte\" : \"" + LocalDate.now().plusDays(15) + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + cartao.getId() + "/aviso-viagem")
                        .header("User-Agent", "PostmanRuntime/7.28.4")
                        .content(novoAvisoViagem)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isUnprocessableEntity());
    }

    @Order(3)
    @Test
    void naoDeveriaCadastrarUmAvisoDeViagemEmBrancoComRetorno400() throws Exception {
        String destinoNuloOuVazio = "{\"destino\" : \"\", \"validoAte\" : \"" + LocalDate.now().plusDays(15) + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + cartao.getId() + "/aviso-viagem")
                        .header("User-Agent", "PostmanRuntime/7.28.4")
                        .content(destinoNuloOuVazio)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }

    @Order(4)
    @Test
    void naoDeveriaCadastrarUmAvisoDeViagemComvalidoAteNoPassadoComRetorno400() throws Exception {
        String destinoNuloOuVazio = "{\"destino\" : \"\", \"validoAte\" : \"" + LocalDate.now().minusDays(5) + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + cartao.getId() + "/aviso-viagem")
                        .header("User-Agent", "PostmanRuntime/7.28.4")
                        .content(destinoNuloOuVazio)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }

    @Test
    @Order(5)
    void naoDeveriaCadastrarUmAvisoDeViagemComIdCartaoInvalidoComRetorno404() throws Exception {

        String novoAvisoViagem = "{\"destino\" : \"Belem\", \"validoAte\" : \"" + LocalDate.now().plusDays(15) + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri + 50L + "/aviso-viagem")
                        .header("User-Agent", "PostmanRuntime/7.28.4")
                        .content(novoAvisoViagem)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound());
    }
}
