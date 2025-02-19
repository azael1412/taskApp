package com.azael.taskapp.helper;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;

import java.util.Map;

public class HateoasPaginationHelper {

    /**
     * Método genérico para agregar enlaces HATEOAS a un DTO.
     *
     * @param dto        El objeto DTO al que se agregarán los enlaces.
     * @param controller La instancia del controlador que maneja el endpoint.
     * @param methodName El nombre del método del controlador que se usará para
     *                   generar el enlace.
     * @param page       Número de página actual.
     * @param size       Tamaño de la página.
     * @param sortBy     Campo por el que se ordena.
     * @param direction  Dirección de ordenamiento (asc/desc).
     * @param filters    Mapa de filtros opcionales (pueden variar según el
     *                   controlador).
     * @param <T>        Tipo genérico del DTO.
     * @return El DTO con los enlaces HATEOAS agregados.
     */
    public static <T> T addHateoasLinks(T dto, Object controller, String methodName, int page, int size, String sortBy,
            String direction, Map<String, String> filters) {
        if (dto instanceof RepresentationModel) {
            ((RepresentationModel<?>) dto).add(HateoasHelper.createLink(
                    controller,
                    methodName,
                    "self",
                    page, size, sortBy, direction, filters));
        }
        return dto;
    }

    /**
     * Método genérico para agregar enlaces de paginación a un modelo paginado.
     *
     * @param pagedModel El modelo paginado al que se agregarán los enlaces.
     * @param page       La página original obtenida del repositorio.
     * @param controller La instancia del controlador que maneja el endpoint.
     * @param methodName El nombre del método del controlador que se usará para
     *                   generar el enlace.
     * @param pageParam  Número de página actual.
     * @param size       Tamaño de la página.
     * @param sortBy     Campo por el que se ordena.
     * @param direction  Dirección de ordenamiento (asc/desc).
     * @param filters    Mapa de filtros opcionales (pueden variar según el
     *                   controlador).
     * @param <T>        Tipo genérico del contenido del modelo paginado.
     */
    public static <T> void addPaginationLinks(PagedModel<T> pagedModel, Page<?> page, Object controller,
            String methodName, int pageParam, int size, String sortBy, String direction, Object... filters) {
        // Enlace a la página actual
        pagedModel.add(HateoasHelper.createLink(
                controller,
                methodName,
                "self",
                pageParam, size, sortBy, direction, filters));

        // Enlace a la siguiente página
        if (page.hasNext()) {
            pagedModel.add(HateoasHelper.createLink(
                    controller,
                    methodName,
                    "next",
                    pageParam + 1, size, sortBy, direction, filters));
        }

        // Enlace a la página anterior
        if (page.hasPrevious()) {
            pagedModel.add(HateoasHelper.createLink(
                    controller,
                    methodName,
                    "previous",
                    pageParam - 1, size, sortBy, direction, filters));
        }

        // Enlace a la primera página
        pagedModel.add(HateoasHelper.createLink(
                controller,
                methodName,
                "first",
                0, size, sortBy, direction, filters));

        // Enlace a la última página
        pagedModel.add(HateoasHelper.createLink(
                controller,
                methodName,
                "last",
                page.getTotalPages() - 1, size, sortBy, direction, filters));
    }
}