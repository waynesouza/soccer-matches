package com.waynesouza.soccer.repository;

import com.waynesouza.soccer.domain.Partida;
import com.waynesouza.soccer.domain.dto.FreguesDTO;
import com.waynesouza.soccer.domain.dto.PartidaDTO;
import com.waynesouza.soccer.domain.dto.RetrospectoClubeDTO;
import com.waynesouza.soccer.domain.dto.RetrospectoConfrontoDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, String> {

    @Query("select count(p) > 0 from Partida p " +
            "where (p.estadio = :estadio and function('date', p.dataHora) = :data)")
    Boolean verificarEstadio(@Param("estadio") String estadio,
                             @Param("data") LocalDate data);

    @Query("select count(p) > 0 from Partida p " +
            "where (" +
                ":#{#dto.id} is null or p.id != :#{#dto.id}" +
            ") " +
            "and (" +
                "p.timeMandante in (:#{#dto.timeMandante}, :#{#dto.timeVisitante}) or " +
                "p.timeVisitante in (:#{#dto.timeMandante}, :#{#dto.timeVisitante})" +
            ") " +
            "and (" +
                "(p.dataHora between :dataLimiteInferior and :dataLimiteSuperior)" +
            ")")
    Boolean verificarDisponibilidade(@Param("dto") PartidaDTO dto,
                                     @Param("dataLimiteInferior")LocalDateTime dataLimiteInferior,
                                     @Param("dataLimiteSuperior")LocalDateTime dataLimiteSuperior);

    @Query("select p from Partida p " +
            "where p.quantidadeGolMandante - p.quantidadeGolVisitante >= 3 or " +
            "p.quantidadeGolVisitante - p.quantidadeGolMandante >= 3")
    List<Partida> listarGoleadas();

    @Query("select p from Partida p " +
            "where p.quantidadeGolMandante = 0 and p.quantidadeGolVisitante = 0")
    List<Partida> listarPartidasSemGols();

    @Query("select p from Partida p " +
            "where (" +
                "(:filtro = 'MANDANTE' and p.timeMandante = :time) or " +
                "(:filtro = 'VISITANTE' and p.timeVisitante = :time)" +
            ") " +
            "or (" +
                "(:filtro is null) and " +
                "(p.timeMandante = :time or p.timeVisitante = :time)" +
            ")")
    List<Partida> listarPartidasPorTime(@Param("time") String time,
                                        @Param("filtro") String filtro);

    List<Partida> findAllByEstadio(String estadio);

    @Query("select new com.waynesouza.soccer.domain.dto.RetrospectoClubeDTO(" +
                "sum(case " +
                    "when p.timeMandante = :time and p.quantidadeGolMandante - p.quantidadeGolVisitante <= 0 then 0 " +
                    "when p.timeVisitante = :time and p.quantidadeGolVisitante - p.quantidadeGolMandante <= 0 then 0 " +
                    "else 1 " +
                "end), " +
                "sum(case " +
                    "when (p.timeMandante = :time or p.timeVisitante = :time) and p.quantidadeGolMandante - p.quantidadeGolVisitante = 0 then 1 " +
                    "else 0 " +
                "end), " +
                "sum(case " +
                    "when p.timeMandante = :time and p.quantidadeGolMandante - p.quantidadeGolVisitante >= 0 then 0 " +
                    "when p.timeVisitante = :time and p.quantidadeGolVisitante - p.quantidadeGolMandante >= 0 then 0 " +
                    "else 1 " +
                "end), " +
                "sum(case " +
                    "when p.timeMandante = :time then p.quantidadeGolMandante " +
                    "when p.timeVisitante = :time then p.quantidadeGolVisitante " +
                    "else 0 " +
                "end), " +
                "sum(case " +
                    "when p.timeMandante = :time then p.quantidadeGolVisitante " +
                    "when p.timeVisitante = :time then p.quantidadeGolMandante " +
                    "else 0 " +
                "end)" +
            ") " +
            "from Partida p " +
            "where (" +
                "(:filtro = 'MANDANTE' and p.timeMandante = :time) or " +
                "(:filtro = 'VISITANTE' and p.timeVisitante = :time)" +
            ") or " +
            "(" +
                ":filtro is null and " +
                "(p.timeMandante = :time or p.timeVisitante = :time)" +
            ")")
    RetrospectoClubeDTO buscarRetrospectoTime(@Param("time") String time,
                                              @Param("filtro") String filtro);

    @Query("select new com.waynesouza.soccer.domain.dto.RetrospectoConfrontoDTO(" +
                "sum(case " +
                    "when p.timeMandante = :primeiroTime and p.quantidadeGolMandante - p.quantidadeGolVisitante <= 0 then 0 " +
                    "when p.timeVisitante = :primeiroTime and p.quantidadeGolVisitante - p.quantidadeGolMandante <= 0 then 0 " +
                    "else 1 " +
                "end), " +
                "sum(case " +
                    "when p.timeMandante = :segundoTime and p.quantidadeGolMandante - p.quantidadeGolVisitante <= 0 then 0 " +
                    "when p.timeVisitante = :segundoTime and p.quantidadeGolVisitante - p.quantidadeGolMandante <= 0 then 0 " +
                    "else 1 " +
                "end), " +
                "sum(case when p.quantidadeGolMandante - p.quantidadeGolVisitante = 0 then 1 else 0 end), " +
                "sum(case " +
                    "when p.timeMandante = :primeiroTime then p.quantidadeGolMandante " +
                    "when p.timeVisitante = :primeiroTime then p.quantidadeGolVisitante " +
                    "else 0 " +
                "end), " +
                "sum(case " +
                    "when p.timeMandante = :segundoTime then p.quantidadeGolMandante " +
                    "when p.timeVisitante = :segundoTime then p.quantidadeGolVisitante " +
                    "else 0 " +
                "end)" +
            ") " +
            "from Partida p " +
            "where (" +
                "(:filtro = 'PRIMEIRO' and p.timeMandante = :primeiroTime and p.timeVisitante = :segundoTime) or " +
                "(:filtro = 'SEGUNDO' and p.timeMandante = :segundoTime and p.timeVisitante = :primeiroTime)" +
            ") or " +
            "(" +
                ":filtro is null and " +
                "((p.timeMandante = :primeiroTime and p.timeVisitante = :segundoTime) or " +
                "(p.timeMandante = :segundoTime and p.timeVisitante = :primeiroTime))" +
            ")")
    RetrospectoConfrontoDTO buscarRetrospectoConfronto(@Param("primeiroTime") String primeiroTime,
                                                       @Param("segundoTime") String segundoTime,
                                                       @Param("filtro") String filtro);

    @Query("select new com.waynesouza.soccer.domain.dto.FreguesDTO(" +
            "case when p.timeMandante = :time then p.timeVisitante else p.timeMandante end, " +
            "count(p.id), " +
            "sum(case " +
                "when p.timeMandante = :time and p.quantidadeGolMandante - p.quantidadeGolVisitante <= 0 then 0 " +
                "when p.timeVisitante = :time and p.quantidadeGolVisitante - p.quantidadeGolMandante <= 0 then 0 " +
                "else 1 " +
            "end), " +
            "sum(case " +
                "when p.timeMandante = :time and p.quantidadeGolMandante - p.quantidadeGolVisitante >= 0 then 0 " +
                "when p.timeVisitante = :time and p.quantidadeGolVisitante - p.quantidadeGolMandante >= 0 then 0 " +
                "else 1 " +
            "end)) " +
            "from Partida p " +
            "where (p.timeMandante = :time or p.timeVisitante = :time) " +
            "group by case when p.timeMandante = :time then p.timeVisitante else p.timeMandante end " +
            "having " +
                "sum(case " +
                    "when p.timeMandante = :time and p.quantidadeGolMandante - p.quantidadeGolVisitante <= 0 then 0 " +
                    "when p.timeVisitante = :time and p.quantidadeGolVisitante - p.quantidadeGolMandante <= 0 then 0 " +
                    "else 1 " +
                "end) - " +
                "sum(case " +
                    "when p.timeMandante = :time and p.quantidadeGolMandante - p.quantidadeGolVisitante >= 0 then 0 " +
                    "when p.timeVisitante = :time and p.quantidadeGolVisitante - p.quantidadeGolMandante >= 0 then 0 " +
                    "else 1 " +
                "end) > 0 " +
            "order by " +
                "sum(case " +
                    "when p.timeMandante = :time and p.quantidadeGolMandante - p.quantidadeGolVisitante <= 0 then 0 " +
                    "when p.timeVisitante = :time and p.quantidadeGolVisitante - p.quantidadeGolMandante <= 0 then 0 " +
                    "else 1 " +
                "end) - " +
                "sum(case " +
                    "when p.timeMandante = :time and p.quantidadeGolMandante - p.quantidadeGolVisitante >= 0 then 0 " +
                    "when p.timeVisitante = :time and p.quantidadeGolVisitante - p.quantidadeGolMandante >= 0 then 0 " +
                    "else 1 " +
                "end) " +
            "desc")
    List<FreguesDTO> listarFregueses(@Param("time") String time,
                                     Pageable pageable);

}
