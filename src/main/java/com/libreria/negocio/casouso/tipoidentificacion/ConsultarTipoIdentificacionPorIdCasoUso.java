package com.libreria.negocio.casouso.tipoidentificacion;

import com.libreria.negocio.casouso.CasoUsoConRetorno;
import com.libreria.entidad.TipoIdentificacionEntidad;
import java.util.UUID;

public interface ConsultarTipoIdentificacionPorIdCasoUso extends CasoUsoConRetorno<UUID, TipoIdentificacionEntidad> {}
