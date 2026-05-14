package com.libreria.datos.dao.sql;

import java.sql.Connection;

public class SQLDAO {

	private Connection conexion;

	protected SQLDAO(final Connection conexion) {
		super();
		this.conexion = conexion;
	}

	protected Connection getConexion() {
		return conexion;
	}

}
