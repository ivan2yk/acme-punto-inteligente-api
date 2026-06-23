package com.acme.pontointeligente.api.utils;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by Ivan on 2/10/2018.
 */
public class PasswordUtilsTests {

    private static final String SENHA = "123456";
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Test
    public void testSenhaNula() {
        assertNull(PasswordUtils.gerarBCrypt(null));
    }

    @Test
    public void testGenerarHashSenha() {
        String hash = PasswordUtils.gerarBCrypt(SENHA);
        assertTrue(bCryptPasswordEncoder.matches(SENHA, hash));
    }

}
