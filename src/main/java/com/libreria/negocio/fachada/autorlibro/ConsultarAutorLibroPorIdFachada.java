package com.libreria.negocio.fachada.autorlibro;

import com.libreria.dto.AutorLibroDTO;
import com.libreria.negocio.fachada.FachadaConRetorno;
import java.util.UUID;

public interface ConsultarAutorLibroPorIdFachada extends FachadaConRetorno<UUID, AutorLibroDTO> {}
