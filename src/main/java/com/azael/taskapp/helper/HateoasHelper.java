package com.azael.taskapp.helper;

import org.springframework.aop.support.AopUtils;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class HateoasHelper {

    /**
     * Crea un enlace HATEOAS para un método de un controlador.
     *
     * @param controller El controlador (puede ser un proxy).
     * @param methodName El nombre del método.
     * @param rel        El nombre de la relación (rel) del enlace.
     * @param args       Los argumentos que recibe el método.
     * @return Un objeto Link con el enlace HATEOAS.
     */
    public static Link createLink(Object controller, String methodName, String rel, Object... args) {
        // Obtener la clase real del controlador (evitando proxies)
        Class<?> controllerClass = AopUtils.getTargetClass(controller);

        try {
            // Obtener los tipos de los parámetros
            Class<?>[] parameterTypes = getParameterTypes(args);

            // Usar WebMvcLinkBuilder.methodOn para crear una referencia al método
            Object methodInvocation = WebMvcLinkBuilder.methodOn(controllerClass)
                    .getClass()
                    .getMethod(methodName, parameterTypes)
                    .invoke(WebMvcLinkBuilder.methodOn(controllerClass), args);

            // Crear el enlace HATEOAS
            return WebMvcLinkBuilder.linkTo(methodInvocation).withRel(rel);
        } catch (Exception e) {
            throw new RuntimeException("Error creating HATEOAS link " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene los tipos de los parámetros de un método.
     *
     * @param args Los argumentos del método.
     * @return Un array de Class<?> con los tipos de los parámetros.
     */
    private static Class<?>[] getParameterTypes(Object... args) {
        Class<?>[] parameterTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        return parameterTypes;
    }
}