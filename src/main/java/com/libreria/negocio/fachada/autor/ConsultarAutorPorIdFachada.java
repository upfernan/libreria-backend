package com.libreria.negocio.fachada.autor;

import com.libreria.dto.AutorDTO;
import com.libreria.negocio.fachada.FachadaConRetorno;
import java.util.UUID;

public interface ConsultarAutorPorIdFachada extends FachadaConRetorno<UUID, AutorDTO> {}
