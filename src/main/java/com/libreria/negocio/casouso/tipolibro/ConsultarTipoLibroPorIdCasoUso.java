package com.libreria.negocio.casouso.tipolibro;

import com.libreria.negocio.casouso.CasoUsoConRetorno;
import com.libreria.entidad.TipoLibroEntidad;
import java.util.UUID;

public interface ConsultarTipoLibroPorIdCasoUso extends CasoUsoConRetorno<UUID, TipoLibroEntidad> {}
