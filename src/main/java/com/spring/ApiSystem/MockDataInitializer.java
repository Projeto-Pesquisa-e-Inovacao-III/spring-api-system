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
import com.spring.ApiSystem.domain.telefone.Telefone;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.UsuarioRepository;
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
            CepRepository cepRepository) {
        this.produtoExibicaoRepository = produtoExibicaoRepository;
        this.alunoRepository = alunoRepository;
        this.produtoContratadoRepository = produtoContratadoRepository;
        this.anamneseRepository = anamneseRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.enderecoRepository = enderecoRepository;
        this.cepRepository = cepRepository;
    }

    private static final List<String> MOCK_CPFS = List.of(
            "75918974032",
            "75813996052",
            "06025066035",
            "91910481092"
    );

    private static final List<String> MOCK_SEXOS = List.of(
            "M",
            "M",
            "F",
            "M"
    );

    private static final List<String> MOCK_NOMES = List.of(
            "Robson Macedo",
            "Luiz Gonçalves",
            "Amanda Coelho",
            "Douglas Cavalcante"
    );

    private static final List<Telefone> MOCK_TELEFONE = List.of(
            new Telefone(null, "55", "11", "32920050", null),
            new Telefone(null, "55", "11", "20840447", null),
            new Telefone(null, "55", "11", "22096486", null),
            new Telefone(null, "55", "11", "28763229", null)
    );


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (agendamentoRepository.count() > 0) return;

        // 1. Get Init Admin's Personal Profile
        Usuario adminUser = usuarioRepository.findByEmail(adminEmail).orElseThrow();
        Personal personal = adminUser.getPersonal();

        // 2. Create 3 Produto Exibicao (2 Pacote, 1 Adicional)
        ProdutoExibicao p1 = new ProdutoExibicao(null, "Pacote Mensal", "1 Mês", "Descricao", new ArrayList<>(), 100.0, "MENSAL", ProdutoExibicaoStatus.ATIVO, TipoProduto.PACOTE, LocalDateTime.now(), TipoAula.PRESENCIAL, 12, 1);
        ProdutoExibicao p2 = new ProdutoExibicao(null, "Pacote Anual", "1 Ano", "Descricao", new ArrayList<>(), 1000.0, "ANUAL", ProdutoExibicaoStatus.ATIVO, TipoProduto.PACOTE, LocalDateTime.now(), TipoAula.PRESENCIAL, 144, 12);


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

        ProdutoExibicao p3 = new ProdutoExibicao(null, "Aula Adicional", "Avulso", "Descricao", new ArrayList<>(), 20.0, "AVULSO", ProdutoExibicaoStatus.ATIVO, TipoProduto.ADICIONAL, LocalDateTime.now(), TipoAula.PRESENCIAL, 1, 0);
        produtoExibicaoRepository.saveAll(List.of(p1, p2, p3));

        // Create CEP & Endereco
        CEP cep = new CEP("00000000", "Rua", "Bairro", "Cidade", "SP");
        cepRepository.save(cep);
        Endereco endereco = new Endereco(null, "123", "", "", "CASA", adminUser, cep, true);
        enderecoRepository.save(endereco);

        // 3. Create 4 Alunos with Anamnese, Produto Contratado (past), and Agendamentos
        for (int i = 0; i < 4; i++) {
            // Save Usuario first so Hibernate assigns the id before Aluno (@MapsId) enters the session
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

            Anamnese anamnese = new Anamnese(null, 180d, 80.0, "Emagrecer", "Rotina", new ArrayList<>(), NivelDeAtividadeEnum.ATIVO, null, aluno);
            aluno.setAnamnese(anamnese);
            alunoRepository.save(aluno);

            ProdutoContratado pc = new ProdutoContratado(null, true, LocalDate.now().minusMonths(i), LocalDate.now().minusDays(i), 10, aluno, i % 2 == 0 ? p1 : p2);
            produtoContratadoRepository.save(pc);

            LocalDateTime pastDate = LocalDateTime.now().minusDays(i);
            Agendamento ag1 = new Agendamento(null, pastDate, "Agendamento " + i, endereco, aluno, personal, pc);
            ag1.setDataFim(pastDate.plusHours(1));
            ag1.setDiaSemana(DiaSemana.SEGUNDA);
            if (i == 0) {
                ag1.aprovado();
                ag1.pendentePersonalConcluir();
            } else if (i == 1) {
                ag1.aprovado();
                ag1.pendentePersonalConcluir();
                ag1.ausenciaCliente();
            } else if (i == 2) {
                ag1.canceladoCliente();
            } else {
                ag1.canceladoPersonal();
            }
            agendamentoRepository.save(ag1);
        }
    }
}
