package aep5s.zonaverde;

import aep5s.zonaverde.domain.entity.*;
import aep5s.zonaverde.domain.enums.*;
import aep5s.zonaverde.domain.state.*;
import aep5s.zonaverde.dto.*;
import aep5s.zonaverde.facade.AgendamentoFacade;
import aep5s.zonaverde.repository.*;
import aep5s.zonaverde.service.ReporteService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ZonaverdeApplicationTests {

	@Autowired AgendamentoFacade agendamentoFacade;
	@Autowired ReporteService reporteService;
	@Autowired EspacoPublicoRepository espacoRepo;
	@Autowired UsuarioRepository usuarioRepo;
	@Autowired ReporteRepository reporteRepo;

	private static Long espacoId;
	private static Long usuarioId;

	@Test
	@Order(1)
	void deveAvancarEstadoDePropostoParaEmAnalise() {
		EstadoEvento estado = new Proposto();
		EstadoEvento proximo = estado.proximoEstado();
		assertEquals("Em Analise", proximo.getNome());
	}

	@Test
	@Order(2)
	void deveLancarExcecaoAoAvancarEstadoConcluido() {
		EstadoEvento estado = new Concluido();
		assertThrows(IllegalStateException.class, estado::proximoEstado);
	}

	@Test
	@Order(3)
	void deveLancarExcecaoAoAvancarEstadoCancelado() {
		EstadoEvento estado = new Cancelado();
		assertThrows(IllegalStateException.class, estado::proximoEstado);
	}

	@Test
	@Order(4)
	void deveCalcularPrioridadeCorretamente() {
		Reporte r1 = new Reporte();
		r1.setConfirmacoes(5);
		r1.setDataAbertura(LocalDate.now().minusDays(3));

		Reporte r2 = new Reporte();
		r2.setConfirmacoes(2);
		r2.setDataAbertura(LocalDate.now().minusDays(10));

		// r1: 5*2 + 3 = 13 | r2: 2*2 + 10 = 14
		assertTrue(r2.calcularPrioridade() > r1.calcularPrioridade());
	}

	@Test
	@Order(5)
	void deveRejeitarEventoComConflitoDehOrario() {
		// Setup: cria espaço e usuário
		EspacoPublico espaco = new EspacoPublico();
		espaco.setNome("Praça Teste");
		espaco.setEndereco("Rua A");
		espaco.setTipo(TipoEspaco.PRACA);
		espaco.setLatitude(-23.0);
		espaco.setLongitude(-51.0);
		espaco.setCapacidadeEstimada(100);
		espacoId = espacoRepo.save(espaco).getId();

		Cidadao cidadao = new Cidadao();
		cidadao.setNome("João");
		cidadao.setEmail("joao@test.com");
		cidadao.setTipo(TipoUsuario.CIDADAO);
		usuarioId = usuarioRepo.save(cidadao).getId();

		EventoDTO dto1 = new EventoDTO();
		dto1.setNome("Feira de Artesanato");
		dto1.setDescricao("Feira comunitária");
		dto1.setDataHoraInicio(LocalDateTime.of(2026, 7, 10, 9, 0));
		dto1.setDataHoraFim(LocalDateTime.of(2026, 7, 10, 17, 0));
		dto1.setEspacoPublicoId(espacoId);
		dto1.setProponenteId(usuarioId);
		agendamentoFacade.proporEvento(dto1);

		EventoDTO dto2 = new EventoDTO();
		dto2.setNome("Show de Música");
		dto2.setDescricao("Evento musical");
		dto2.setDataHoraInicio(LocalDateTime.of(2026, 7, 10, 14, 0));
		dto2.setDataHoraFim(LocalDateTime.of(2026, 7, 10, 20, 0));
		dto2.setEspacoPublicoId(espacoId);
		dto2.setProponenteId(usuarioId);

		assertThrows(IllegalStateException.class,
				() -> agendamentoFacade.proporEvento(dto2));
	}

	@Test
	@Order(6)
	void deveOrdenarReportesPorPrioridade() {
		if (espacoId == null || usuarioId == null) return;

		EspacoPublico espaco = espacoRepo.findById(espacoId).orElseThrow();
		Usuario autor = usuarioRepo.findById(usuarioId).orElseThrow();

		Reporte r1 = new Reporte();
		r1.setDescricao("Lâmpada queimada");
		r1.setTipo(TipoReporte.ILUMINACAO);
		r1.setDataAbertura(LocalDate.now().minusDays(3));
		r1.setConfirmacoes(5);
		r1.setEspacoPublico(espaco);
		r1.setAutor(autor);
		reporteRepo.save(r1);

		Reporte r2 = new Reporte();
		r2.setDescricao("Mato alto");
		r2.setTipo(TipoReporte.OUTRO);
		r2.setDataAbertura(LocalDate.now().minusDays(10));
		r2.setConfirmacoes(2);
		r2.setEspacoPublico(espaco);
		r2.setAutor(autor);
		reporteRepo.save(r2);

		var lista = reporteService.listarPorPrioridade();
		assertTrue(lista.get(0).getPrioridade() >= lista.get(1).getPrioridade());
	}
}