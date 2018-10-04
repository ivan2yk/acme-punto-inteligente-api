package com.acme.pontointeligente.api.security;

import com.acme.pontointeligente.api.entities.Funcionario;
import com.acme.pontointeligente.api.enums.PerfilEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 3/10/2018.
 */
public class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(Funcionario funcionario) {
        return new JwtUser(funcionario.getId(),
                funcionario.getEmail(),
                funcionario.getSenha(),
                mapToGrantedAuthorities(funcionario.getPerfil()));
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(PerfilEnum perfilEnum) {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(perfilEnum.toString()));
        return authorities;
    }

}
