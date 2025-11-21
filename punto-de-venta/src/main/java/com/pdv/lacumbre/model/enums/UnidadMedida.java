package com.pdv.lacumbre.model.enums;

public enum UnidadMedida {
    // Más comunes (uso diario)
    PIEZA("pieza"),
    METRO("metro"),
    KILOGRAMO("kilogramo"),
    LITRO("litro"),
    PAQUETE("paquete"),
    CAJA("caja"),
    BOLSA("bolsa"),

    // Materiales de construcción
    SACO("saco"),
    BULTO("bulto"),
    METRO_CUBICO("metro cúbico"),
    CUBETA("cubeta"),
    GALON("galón"),
    TONELADA("tonelada"),

    // Herramientas y productos específicos
    JUEGO("juego"),
    AEROSOL("aerosol"),
    ROLLO("rollo"),
    TRAMO("tramo"),
    CARTUCHO("cartucho"),

    // Menos comunes
    MILILITRO("mililitro"),
    GRAMO("gramo"),
    PLANCHA("plancha"),
    LAMINA("lámina"),
    CAMION("camión"),
    CARRETILLA("carretilla");

    private final String descripcion;

    UnidadMedida(String descripcion){
        this.descripcion = descripcion;
    }

    public String getDescripcion(){
        return descripcion;
    }
}
