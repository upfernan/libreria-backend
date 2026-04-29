package com.libreria.negocio.assembler.dto;

// Representa un dominio (Domain) y T: Representa un Transfer Object 
public interface DTOAssembler<D, T> {

 D ensamblarDominio(T dto);

 T ensamblarDTO(D dominio);
}
