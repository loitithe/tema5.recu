package recuJDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.metadata.IIOMetadataFormatImpl;

public class Conectores {
	private Connection conexion;
	private String url;

	public void abrirConexion(String bd, String servidor, String usuario, String password)
			throws ClassNotFoundException {
		try {
			// Class.forName("org.mariadb.jdbc.Driver");
			url = String.format("jdbc:mariadb://%s:3306/%s", servidor, bd);

			this.conexion = DriverManager.getConnection(url, usuario, password); // Establecemos la conexión con la
			// BD
			if (this.conexion != null)
				System.out.println("Conectado a la base de datos " + bd + " en " + servidor);

			else
				System.out.println("No se ha conectado a la base de datos " + bd + " en " + servidor);
		} catch (SQLException e) {

			System.out.println("SQLException: " + e.getLocalizedMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("Código error: " + e.getErrorCode());
		}

	}

	public void cerrarConexion() {
		try {
			this.conexion.close();
		} catch (SQLException e) {
			System.out.println("Error al cerrar la conexión: " + e.getLocalizedMessage());
		}
	}

	void buscaValor(String key, String value) {
		String query = String.format("Select * from naves where %s like %s", key, value);
		try {
			abrirConexion("add", "localhost", "root", "");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try (Statement st = this.conexion.createStatement()) {
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				for (int i = 0; i < 14; i++) {
					System.out.print(" " + rs.getInt(i));

				}
			}
			st.close();
			cerrarConexion();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
