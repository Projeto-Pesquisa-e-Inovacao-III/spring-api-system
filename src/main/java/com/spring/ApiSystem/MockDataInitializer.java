package com.spring.ApiSystem;

import com.spring.ApiSystem.domain.admin.AdminUserInitializer;
import com.spring.ApiSystem.domain.agendamento.Agendamento;
import com.spring.ApiSystem.domain.agendamento.AgendamentoRepository;
import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.aluno.AlunoRepository;
import com.spring.ApiSystem.domain.aluno.vo.Cpf;
import com.spring.ApiSystem.domain.anamnese.Anamnese;
import com.spring.ApiSystem.domain.anamnese.AnamneseRepository;
import com.spring.ApiSystem.domain.anamnese.enums.NivelDeAtividadeEnum;
import com.spring.ApiSystem.domain.beneficio.Beneficio;
import com.spring.ApiSystem.domain.cep.CEP;
import com.spring.ApiSystem.domain.cep.CepRepository;
import com.spring.ApiSystem.domain.endereco.Endereco;
import com.spring.ApiSystem.domain.endereco.EnderecoRepository;
import com.spring.ApiSystem.domain.personal.Personal;
import com.spring.ApiSystem.domain.produtocontratado.ProdutoContratado;
import com.spring.ApiSystem.domain.produtocontratado.ProdutoContratadoRepository;
import com.spring.ApiSystem.domain.produtoexibicao.ProdutoExibicao;
import com.spring.ApiSystem.domain.produtoexibicao.ProdutoExibicaoRepository;
import com.spring.ApiSystem.domain.produtoexibicao.enums.ProdutoExibicaoStatus;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoProduto;
import com.spring.ApiSystem.domain.resumoAgendamento.ResumoAgendamento;
import com.spring.ApiSystem.domain.resumoAgendamento.ResumoAgendamentoRepository;
import com.spring.ApiSystem.domain.resumoAgendamento.enums.GrupoMuscular;
import com.spring.ApiSystem.domain.telefone.Telefone;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.UsuarioRepository;
import com.spring.ApiSystem.domain.usuario.UsuarioService;
import com.spring.ApiSystem.domain.usuario.enums.Role;
import com.spring.ApiSystem.shared.enums.DiaSemana;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(name = "app.mock.seed.enabled", havingValue = "true")
@DependsOn("adminUserInitializer")
@Order(3)
public class MockDataInitializer implements CommandLineRunner {
    private final ProdutoExibicaoRepository produtoExibicaoRepository;
    private final AlunoRepository alunoRepository;
    private final ProdutoContratadoRepository produtoContratadoRepository;
    private final AnamneseRepository anamneseRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EnderecoRepository enderecoRepository;
    private final CepRepository cepRepository;
    private final ResumoAgendamentoRepository resumoAgendamentoRepository;
    @Value("${init.email}")
    private String adminEmail;

    public MockDataInitializer(
            ProdutoExibicaoRepository produtoExibicaoRepository,
            AlunoRepository alunoRepository,
            ProdutoContratadoRepository produtoContratadoRepository,
            AnamneseRepository anamneseRepository,
            AgendamentoRepository agendamentoRepository,
            UsuarioRepository usuarioRepository,
            EnderecoRepository enderecoRepository,
            CepRepository cepRepository,
            ResumoAgendamentoRepository resumoAgendamentoRepository) {
        this.produtoExibicaoRepository = produtoExibicaoRepository;
        this.alunoRepository = alunoRepository;
        this.produtoContratadoRepository = produtoContratadoRepository;
        this.anamneseRepository = anamneseRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.enderecoRepository = enderecoRepository;
        this.cepRepository = cepRepository;
        this.resumoAgendamentoRepository = resumoAgendamentoRepository;
    }

    private static final List<String> MOCK_CPFS = List.of(
            "75918974032",
            "75813996052",
            "06025066035",
            "91910481092");

    private static final List<String> MOCK_SEXOS = List.of(
            "M",
            "M",
            "F",
            "M");

    private static final List<String> MOCK_NOMES = List.of(
            "Robson Macedo",
            "Luiz Gonçalves",
            "Amanda Coelho",
            "Douglas Cavalcante");

    private static final List<Telefone> MOCK_TELEFONE = List.of(
            new Telefone(null, "55", "11", "32920050", null),
            new Telefone(null, "55", "11", "20840447", null),
            new Telefone(null, "55", "11", "22096486", null),
            new Telefone(null, "55", "11", "28763229", null));

    private static final List<String> MOCK_MENSAGENS_RESUMO = List.of(
            "O treino de hoje foi excelente! O aluno demonstrou uma evolução surpreendente na carga, superando todas as expectativas. A técnica foi mantida impecável ao longo de todas as séries de força intensa.",
            "Foco em hipertrofia total! A execução dos movimentos foi muito boa, com ótima amplitude e cadência perfeita. O recrutamento muscular foi notável, já exigindo bastante do aluno até a falha concêntrica.",
            "O aluno relatou um leve desconforto articular no ombro logo no início do treino. Fizemos as adaptações necessárias, trocando alguns exercícios para garantir a segurança sem perder o grande estímulo.",
            "Treino de resistência física concluído com extremo sucesso! A energia do aluno estava lá em cima durante toda a sessão. O condicionamento aeróbico apresentou melhoras muito significativas agora mesmo.",
            "Foi um dia de treinar membros inferiores pesado! Apesar da intensidade extremamente alta, o aluno completou todas as séries com bravura, sempre garantindo máxima hipertrofia para as suas estruturas.",
            "Realizamos um treino voltado inteiramente para recuperação ativa e alongamentos globais. Foi ótimo para soltar as tensões musculares, aprimorar a mobilidade articular e preparar para a próxima fase.",
            "A técnica de agachamento livre hoje demonstrou uma melhoria notável em relação à nossa última semana. O aluno já consegue alcançar mais profundidade sem curvar a lombar, ativando bem seus glúteos.",
            "Focamos bastante em toda a região do core e na estabilidade pélvica. O aluno apresentou um controle motor impecável durante todas as pranchas e rotações. Sem dúvida, um ótimo e belíssimo desempenho."
    );

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (agendamentoRepository.count() > 0)
            return;

        // 1. Get Init Admin's Personal Profile
        Usuario adminUser = usuarioRepository.findByEmail(adminEmail).orElseThrow();
        Personal personal = adminUser.getPersonal();

        if (!usuarioRepository.existsById(2L)) {
            // 2. Create 3 Produto Exibicao (2 Pacote, 1 Adicional)
            ProdutoExibicao p1 = new ProdutoExibicao(null, "Pacote Mensal", "1 Mês", "Descricao", new ArrayList<>(),
                    100.0, "MENSAL", ProdutoExibicaoStatus.ATIVO, TipoProduto.PACOTE, LocalDateTime.now(),
                    TipoAula.PRESENCIAL, 12, 1);
            ProdutoExibicao p2 = new ProdutoExibicao(null, "Pacote Anual", "1 Ano", "Descricao", new ArrayList<>(),
                    1000.0, "ANUAL", ProdutoExibicaoStatus.ATIVO, TipoProduto.PACOTE, LocalDateTime.now(),
                    TipoAula.PRESENCIAL, 144, 12);

            // Benefícios p1 - Pacote Mensal (12 aulas/mês)
            Beneficio b1 = new Beneficio(null, "12 aulas presenciais por mês", p1);
            Beneficio b2 = new Beneficio(null, "Avaliação física mensal", p1);
            Beneficio b3 = new Beneficio(null, "Planilha de treino personalizada", p1);
            Beneficio b4 = new Beneficio(null, "Suporte via WhatsApp", p1);
            p1.getBeneficios().addAll(List.of(b1, b2, b3, b4));

            // Benefícios p2 - Pacote Anual (144 aulas/ano → 12/mês)
            Beneficio b5 = new Beneficio(null, "144 aulas presenciais no ano", p2);
            Beneficio b6 = new Beneficio(null, "Avaliação física trimestral", p2);
            Beneficio b7 = new Beneficio(null, "Planilha de treino atualizada mensalmente", p2);
            Beneficio b8 = new Beneficio(null, "Suporte prioritário via WhatsApp", p2);
            Beneficio b9 = new Beneficio(null, "Desconto de 17% em relação ao mensal", p2);
            p2.getBeneficios().addAll(List.of(b5, b6, b7, b8, b9));

            ProdutoExibicao p3 = new ProdutoExibicao(null, "Aula Adicional", "Avulso", "Descricao", new ArrayList<>(),
                    20.0, "AVULSO", ProdutoExibicaoStatus.ATIVO, TipoProduto.ADICIONAL, LocalDateTime.now(),
                    TipoAula.PRESENCIAL, 1, 0);
            produtoExibicaoRepository.saveAll(List.of(p1, p2, p3));

            // Create CEP & Endereco
            CEP cep = new CEP("01310100", "Avenida Paulista", "Bela Vista", "São Paulo", "SP");
            cepRepository.save(cep);
            Endereco endereco = new Endereco(null, "1000", "Apto 12", "", "CASA", adminUser, cep, true);
            enderecoRepository.save(endereco);

            // 3. Create 4 Alunos with Anamnese, Produto Contratado (past), and Agendamentos
            for (int i = 0; i < 4; i++) {
                // Save Usuario first so Hibernate assigns the id before Aluno (@MapsId) enters
                // the session
                Usuario u = new Usuario();
                u.setNome(MOCK_NOMES.get(i));
                u.setEmail(MOCK_NOMES.get(i).toLowerCase().replaceAll("\\s", "") + "@teste.com");
                u.setSenha("hashfake");
                u.setAtivo(true);
                u.setDataNascimento(LocalDate.of(1990, 1, 1));
                u.setSexo(MOCK_SEXOS.get(i));
                u.addRole(Role.ALUNO);
                Telefone telefone = MOCK_TELEFONE.get(i);
                telefone.setUsuario(u);
                u.setTelefones(List.of(telefone));
                Usuario savedU = usuarioRepository.save(u);

                Cpf cpf = new Cpf(MOCK_CPFS.get(i));
                Aluno aluno = new Aluno(null, savedU, cpf, true, null, true);

                Anamnese anamnese = new Anamnese(null, 180d, 80.0, "Emagrecer", "Rotina", new ArrayList<>(),
                        NivelDeAtividadeEnum.ATIVO, null, aluno);
                aluno.setAnamnese(anamnese);
                alunoRepository.save(aluno);

                ProdutoContratado pc = new ProdutoContratado(null, true, LocalDate.now().minusMonths(i),
                        LocalDate.now().minusDays(i), 10, aluno, i % 2 == 0 ? p1 : p2);
                produtoContratadoRepository.save(pc);

                int quantidadeAgendamentos = (i == 0) ? 20 : (i == 1 ? 10 : 1);
                List<Agendamento> agendamentosCriados = new ArrayList<>();

                for (int j = 0; j < quantidadeAgendamentos; j++) {
                    LocalDateTime pastDate = LocalDateTime.now().minusDays(i + j + 1);

                    Agendamento ag1 = new Agendamento(null, pastDate, "Agendamento " + i + "-" + j, endereco, aluno, personal, pc);

                    ag1.setDataFim(pastDate.plusHours(1));
                    ag1.setDiaSemana(DiaSemana.fromDayOfWeek(pastDate.getDayOfWeek()));
                    
                    if (i == 0 && j < 7) {
                        ag1.aprovado();
                        ag1.pendentePersonalConcluir();
                    } else if (j % 4 == 0) {
                        ag1.aprovado();
                        ag1.pendentePersonalConcluir();
                    } else if (j % 4 == 1) {
                        ag1.aprovado();
                        ag1.pendentePersonalConcluir();
                        ag1.ausenciaCliente();
                    } else if (j % 4 == 2) {
                        ag1.canceladoCliente();
                    } else {
                        ag1.canceladoPersonal();
                    }
                    agendamentosCriados.add(agendamentoRepository.save(ag1));
                }

                if (i < 2) {
                    int resumoIndex = 0;
                    int mantidosPendentes = 0;
                    for (Agendamento agendamentoSalvo : agendamentosCriados) {
                        if (agendamentoSalvo.getStatus() == AgendamentoStatus.PENDENTE_PERSONAL_CONCLUIR) {
                            if (i == 0 && mantidosPendentes < 7) {
                                mantidosPendentes++;
                                continue;
                            }
                            String baseMsg = MOCK_MENSAGENS_RESUMO.get(resumoIndex % MOCK_MENSAGENS_RESUMO.size());
                            StringBuilder sb = new StringBuilder(baseMsg);
                            while (sb.length() < 200) {
                                sb.append(".");
                            }
                            String mensagemFinal = sb.substring(0, 200);

                            ResumoAgendamento resumo = new ResumoAgendamento(null, aluno, personal, agendamentoSalvo,
                                    mensagemFinal, List.of(GrupoMuscular.PEITO, GrupoMuscular.COSTAS));
                            resumoAgendamentoRepository.save(resumo);
                            resumoIndex++;
                            
                            agendamentoSalvo.concluido();
                            agendamentoRepository.save(agendamentoSalvo);
                        }
                    }
                }

                for (int j = 1; j <= 5; j++) {
                    LocalDateTime futureDate = LocalDateTime.now().plusDays(i * 5L + j * 2L);
                    Agendamento agFuture = new Agendamento(null, futureDate, "Agendamento Futuro " + i + "-" + j, endereco, aluno, personal, pc);
                    agFuture.setDataFim(futureDate.plusHours(1));
                    agFuture.setDiaSemana(DiaSemana.fromDayOfWeek(futureDate.getDayOfWeek()));

                    if (j % 3 == 0) {
                        agFuture.aprovado();
                    } else if (j % 3 == 1) {
                        agFuture.pendenteClienteAprovacao();
                    }
                    // O construtor já define PENDENTE_PERSONAL_APROVACAO por padrão para o else
                    agendamentoRepository.save(agFuture);
                }
            }
        }
    }
}
