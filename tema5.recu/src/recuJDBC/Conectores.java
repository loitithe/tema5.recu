package recuJDBC;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.metadata.IIOMetadataFormatImpl;

import org.mariadb.jdbc.MySQLDataSource;

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
	 * Crea un método que pasándole un nombre de campo y un valor nos devuelva todas
	 * las filas que contengan ese valor. Hay que tener en cuenta y tratar de forma
	 * correcta los campos en formato texto y los campos numéricos
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
	 * Borre filas de la tabla un función de su id. Deberá devolver el número de
	 * filas borradas.
	 * 
	 * @param id
	 */
	void borrarFilas(int id) {
		String query = String.format("Delete from naves where id= %d", id);
		try (Statement st = this.conexion.createStatement()) {
			int delNave = st.executeUpdate(query);
			System.out.println("Filas borradas: " + delNave);

		} catch (SQLException e) {
			System.out.println("Error: " + e.getLocalizedMessage());
		}
	}

	/**
	 * Nos permita actualizar filas de una tabla en función de su id. Deberá
	 * devolver el número de filas actualizadas.
	 * 
	 * @param id
	 * @param nv
	 */
	void actualizaFila(int id, Nave nv) {
		String query = String.format("Update naves set nombre='%s',fabricante='%s',estado='%s' where id= %d",
				nv.getNombre(), nv.getFabricante(), nv.getEstado(), id);
		try (Statement st = this.conexion.createStatement()) {
			int filasActualizadas = st.executeUpdate(query);
			System.out.println("Filas actualizadas :" + filasActualizadas);
		} catch (SQLException e) {
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
		String query = String.format(
				"Insert into naves(nombre,pais,fabricante,Estado,Potencia) VALUES('%s','%s','%s','%s','%d')",
				nv.getNombre(), nv.getPais(), nv.getFabricante(), nv.getEstado(), nv.getPotencia());
		try (Statement st = this.conexion.createStatement()) {
			int filasInsertadas = st.executeUpdate(query);
			System.out.println("Filas insertadas: " + filasInsertadas);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * a) Dado un país devuelva el número de naves totales que tenga.
	 * 
	 * @param pais
	 */
	void numNaves(String pais) {
		String query = String.format("Select count(*) From naves where pais='%s';", pais);
		try (Statement st = this.conexion.createStatement()) {
			ResultSet rs = st.executeQuery(query);
			System.out.println(query);
			if (rs.next()) {
				System.out.println("Número de naves en " + pais + " : " + rs.getInt(1));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * b) Dado un país devuelva el número de naves que tiene en cada estado.
	 * 
	 * @param pais
	 */
	void numEstado(String pais) {
		int activo = 0;
		int retirado = 0;
		int endes = 0;
		int otros = 0;
		String query = String.format("select estado,count(*) as cant from naves where pais='%s' group by estado;",
				pais);
		try (Statement st = this.conexion.createStatement()) {
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				switch (rs.getString("Estado")) {
				case "Retirado":
					retirado = rs.getInt("cant");
					break;
				case "Activo":
					activo = rs.getInt("cant");
					break;
				case "En desarrollo":
					endes = rs.getInt("cant");
					break;
				default:
					otros += rs.getInt("cant");
					break;
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Activo :" + activo);
		System.out.println("Retirado :" + retirado);
		System.out.println("En desarrollo :" + endes);
		System.out.println("Otros :" + otros);
	}

	/* c) ¿Qué naves no tienen un estado asignado? */
	void sinEstado() {
		String query = "Select * from naves where Estado is null";
		try (Statement st = this.conexion.createStatement()) {
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				System.out.println(rs.getString("nombre") + " : estado " + rs.getString("Estado"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/* d) La segunda y tercera con un volumen de carga mayor. */
	void mayorCarga() {
		String query = "select * from naves order by CargaUtil desc limit 1,3";
		try (Statement st = this.conexion.createStatement()) {
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				System.out.println(rs.getString("nombre") + " carga util : " + rs.getString("CargaUtil"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private PreparedStatement ps = null;
	// Ejercicio5 con preparedStatement

	void numNavesPs(String pais) throws SQLException {
		String query = "Select * From naves where pais=?;";
		System.out.println(query);
		if (this.ps == null) {
			this.ps = this.conexion.prepareStatement(query);
			ps.setString(1, pais);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString("nombre"));
			}

		}
	}

	void numEstadoPs(String pais) throws SQLException {
		int activo = 0;
		int retirado = 0;
		int endes = 0;
		int otros = 0;
		String query = "select estado,count(*) as cant from naves where pais=? group by estado;";

		if (this.ps == null) {
			this.ps = this.conexion.prepareStatement(query);
			ps.setString(1, pais);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				switch (rs.getString("Estado")) {
				case "Retirado":
					retirado = rs.getInt("cant");
					break;
				case "Activo":
					activo = rs.getInt("cant");
					break;
				case "En desarrollo":
					endes = rs.getInt("cant");
					break;
				default:
					otros += rs.getInt("cant");
					break;
				}
			}
			System.out.println("Activo :" + activo);
			System.out.println("Retirado :" + retirado);
			System.out.println("En desarrollo :" + endes);
			System.out.println("Otros :" + otros);

		}

	}

	void sinEstadoPs(String estado) throws SQLException {
		String query = "Select * from naves where Estado <=> ?";
		if (this.ps == null) {
			this.ps = this.conexion.prepareStatement(query);
			if (estado.toLowerCase().equals("null")) {
				ps.setNull(1, 1);
			} else {
				ps.setString(1, estado);
			}
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString("nombre") + " : estado " + rs.getString("Estado"));
			}
		}
	}

	void mayorCargaPs() throws SQLException {
		String query = "select * from naves order by CargaUtil desc limit 1,3";
		if (this.ps == null) {
			this.ps = this.conexion.prepareStatement(query);
			// ps.setString(1, valor);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString("nombre") + "  :" + rs.getString("CargaUtil"));
			}
		}

	}

	/**
	 * Ejercicio 7- Procedimiento almacenado paises
	 * 
	 * @throws SQLException
	 */
	void getPaises() throws SQLException {
		CallableStatement cs = this.conexion.prepareCall("CALL paises");
		ResultSet rs = cs.executeQuery();
		while (rs.next()) {
			System.out.println(rs.getString(1));

		}

	}

	/**
	 * Ejercicio 8
	 * 
	 * @param bd
	 */
	public void getInfo(String bd) {
		DatabaseMetaData dbmt;
		ResultSet pk, db, tablas, columnas;
		try {
			dbmt = this.conexion.getMetaData();

			db = dbmt.getCatalogs();
			System.out.println("Bases de datos disponibles :");
			while (db.next()) {
				System.out.println(db.getString(1));
			}
			System.out.println("### Info de las tablas ###");
			tablas = dbmt.getTables(bd, null, null, null);

			while (tablas.next()) {
				System.out.println(
						String.format("%s %s", tablas.getString("TABLE_NAME"), tablas.getString("TABLE_TYPE")));
				columnas = dbmt.getColumns(bd, null, tablas.getString("TABLE_NAME"), null);
				while (columnas.next()) {
					System.out.println(String.format(" %s %s %d %s %s", columnas.getString("COLUMN_NAME"),
							columnas.getString("TYPE_NAME"), columnas.getInt("COLUMN_SIZE"),
							columnas.getString("IS_NULLABLE"), columnas.getString("IS_AUTOINCREMENT")));
				}
				pk = dbmt.getPrimaryKeys(tablas.getString("TABLE_CAT"), tablas.getString("TABLE_SCHEM"),
						tablas.getString("TABLE_NAME"));
				while (pk.next()) {
					System.out.println("Primary key_:" + pk.getString("COLUMN_NAME"));
				}
			}
//			dbmt.getPrimaryKeys(catalog, schema, table;

		} catch (SQLException e) {
			System.out.println("Error obteniendo datos " + e.getLocalizedMessage());
		}
	}

	void getInfoRSmt(String query) throws SQLException {
		Statement st = this.conexion.createStatement();
		ResultSet rs = st.executeQuery(query);
		ResultSetMetaData rsmd = rs.getMetaData();
		System.out.println("Id\tNombre\tAlias\tTipo");
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			System.out.println(String.format("%d\t%s\t%s\t%s", i, rsmd.getColumnName(i), rsmd.getColumnLabel(i),
					rsmd.getColumnTypeName(i)));
		}
	}
}
