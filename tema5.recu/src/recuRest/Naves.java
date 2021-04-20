package recuRest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/naves")
public class Naves {

	static ArrayList<Nave> naves = new ArrayList<Nave>();
	Bd bd = new Bd();
	Nave nv;

	public void guardar(Nave nave) {
		this.naves.add(nave);
	}

	public Nave getNave(ResultSet rs) throws SQLException {
		Nave nave = new Nave();
		nave.setId(rs.getInt(1));
		nave.setNombre(rs.getString(2));
		nave.setPais(rs.getString(3));
		nave.setFabricante(rs.getString(4));
		nave.setSistemaLanzamiento(rs.getString(5));
		nave.setLongitud(rs.getFloat(6));
		nave.setMasaSeco(rs.getInt(7));
		nave.setMasaLanzamiento(rs.getInt(8));
		nave.setCargaUtil(rs.getInt(9));
		nave.setVolumenUtil(rs.getFloat(10));
		nave.setCargaUtilRetorno(rs.getInt(11));
		nave.setDiametro(rs.getFloat(12));
		nave.setPotencia(rs.getInt(13));
		nave.setEstado(rs.getString(14));

		naves.add(nave);
		return nave;

	}

	public void rellenaNaves() {

	}

	/**
	 * Ejercicio 10 - Crea un método que visualice todos los datos de la tabla
	 * naves.
	 * 
	 * @return coleccion de naves
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response visualizar() {

		try {
			bd.conecta("recuperacion");

			if (bd.con != null) {
				System.out.println("pasa");
				try (Statement st = bd.con.createStatement()) {
					ResultSet rs = st.executeQuery("select * from naves ;");
					while (rs.next()) {
						System.out.println(getNave(rs));
					}
				} // TODO controlar excepcion
				bd.cierraBd();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity("Esto peta").header("clave1", 10).build();
		}
		return Response.ok(naves).header("tam", naves.size()).build();
	}

	/**
	 * 11. Método nave que el en el path {nave} muestre información de una nave en
	 * particular (en función de su id).
	 * 
	 * @param id de la nave a buscar
	 * @return informacion de la nave buscada
	 * 
	 *         http://localhost:8080/tema5.recu/rest/naves/nave/1
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("nave/{id}")
	public Response infoNave(@PathParam("id") int id) {

		try {
			bd.conecta("recuperacion");

			if (bd.con != null) {
				System.out.println("pasa");
				try (Statement st = bd.con.createStatement()) {
					ResultSet rs = st.executeQuery("select * from naves where id =" + id);
					while (rs.next()) {
						nv = getNave(rs);
						System.out.println(getNave(rs));
					}
				}//TODO excepciones
				bd.cierraBd();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity("Esto peta").build();
		}
		return Response.ok(nv).build();
	}

	/**
	 * 12. Método países, bajo el path países, que muestre las naves pertenecen a un
	 * país.
	 * 
	 * @param pais
	 * @return http://localhost:8080/tema5.recu/rest/naves/Rusia
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("{paises}")
	public Response paisNave(@PathParam("paises") String pais) {
		naves = new ArrayList<Nave>();
		try {
			bd.conecta("recuperacion");
			if (bd != null) {
				try (Statement st = bd.con.createStatement()) {
					ResultSet rs = st.executeQuery("Select * from naves where pais ='" + pais + "'");
					while (rs.next()) {
						getNave(rs);
					}
				}//TODO excepciones
				bd.cierraBd();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity("Esto peta").build();
		}

		return Response.ok(naves).build();
	}

	/**
	 * Método estado que nos devuelve un una estructura de datos para representar
	 * las navesagrupadas por su estado. Hay que tener en cuenta que pueden no tener
	 * estado.
	 * 
	 * @return naves separadas por estados
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/estado")
	public Response estado() {
		ArrayList<Nave> naveA, naveR, naveD, naveN;
		naveA = new ArrayList<Nave>();
		naveR = new ArrayList<Nave>();
		naveD = new ArrayList<Nave>();
		naveN = new ArrayList<Nave>();

		try {
			bd.conecta("recuperacion");
			if (bd.con != null) {

				try (Statement st = bd.con.createStatement()) {
					ResultSet rs = st.executeQuery("select * from naves");
					while (rs.next()) {
						Nave n = getNave(rs);

						System.out.println(n.getNombre());
						switch (n.getEstado()) {
						case "Activo":
							naveA.add(n);
							break;
						case "Retirado":
							naveR.add(n);
							break;
						case "En desarrollo":
							naveD.add(n);
							break;
						default:
							naveN.add(n);
							break;
						}

					}
				}

				bd.cierraBd();//TODO excepciones
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity("Esto peta").build();
		}
		HashMap<String, ArrayList<Nave>> naves = new HashMap<String, ArrayList<Nave>>();
		naves.put("Activos", naveA);
		naves.put("Retirados", naveR);
		naves.put("En desarrollo", naveD);
		naves.put("Sin estado", naveN);
		return Response.ok(naves).build();
	}

	/**
	 * 14. Método que nos muestre el número de naves de un país pasado como parte de
	 * la URI. http://localhost:8080/tema5.recu/rest/naves/pais/Japon
	 * 
	 * @param pais sobre el que se hace la busqueda
	 * @return response con el numero de naves segun el pais
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("pais/{pais}")
	public Response navesPais(@PathParam("pais") String pais) {
		naves = new ArrayList<Nave>();
		try {
			bd.conecta("recuperacion");
			if (bd.con != null) {
				try (Statement st = bd.con.createStatement()) {
					ResultSet rs = st.executeQuery("Select * from naves where pais ='" + pais + "'");
					// System.out.println("Select * from naves where pais=" + pais);
					while (rs.next()) {
						System.out.println(getNave(rs));
					}
				}//TODO excepciones
				bd.cierraBd();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity("Esto peta").build();
		}
		// Pretty Print failed: SyntaxError: JSON.parse: unexpected character at line 1
		// column 1 of the JSON data
		return Response.ok("Numero de naves en " + pais + ": " + naves.size()).build();
	}

	/**
	 * 15. Método que nos muestre las naves de un fabricante pasado como parámetro
	 * de consulta. Si no se especifica ninguno se han de mostrar las naves de
	 * Energia.
	 * http://localhost:8080/tema5.recu/rest/naves/fabricantes?fabricante=EADS
	 * http://localhost:8080/tema5.recu/rest/naves/fabricantes?
	 * 
	 * @param fabricante
	 * @return informacion de las naves segun el fabricante
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("fabricantes")
	public Response navesFabricante(@DefaultValue("Energia") @QueryParam("fabricante") String fabricante) {
		naves = new ArrayList<Nave>();
		try {
			bd.conecta("recuperacion");
			if (bd.con != null) {
				try (Statement st = bd.con.createStatement()) {
					ResultSet rs = st.executeQuery("Select * from naves where fabricante='" + fabricante + "'");
					while (rs.next()) {
						System.out.println(getNave(rs));

					}
				}//TODO excepciones
				bd.cierraBd();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity("Esto peta").build();
		}

		return Response.ok(naves).build();
	}

	/**
	 * 16. Método que nos permita borrar una nave en función de su id.
	 * http://localhost:8080/tema5.recu/rest/naves/borrar/14
	 * 
	 * @param id
	 * @return
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("borrar/{id}")
	public Response borraNave(@PathParam("id") int id) {
		Nave n;
		try {
			bd.conecta("recuperacion");
			if (bd.con != null) {
				try (Statement st = bd.con.createStatement()) {
					int filas = st.executeUpdate("delete from naves where id=" + id);
						
					if (filas>0) {
						System.out.println("Nave con id:" + id + " ha sido borrada");			
						return Response.ok().build();
					}

					
				}//TODO excepciones
				bd.cierraBd();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity("Esto peta").build();
		}

		return Response.ok("Nave no encontrada").build();
	}

	/**
	 * 17. Método que nos permita actualizar una nave. Para realizar el ejercicio se
	 * debe pasar un objeto json por el RESTer Method POST Headers accept-json /
	 * content-type - json En body se deben introducir los datos del objeto
	 * 
	 * { "cargaUtil": 15000, "cargaUtilRetorno": 1000, "diametro": 60, "estado":
	 * "Nuevo", "fabricante": "Abel Caballero", "id": 20, "longitud": 0,
	 * "masaLanzamiento": 0, "masaSeco": 0, "nombre": "O dino voador", "pais":
	 * "Españita", "potencia": 0, "sistemaLanzamiento": "HabelasAilas",
	 * "volumenUtil": 0 }
	 * 
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public Response actualizaNave(Nave nv) {
		int rs = -1;
		try {
			bd.conecta("recuperacion");
			if (bd.con != null) {
				try (Statement st = bd.con.createStatement()) {
					rs = st.executeUpdate("Update naves set nombre='" + nv.getNombre() + "' where id=" + nv.getId());

				}
				bd.cierraBd();//TODO excepciones
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity("Esto peta" + e.getLocalizedMessage()).build();
		}

		return Response.ok(nv).header("numFilas", rs).build();

	}

	/**
	 * 18. Método que nos permita insertar una nave.
	 * 
	 * @param nv
	 * @return
	 */
	@Path("/insertar")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public Response insertarNave(Nave nv) {

		try {
			bd.conecta("recuperacion");
			if (bd.con != null) {
				String query = "insert into naves values(null,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				try (PreparedStatement st = bd.con.prepareStatement(query);) {
					System.out.println("pasa");
					st.setString(1, nv.getNombre()); // nombre
					st.setString(2, nv.getPais()); // pais
					st.setString(3, nv.getFabricante()); // fabricante
					st.setString(4, nv.getSistemaLanzamiento()); // sistemaLanzamiento
					st.setFloat(5, nv.getLongitud()); // longitud
					st.setInt(6, nv.getMasaSeco()); // masaSeco
					st.setInt(7, nv.getMasaLanzamiento()); // masaLanzamiento
					st.setInt(8, nv.getCargaUtil()); // cargaUtil
					st.setFloat(9, nv.getVolumenUtil()); // volumenUtil
					st.setInt(10, nv.getCargaUtilRetorno()); // cargaUtilRetorno
					st.setFloat(11, nv.getDiametro()); // diametro
					st.setInt(12, nv.getPotencia());// potencia
					st.setString(13, nv.getEstado());// estado
					int filas = st.executeUpdate();
					if (filas > 0) {
						System.out.println(nv.getNombre() + " ha sido añadido a la tabla naves");
					}
					naves.add(nv);
				}

				bd.cierraBd();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity("Esto peta" + e.getLocalizedMessage()).build();
		}
		return Response.ok(nv).build();
	}
}
