package recuJDBC;

import recuRest.Nave;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Conectores con = new Conectores();
		Nave nv;
		nv = new Nave("Sputnik", "Putinland", "Nintendo", "LanzaOsos", 120, 300, 500, 3000, 2500, 1000, 50, 900,
				"null");
		try {
			con.abrirConexion("recuperacion", "localhost", "root", "");
			// con.buscaValor("pais", "Putinland"); //Ejercicio1
			// con.borrarFilas(14); //Ejercicio2
			// con.actualizaFila(19, nv); //Ejercicio3
			// con.insertarFilas(nv); //Ejercicio4

			// Ejercicio5
			// con.numNaves("Rusia"); //A
			// con.numEstado("USA"); // B
			// con.sinEstado(); //C
			// con.mayorCarga(); //D

			// Ejercicio 6 Sentencias preparadas
			// con.numNavesPs("USA");
			// con.numEstadoPs("Rusia");
			// con.sinEstadoPs("Activo");
			// con.mayorCargaPs();

			// Ejercicio 7
			// con.getPaises();
			/**
			 * a) Bases de datos disponibles. b) Nombre de la tabla y tipo dentro de una
			 * base de datos concreta. c) Nombre de las columnas de la tabla naves, tipo de
			 * dato y si permite nulos. d) Campo que es clave primaria de tabla notas.
			 */
			
			//con.getInfo("recuperacion");
			con.getInfoRSmt("Select * from naves");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.cerrarConexion();
		}
	}

}
