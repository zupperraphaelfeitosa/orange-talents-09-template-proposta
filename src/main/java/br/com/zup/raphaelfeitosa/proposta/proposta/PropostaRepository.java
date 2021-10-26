package br.com.zup.raphaelfeitosa.proposta.proposta;

import br.com.zup.raphaelfeitosa.proposta.cartao.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {
    Optional<Proposta> findByEmail(String email);

    Optional<Proposta> findByDocument(String document);

    Collection<Proposta> findByStatusAndCartao(StatusProposta status, Cartao carta);
}
