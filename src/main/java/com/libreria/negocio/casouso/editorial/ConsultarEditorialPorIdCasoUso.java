package com.libreria.negocio.casouso.editorial;

import com.libreria.negocio.casouso.CasoUsoConRetorno;
import com.libreria.entidad.EditorialEntidad;
import java.util.UUID;

public interface ConsultarEditorialPorIdCasoUso extends CasoUsoConRetorno<UUID, EditorialEntidad> {}
