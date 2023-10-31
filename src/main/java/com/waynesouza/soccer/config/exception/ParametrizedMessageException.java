package com.waynesouza.soccer.config.exception;

import lombok.Getter;

@Getter
public class ParametrizedMessageException extends RuntimeException {

    private final String messageName;

    public ParametrizedMessageException(String messageName) {
        super(messageName);
        this.messageName = messageName;
    }

}
