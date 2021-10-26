package br.com.zup.raphaelfeitosa.proposta.cartao;

import br.com.zup.raphaelfeitosa.proposta.cartao.feign.ServicoCartaoApi;
import br.com.zup.raphaelfeitosa.proposta.proposta.Proposta;
import br.com.zup.raphaelfeitosa.proposta.proposta.PropostaRepository;
import br.com.zup.raphaelfeitosa.proposta.proposta.StatusProposta;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ActiveProfiles("test")
public class AssociaCartaoAPropostaElegivelTest {

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private AssociaCartaoAPropostaElegivel associaCartaoAPropostaElegivel;

    @MockBean
    private ServicoCartaoApi servicoCartaoApi;

    @Test
    void deveriaAssociarCartaoAPropostaElegivel() {

        propostaRepository.deleteAll();
        cartaoRepository.deleteAll();

        Proposta propostaElegivelSemCartao = new Proposta(
                "John Doe",
                "johndoe@gmail.com",
                "99985090098",
                BigDecimal.valueOf(1500), "Travessa quinze");

        propostaElegivelSemCartao.adicionaRestricao(StatusProposta.ELEGIVEL);
        propostaRepository.save(propostaElegivelSemCartao);

        Proposta propostaNaoElegivel = new Proposta(
                "Maria Doe",
                "mariadoe@gmail.com",
                "38783121056",
                BigDecimal.valueOf(3000), "Rua dezoito");

        propostaNaoElegivel.adicionaRestricao(StatusProposta.NAO_ELEGIVEL);
        propostaRepository.save(propostaNaoElegivel);

        RetornoCartaoResponse retornoCartaoResponse = new RetornoCartaoResponse(
                "5812-4804-7265-6806",
                LocalDateTime.now(),
                "John Doe",
                5000);

        Mockito.when(servicoCartaoApi.solicitaCartao(Mockito.any())).thenReturn(retornoCartaoResponse);

        associaCartaoAPropostaElegivel.associaCartaoAPropostaElegivel();

        assertTrue(propostaRepository.findByStatusAndCartao(StatusProposta.ELEGIVEL, null).isEmpty());
        assertTrue(cartaoRepository.findByNumero("5812-4804-7265-6806").isPresent());
        assertTrue(propostaRepository.findByDocument("38783121056").isPresent());
    }
}
