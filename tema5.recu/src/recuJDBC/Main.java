package recuJDBC;

import recuRest.Nave;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Conectores con = new Conectores();
		Nave nv;
		nv = new Nave(15, "Nemesis", "Alderaan", "Taiwan", "LanzaBlas", 120, 300, 500, 3000, 2500, 1000, 50, 900,
				"Activo");
		try {
			con.abrirConexion("recuperacion", "localhost", "root", "");
			// con.buscaValor("pais", "Rusia");//Ejercicio1
			// con.borrarFilas(14);//Ejercicio2
			con.actualizaFila(1, nv);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.cerrarConexion();
		}
	}

}
