package com.libreria.negocio.fachada.libro;

import com.libreria.dto.LibroDTO;
import com.libreria.negocio.fachada.FachadaConRetorno;
import java.util.UUID;

public interface ConsultarLibroPorIdFachada extends FachadaConRetorno<UUID, LibroDTO> {

}
