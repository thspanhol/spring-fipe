package com.spring.fipe.service;

import java.util.List;

public interface IConverteDados {

    <T> T converteDados(String json, Class<T> classe);

    <T> List<T> converteLista(String json, Class<T> classe);
}
