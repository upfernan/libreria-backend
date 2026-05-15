package com.libreria.negocio.fachada.categoria;

import com.libreria.dto.CategoriaDTO;
import com.libreria.negocio.fachada.FachadaConRetorno;
import java.util.UUID;

public interface ConsultarCategoriaPorIdFachada extends FachadaConRetorno<UUID, CategoriaDTO> {

}
