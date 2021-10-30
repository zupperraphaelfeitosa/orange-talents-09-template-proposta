package br.com.zup.raphaelfeitosa.proposta.bloqueio;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BloqueioRepository extends JpaRepository<Bloqueio, Long> {

    Optional<Bloqueio> findByNumero(String numero);
}
