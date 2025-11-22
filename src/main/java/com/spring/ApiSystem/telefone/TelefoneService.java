package com.spring.ApiSystem.telefone;

import com.spring.ApiSystem.telefone.dto.request.ReqAtualizarTelefoneDTO;
import com.spring.ApiSystem.telefone.dto.request.ReqCadastrarTelefoneDTO;
import com.spring.ApiSystem.telefone.dto.response.ResAtualizarTelefoneDTO;
import com.spring.ApiSystem.telefone.dto.response.ResCadastrarTelefoneDTO;
import com.spring.ApiSystem.telefone.dto.response.ResListarTelefonesPorIdDoUsuario;
import com.spring.ApiSystem.telefone.exception.TelefoneDeveTerUmCadastroException;
import com.spring.ApiSystem.telefone.exception.TelefoneDuplicadoException;
import com.spring.ApiSystem.telefone.exception.TelefoneNaoEncontradoException;
import com.spring.ApiSystem.telefone.mapper.TelefoneMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelefoneService {
    private final TelefoneRepository telefoneRepository;
    private final TelefoneMapper telefoneMapper;

    public TelefoneService(TelefoneRepository telefoneRepository, TelefoneMapper telefoneMapper) {
        this.telefoneRepository = telefoneRepository;
        this.telefoneMapper = telefoneMapper;
    }

    public ResCadastrarTelefoneDTO cadastrarTelefone(ReqCadastrarTelefoneDTO telefoneDTO) {
        validarTelefoneDuplicado(telefoneDTO.ddd(), telefoneDTO.numero());

        Telefone telefone = telefoneMapper.toEntity(telefoneDTO);
        telefone = telefoneRepository.save(telefone);

        return telefoneMapper.toDtoCasdastrarTelefone(telefone);
    }

    public ResAtualizarTelefoneDTO atualizarTelefone(ReqAtualizarTelefoneDTO telefoneDTO) {
        buscarTelefonePorId(telefoneDTO.id());

        Telefone telefone = telefoneMapper.toEntity(telefoneDTO);
        telefone = telefoneRepository.save(telefone);

        return telefoneMapper.toDtoAtualizarTelefone(telefone);
    }

    public void removerTelefone(Long idTelefone) {
        Telefone telefone = buscarTelefonePorId(idTelefone);
//        validarUnicoTelefoneDoUsuario(telefone.getUsuario().getId());

        telefoneRepository.deleteById(idTelefone);
    }

    public List<ResListarTelefonesPorIdDoUsuario> listarTelefonesPorIdDoUsuario(Long idDoUsuario) {
        return telefoneMapper.toDtoListarTelefonesPorIdDoUsuario(
                telefoneRepository.findTelefoneByUsuario_Id(idDoUsuario)
        );
    }

    private Telefone buscarTelefonePorId(Long idTelefone) {
        return telefoneRepository.findById(idTelefone)
                .orElseThrow(TelefoneNaoEncontradoException::new);
    }

    private void validarTelefoneDuplicado(String ddd, String numero) {
        if (telefoneRepository.findTelefoneByDddAndNumero(ddd, numero).isPresent())
            throw new TelefoneDuplicadoException();
    }

    private void validarUnicoTelefoneDoUsuario(Long usuarioId) {
        if (listarTelefonesPorIdDoUsuario(usuarioId).size() == 1) {
            throw new TelefoneDeveTerUmCadastroException();
        }
    }
}
