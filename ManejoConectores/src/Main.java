import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
	public int codigoalumno, codigoasignatura;
	private Connection conexion;
	private String url;

	/**
	 * 
	 * @param bd
	 * @param servidor
	 * @param usuario
	 * @param password
	 */
	public void abrirConexion(String bd, String servidor, String usuario, String password) {

		try {
			url = String.format("jdbc:mariadb://%s:3306/%s", servidor, bd);

			this.conexion = DriverManager.getConnection(url, usuario, password);
			if (this.conexion != null) {
				System.out.println("Conectado a la bd " + bd + " en " + servidor);
			} else
				System.out.println("No se ha podido realizar la conexion a " + bd);
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getLocalizedMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("Código error: " + e.getErrorCode());
		}
	}

	/**
	 * 
	 */
	public void cerrarConexion() {
		try {
			this.conexion.close();
		} catch (SQLException e) {
			System.out.println("Error al cerrar la conexión: " + e.getLocalizedMessage());
		}
	}

	/**
	 * Ejercicio 1
	 * 
	 * @param cad
	 * @throws SQLException
	 */
	public void buscaAlumno(String cad) throws SQLException {
		String query = "select * from alumnos where nombre like '%" + cad + "%'";

		abrirConexion("add", "localhost", "root", "");
		Statement st = this.conexion.createStatement();
		ResultSet rs = st.executeQuery(query);
		while (rs.next()) {
			System.out.println(rs.getString(2));
		}
		st.close();
		cerrarConexion();

	}

	/**
	 * 
	 * @param st
	 * @throws IOException
	 */
	public void addStudent(Student st) throws IOException {

		PreparedStatement ps1;
		Statement state;
		try {
			abrirConexion("add", "localhost", "root", "");

			state = this.conexion.createStatement();
			ResultSet rs = state.executeQuery("Select MAX(codigo) Cod from alumnos");
			while (rs.next()) {
				codigoalumno = rs.getInt(1) + 1;

			}
			st.setCodigo(codigoalumno);
			ps1 = conexion.prepareStatement(
					"INSERT INTO alumnos (codigo,nombre,apellidos,altura,aula)" + "VALUES (?,?,?,?,?)");
			ps1.setInt(1, st.getCodigo());
			ps1.setString(2, st.getNombre());
			ps1.setString(3, st.getApellidos());
			ps1.setInt(4, st.getAltura());
			ps1.setInt(5, st.getAula());
			ps1.executeQuery();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param sub
	 */
	public void addSubject(Subject sub) {
		PreparedStatement ps1;
		Statement state;
		try {
			abrirConexion("add", "localhost", "root", "");

			state = this.conexion.createStatement();
			ResultSet rs = state.executeQuery("Select MAX(COD) Cod from asignaturas");
			while (rs.next()) {
				codigoasignatura = rs.getInt(1) + 1;

			}
			sub.setCodigo(codigoasignatura);
			ps1 = conexion.prepareStatement("INSERT INTO asignaturas (COD,NOMBRE) VALUES (?,?)");
			ps1.setInt(1, sub.getCodigo());
			ps1.setString(2, sub.getNombre());

			ps1.executeQuery();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Ejercicio 3
	 * 
	 * @param ob
	 * @param codigo
	 */
	public void borraDatos(Object ob, int codigo) {
		String table = "";
		String cod = "";
		if (ob.getClass() == Student.class) {
			Student st = (Student) ob;

			table = "alumnos";
			cod = "codigo";

		} else if (ob.getClass() == Subject.class) {
			table = "asignaturas";
			Subject subject = (Subject) ob;

			cod = "COD";
		} else {
			System.err.println("Class not found.");
		}
		try {
			abrirConexion("add", "localhost", "root", "");

			Statement sta = this.conexion.createStatement();
			int filasAfectadas = sta.executeUpdate("DELETE  FROM " + table + " where " + cod + " = " + codigo + ";");
			System.out.println("Filas afectadas :" + filasAfectadas);
			sta.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getLocalizedMessage());
		}
	}

	/**
	 * Ejercicio 4
	 * 
	 * @param ob
	 * @param codigo
	 */
	public void editaDatos(Object ob, int codigo) {
		PreparedStatement ps1;
		String table = "";
		String cod = "";
		Student st;
		Subject subject;
		int filasAfectadas = 0;
		if (ob.getClass() == Student.class) {
			st = (Student) ob;
			table = "alumnos";
			cod = "codigo";

		} else if (ob.getClass() == Subject.class) {
			table = "asignaturas";
			subject = (Subject) ob;
			cod = "COD";
		} else {
			System.err.println("Class not found.");
		}

		try {
			abrirConexion("add", "localhost", "root", "");

			Statement sta = this.conexion.createStatement();
			if (ob.getClass() == Student.class) {
				st = (Student) ob;
				System.out.println("UPDATE " + table + " SET nombre='" + st.getNombre() + "'" + ",apellidos='"
						+ st.getApellidos() + "'" + ",altura=" + st.getAltura() + ",aula=" + st.getAula() + " WHERE "
						+ cod + "=" + codigo + ";");
				System.out.println(st.getNombre() + st.getApellidos());
				ps1 = conexion.prepareStatement("UPDATE " + table + " SET nombre='" + st.getNombre() + "'"
						+ ",apellidos='" + st.getApellidos() + "'" + ",altura=" + st.getAltura() + ",aula="
						+ st.getAula() + " WHERE " + cod + "=" + codigo + ";");

				filasAfectadas = ps1.executeUpdate();

			} else if (ob.getClass() == Subject.class) {

				subject = (Subject) ob;
				ps1 = conexion.prepareStatement("UPDATE " + table + " SET NOMBRE='" + subject.getNombre() + "' WHERE "
						+ cod + "=" + codigo + ";");
				filasAfectadas = ps1.executeUpdate();
			}

			System.out.println("Filas afectadas :" + filasAfectadas);
			sta.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getLocalizedMessage());
		}

	}

	/**
	 * Ejercicio 5 b Nombre de los alumnos, de las asignaturas y notas de aquellos
	 * alumnos que han aprobado alguna asignatura.
	 * 
	 * @throws SQLException
	 */

	public void infoAlumnos() throws SQLException {
		String query = "";

		abrirConexion("add", "localhost", "root", "");
		try (Statement sta = this.conexion.createStatement()) {

			query = String.format("Select alumnos.nombre,asignaturas.COD,asignaturas.nombre,nota from notas "
					+ "join asignaturas on asignaturas.COD=notas.asignatura "
					+ "join alumnos on alumnos.codigo=notas.alumno where nota>5 order by alumnos.codigo");
			System.out.println(query);
			ResultSet rs = sta.executeQuery(query);

			while (rs.next()) {
				System.out.println(rs.getString("nombre") + " -->" + rs.getInt("NOTA") + "\t"
						+ rs.getString("asignaturas.NOMBRE") + "(" + rs.getInt("COD") + ")");
			}
			sta.close();
		} catch (SQLException e) {
			System.out.println("Se ha producido un error: " + e.getLocalizedMessage());
		}
	}

	/**
	 * TODO Ejercicio 5c Muestra la asignatura que no tiene ningun alumno
	 */
	public void asignaturasSinAlumnos() {
		String query = "";
		try (Statement sta = this.conexion.createStatement()) {
			query = String
					.format("Select nombre from asignaturas join notas on asignaturas.COD not notas.asignatura  ");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * Ejercicio 6. Preparadas
	 * 
	 * @param patron
	 * @param altura
	 */
	public void comparaPrepSta(String patron, int altura) {
		PreparedStatement ps = null;
		String query;
		this.url = "jdbc:mysql://%s:3306/%s?useServerPrepStmts=true";
		try {
			System.out.println("Con sentencia preparada");
			query = "SELECT altura, nombre from alumnos where nombre like ? && altura>=?";
			if (ps == null) {
				ps = this.conexion.prepareStatement(query);
				ps.setString(1, patron);
				ps.setInt(2, altura);
				ResultSet resultado = ps.executeQuery();
				System.out.println(query);
				while (resultado.next()) {
					System.out.println(resultado.getString("nombre") + ":" + resultado.getInt(1));
				}
			}
		} catch (SQLException e) {
			System.out.println("Se ha producido un error: " + e.getLocalizedMessage());
		}
	}

	/**
	 * Ejercicio 6 . Normal
	 * 
	 * @param patron
	 * @param altura
	 */
	public void comparaPatron(String patron, int altura) {
		String query = "";
		try (Statement sta = this.conexion.createStatement()) {
			System.out.println("Sin sentencia preparada :");
			query = ("Select nombre,altura from alumnos where nombre like \'%" + patron + "%\' && altura>=" + altura);
			System.out.println(query);
			ResultSet rs = sta.executeQuery(query);
			while (rs.next()) {
				System.out.println(rs.getString("nombre") + ":" + rs.getInt("altura"));
			}
			sta.close();
		} catch (SQLException e) {
			System.out.println("Se ha producido un error: " + e.getLocalizedMessage());
		}
	}

	/**
	 * Quememos crear un método que pasándole cuatro parámetros (tabla, nombre de
	 * campo, tipo de dato, propiedades) nos permita añadir una columna a una tabla.
	 * 
	 * @param tabla
	 * @param nombre
	 * @param tipo
	 * @param propiedades
	 */
	public void añadirColumna(String tabla, String nombre, String tipo, String propiedades) {

		
	}

	/**
	 * 9.dMediante DatabaseMetaData (y métodos similares) queremos obtener cierta
	 * información de la base de datos y de las tablas que contiene la base de
	 * datos: Para todas las tablas de la base datos ADD obtén: el nombre de las
	 * tabla y el tipo de tabla
	 * 
	 * @param bd Base de datos de la consulta de datos
	 */
	public void ejercicio9d(String bd) {
		DatabaseMetaData dbmt;
		ResultSet tablas;
		try {
			dbmt = this.conexion.getMetaData();
			tablas = dbmt.getTables(bd, null, null, null);
			while (tablas.next()) {
				if (tablas.getString("TABLE_TYPE").equals("VIEW")) {
					System.out.println("## VIEW ##");
					System.out.println(String.format("Table : %s - Type: %s", tablas.getString("TABLE_NAME"),
							tablas.getString("TABLE_TYPE")));
				} else if (tablas.getString("TABLE_TYPE").equals("TABLE")) {
					System.out.println("## TABLE ##");
					System.out.println(String.format("Table : %s - Type: %s", tablas.getString("TABLE_NAME"),
							tablas.getString("TABLE_TYPE")));
				}
			}

		} catch (SQLException e) {
			System.out.println("Error obteniendo datos " + e.getLocalizedMessage());
		}
	}

	/**
	 * Mediante getColumns obtén de las tablas de la base de datos ADD que comiencen
	 * por 'a' los siguientes datos: posición de la columna, base de datos, tabla,
	 * nombre de la columna, nombre del tipo de dato de la columna, tamaño de la
	 * columna y si permite nulos. Indica también si has encontrado alguna tabla con
	 * un campo autoincrementado.
	 * 
	 * @param bd Base de datos de la consulta
	 */
	public void ejercicio9g(String bd) {
		DatabaseMetaData dbmt;
		ResultSet tablas, columnas;
		try {
			dbmt = this.conexion.getMetaData();
			tablas = dbmt.getTables(bd, null, null, null);
			System.out.println("DATOS BASE DE DATOS: " + bd);
			while (tablas.next()) {
				if (tablas.getString("TABLE_NAME").substring(0, 1).equals("a")) {

					System.out.println("Tabla : " + tablas.getString("TABLE_NAME"));
					columnas = dbmt.getColumns(bd, null, tablas.getString("TABLE_NAME"), null);
					while (columnas.next()) {
						System.out.println(String.format(
								"Columna: %s Tipo :%s \nTamaño col: %d Nullable?: %s \nAutoincrement?: %s",
								columnas.getString("COLUMN_NAME"), columnas.getString("TYPE_NAME"),
								columnas.getInt("COLUMN_SIZE"), columnas.getString("IS_NULLABLE"),
								columnas.getString("IS_AUTOINCREMENT")));
						System.out.println("Posicion columna:" + columnas.getString("ORDINAL_POSITION"));

					}
					System.out.println();
				}

			}
		} catch (Exception e) {
			System.out.println("Error obteniendo datos " + e.getLocalizedMessage());

		}

	}

	/**
	 * Muestra informacion de las claves primarias
	 * 
	 * @param bd
	 */
	public void ejercicio9HPK(String bd) {
		DatabaseMetaData dbmt;
		ResultSet tables, primarykeys;
		try {
			dbmt = this.conexion.getMetaData();
			tables = dbmt.getTables("add", null, null, null);
			while (tables.next()) {
				primarykeys = dbmt.getPrimaryKeys("add", null, tables.getString("TABLE_NAME"));
				while (primarykeys.next()) {
					System.out.println("Table name: " + primarykeys.getString("TABLE_NAME"));
					System.out.println("Column name: " + primarykeys.getString("COLUMN_NAME"));
					System.out.println("Catalog name: " + primarykeys.getString("TABLE_CAT"));

					System.out.println("Primary key sequence: " + primarykeys.getString("KEY_SEQ"));
					System.out.println("Primary key name: " + primarykeys.getString("PK_NAME"));
					System.out.println(" ");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ejercicio9HEK(String bd) {
		DatabaseMetaData dbmt;
		ResultSet exportedkeys;

		try {
			dbmt = this.conexion.getMetaData();
			exportedkeys = dbmt.getExportedKeys("add", null, null);
			while (exportedkeys.next()) {
				System.out.println("Table name :" + exportedkeys.getString("PKTABLE_NAME"));
				System.out.println("Column name :" + exportedkeys.getString("PKCOLUMN_NAME"));
				System.out.println("FK Table name :" + exportedkeys.getString("FKTABLE_NAME"));
				System.out.println("FK Column name: " + exportedkeys.getString("FKCOLUMN_NAME"));
				System.out.println("  ");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		Main m = new Main();
		// m.buscaAlumno("f");

		Student stañadir = new Student(0, "Miguel", "Vazquez", 175, 20);
		Student stborrar = new Student("Miguel", "Vazquez", 175, 20);
		Student steditat = new Student("Editado", "Edit", 666, 20);

		Subject sub = new Subject(0, "Nueva asignatura");
		Subject subject = new Subject(0, "EditSub");

		try {
			m.abrirConexion("add", "localhost", "root", "");

			// m.addStudent(stañadir);
			// m.addSubject(sub);

			// m.editaDatos(subject, 4);

			// m.borraDatos(stborrar, 12);
			// m.editaDatos(steditat, 11);

			// m.infoAlumnos();
			// m.asignaturasSinAlumnos();
			// m.comparaPrepSta("r%", 150);
			// m.comparaPatron("r%", 150);

			// m.ejercicio9d("add");
			// m.ejercicio9g("add");

			// m.ejercicio9HPK("add");
			m.ejercicio9HEK("add");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
