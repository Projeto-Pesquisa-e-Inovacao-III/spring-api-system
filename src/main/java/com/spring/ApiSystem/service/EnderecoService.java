package com.spring.ApiSystem.service;

import com.spring.ApiSystem.dto.endereco.request.EdicaoEnderecoDTO;
import com.spring.ApiSystem.dto.endereco.request.EnderecoDTO;
import com.spring.ApiSystem.mapper.EnderecoMapper;
import com.spring.ApiSystem.model.Endereco;
import com.spring.ApiSystem.model.Usuario;
import com.spring.ApiSystem.repository.EnderecoRepository;
import com.spring.ApiSystem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;
    private final UserRepository userRepository;
    private final EnderecoMapper enderecoMapper;
    private final ViaCepService viaCepService;

    public EnderecoService(EnderecoRepository enderecoRepository,
                           EnderecoMapper enderecoMapper,
                           UserRepository userRepository,
                           ViaCepService viaCepService) {
        this.enderecoRepository = enderecoRepository;
        this.userRepository = userRepository;
        this.enderecoMapper = enderecoMapper;
        this.viaCepService = viaCepService;
    }

    public Endereco cadastrarEndereco(EdicaoEnderecoDTO edicaoEnderecoDTO, String email){
        Optional<Usuario> usuarioEncontrado = userRepository.findByEmail(email);

        if(usuarioEncontrado.isPresent()){
            EnderecoDTO enderecoDTO = viaCepService.verificarCep(edicaoEnderecoDTO.getCep());
            if(enderecoDTO != null){
                enderecoMapper.atualizarEnderecoDtoFromDto(edicaoEnderecoDTO, enderecoDTO);
                Endereco endereco = enderecoMapper.toEntity(enderecoDTO);
                endereco.setUsuario(usuarioEncontrado.get());
                endereco.setData_criacao(LocalDateTime.now());
                return enderecoRepository.save(endereco);
            }
        }

        return null;
    }

    public List<Endereco> listarEnderecos(String email){
        Optional<Usuario> usuarioEncontrado = userRepository.findByEmail(email);

        if(usuarioEncontrado.isPresent()){
            return enderecoRepository.findByUsuario(usuarioEncontrado.get());
        }

        return null;
    }

    public Endereco atualizarEndereco(Long id , EdicaoEnderecoDTO edicaoEnderecoDTO, String email){
        Optional<Usuario> usuarioEncontrado = userRepository.findByEmail(email);
        Optional<Endereco> enderecoEncontrado = enderecoRepository.findByIdAndUsuario(id, usuarioEncontrado.get());

        if(enderecoEncontrado.isPresent()){
            EnderecoDTO enderecoDTO = viaCepService.verificarCep(edicaoEnderecoDTO.getCep());
            if(enderecoDTO != null){
                enderecoMapper.atualizarEnderecoDtoFromDto(edicaoEnderecoDTO, enderecoDTO);
                enderecoMapper.atualizarEnderecoFromDto(enderecoDTO, enderecoEncontrado.get());
                enderecoEncontrado.get().setData_atualizacao(LocalDateTime.now());
                return enderecoRepository.save(enderecoEncontrado.get());
            }
        }

        return null;
    }

    public Boolean removerEndereco(Long id, String email){
        Optional<Usuario> usuarioEncontrado = userRepository.findByEmail(email);
        Optional<Endereco> enderecoEncontrado = enderecoRepository.findByIdAndUsuario(id, usuarioEncontrado.get());

        if(enderecoEncontrado.isPresent()){
            enderecoRepository.deleteById(id);
            return true;
        }

        return false;
    }
}
