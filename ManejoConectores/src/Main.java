import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
	public int codigoalumno, codigoasignatura;
	private Connection conexion;

	/**
	 * 
	 * @param bd
	 * @param servidor
	 * @param usuario
	 * @param password
	 */
	public void abrirConexion(String bd, String servidor, String usuario, String password) {

		try {
			String url = String.format("jdbc:mariadb://%s:3306/%s", servidor, bd);

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
	 * Nombre de las aulas con alumnos
	 * 
	 * @throws SQLException
	 */
	public void infoAlumnos() throws SQLException {
		abrirConexion("add", "localhost", "root", "");
		String query="Select nombreAula from aulas,alumnos where aulas.numero = alumnos.aula; ";
		Statement stmt = this.conexion.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {

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

		// m.addStudent(stañadir);
		// m.addSubject(sub);

		// m.editaDatos(subject, 4);

		// m.borraDatos(stborrar, 12);
		// m.editaDatos(steditat, 11);

		m.infoAlumnos();
	}

}
