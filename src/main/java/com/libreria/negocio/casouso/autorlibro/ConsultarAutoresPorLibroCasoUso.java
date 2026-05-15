package com.libreria.negocio.casouso.autorlibro;

import com.libreria.entidad.AutorLibroEntidad;
import com.libreria.negocio.casouso.CasoUsoConRetorno;
import java.util.List;
import java.util.UUID;

public interface ConsultarAutoresPorLibroCasoUso extends CasoUsoConRetorno<UUID, List<AutorLibroEntidad>> {}
