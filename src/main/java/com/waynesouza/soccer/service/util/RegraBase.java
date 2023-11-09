package com.waynesouza.soccer.service.util;

import com.waynesouza.soccer.config.exception.ParametrizedMessageException;

public interface RegraBase {

    default void verificarRegra(Boolean regra, String mensagem) {
        if (regra) {
            throw new ParametrizedMessageException(mensagem);
        }
    }

}
