package recuJDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.metadata.IIOMetadataFormatImpl;

import recuRest.Nave;

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

	/**
	 * 
	 * @param key
	 * @param value
	 */
	void buscaValor(String key, String value) {
		String query = String.format("Select * from naves where %s = '%s'", key, value);

		try (Statement st = this.conexion.createStatement()) {
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {

				System.out.println(String.format("%d :%s \t %s %s", rs.getInt("id"), rs.getString("nombre"),
						rs.getString("pais"), rs.getString("Estado")));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cerrarConexion();
		}

	}

	/**
	 * 
	 * @param id
	 */
	void borrarFilas(int id) {
		String query = String.format("Delete from naves where id= %d", id);
		try (Statement st = this.conexion.createStatement()) {
			int delNave = st.executeUpdate(query);
			System.out.println("Filas borradas: " + delNave);

		} catch (Exception e) {
			System.out.println("Error: " + e.getLocalizedMessage());
		}
	}

	/**
	 * 
	 * @param id
	 * @param nv
	 */
	void actualizaFila(int id, Nave nv) {
		String query = String.format("Update naves set id=%d,nombre='%s',fabricante='%s',estado='%s' where id= %d",
				nv.getId(), nv.getNombre(), nv.getFabricante(), nv.getEstado(), id);
		try (Statement st = this.conexion.createStatement()) {
			int filasActualizadas = st.executeUpdate(query);
			System.out.println("Filas actualizadas :" + filasActualizadas);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 4. Inserte una fila en la tabla. El campo id debe insertarse de forma
	 * automática.
	 * 
	 * @param nv
	 */
	void insertarFilas(Nave nv) {
		String query = String.format("Insert into naves VALUES('%s','%s','%s','%s','%d')", nv.getNombre(), nv.getPais(),
				nv.getFabricante(), nv.getEstado(), nv.getPotencia());
		try (Statement st = this.conexion.createStatement()) {
			int filasInsertadas = st.executeUpdate(query);
			System.out.println("Filas insertadas: " + filasInsertadas);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
