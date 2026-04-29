package com.libreria.negocio.assembler.entidad;

//D: Representa un dominio (Domain) y E: Representa una Entidad
public interface EntidadAssembler<D, E> {

 D ensamblarDominio(E entidad);

 E ensamblarEntidad(D dominio);
}
