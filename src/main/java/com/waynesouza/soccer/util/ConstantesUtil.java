package com.waynesouza.soccer.util;

public final class ConstantesUtil {

    public static final String ERRO_HORARIO_PARTIDA = "Não é permitido a realizacão de partidas antes das 08:00 e " +
            "nem após às 22:00";
    public static final String ERRO_DISPONIBILIDADE = "Não é permitido salvar partidas em que o intervalo entre elas " +
            "seja menor que 48 horas ou quando o estádio já recebeu uma partida neste dia";
    public static final String ERRO_PARTIDA_FUTURO = "Não é permitido salvar partidas em datas futuras";
    public static final String ERRO_PARTIDA_NAO_ENCONTRADA = "A partida não foi encontrada";

}
