package com.libreria.negocio.fachada.autorlibro;

import com.libreria.dto.AutorLibroDTO;
import com.libreria.negocio.fachada.FachadaConRetorno;
import java.util.List;
import java.util.UUID;

public interface ConsultarAutoresPorLibroFachada extends FachadaConRetorno<UUID, List<AutorLibroDTO>> {}
