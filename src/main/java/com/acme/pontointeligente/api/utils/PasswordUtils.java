package com.acme.pontointeligente.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by Ivan on 1/10/2018.
 */
public class PasswordUtils {

    private static final Logger LOG = LoggerFactory.getLogger(PasswordUtils.class);

    private PasswordUtils() {
    }

    public static String gerarBCrypt(String senha) {
        if (senha == null) {
            return null;
        }
        LOG.info("Generando hash con BCrypt.");
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(senha);
    }

}
