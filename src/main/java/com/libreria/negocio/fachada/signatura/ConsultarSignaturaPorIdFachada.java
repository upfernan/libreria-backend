package com.libreria.negocio.fachada.signatura;

import com.libreria.dto.SignaturaDTO;
import com.libreria.negocio.fachada.FachadaConRetorno;
import java.util.UUID;

public interface ConsultarSignaturaPorIdFachada extends FachadaConRetorno<UUID, SignaturaDTO> {}
