package br.com.zup.raphaelfeitosa.proposta.carteira;

import br.com.zup.raphaelfeitosa.proposta.cartao.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarteiraRepository extends JpaRepository<Carteira, Long> {
    Optional<Carteira> findByCartaoAndCarteira(Cartao cartao, TipoCarteira carteira);
}
