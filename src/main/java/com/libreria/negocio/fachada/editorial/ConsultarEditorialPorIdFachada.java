package com.libreria.negocio.fachada.editorial;

import com.libreria.dto.EditorialDTO;
import com.libreria.negocio.fachada.FachadaConRetorno;
import java.util.UUID;

public interface ConsultarEditorialPorIdFachada extends FachadaConRetorno<UUID, EditorialDTO> {

}
