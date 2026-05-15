package com.libreria.negocio.casouso.autorlibro;

import com.libreria.entidad.AutorLibroEntidad;
import com.libreria.negocio.casouso.CasoUsoConRetorno;
import java.util.UUID;

public interface ConsultarAutorLibroPorIdCasoUso extends CasoUsoConRetorno<UUID, AutorLibroEntidad> {}
