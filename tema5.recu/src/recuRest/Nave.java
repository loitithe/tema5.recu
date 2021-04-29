package recuRest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Nave {
	private int id;
	private String nombre;
	private String pais;
	private String fabricante;
	private String sistemaLanzamiento;
	private float longitud;
	private int masaSeco;
	private int masaLanzamiento;
	private int cargaUtil;
	private float volumenUtil;
	private int cargaUtilRetorno;
	private float diametro;
	private int potencia;
	private String estado;

	public Nave(/* int id, */String nombre, String pais, String fabricante, String sistemaLanzamiento, float longitud,
			int masaSeco, int masaLanzamiento, int cargaUtil, float volumenUtil, int cargaUtilRetorno, float diametro,
			int potencia, String estado) {
		super();
		// this.id = id;
		this.nombre = nombre;
		this.pais = pais;
		this.fabricante = fabricante;
		this.sistemaLanzamiento = sistemaLanzamiento;
		this.longitud = longitud;
		this.masaSeco = masaSeco;
		this.masaLanzamiento = masaLanzamiento;
		this.cargaUtil = cargaUtil;
		this.volumenUtil = volumenUtil;
		this.cargaUtilRetorno = cargaUtilRetorno;
		this.diametro = diametro;
		this.potencia = potencia;
		this.estado = estado;

	}

	public Nave() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public String getSistemaLanzamiento() {
		return sistemaLanzamiento;
	}

	public void setSistemaLanzamiento(String sistemaLanzamiento) {
		this.sistemaLanzamiento = sistemaLanzamiento;
	}

	public float getLongitud() {
		return longitud;
	}

	public void setLongitud(float longitud) {
		this.longitud = longitud;
	}

	public int getMasaSeco() {
		return masaSeco;
	}

	public void setMasaSeco(int masaSeco) {
		this.masaSeco = masaSeco;
	}

	public int getMasaLanzamiento() {
		return masaLanzamiento;
	}

	public void setMasaLanzamiento(int masaLanzamiento) {
		this.masaLanzamiento = masaLanzamiento;
	}

	public int getCargaUtil() {
		return cargaUtil;
	}

	public void setCargaUtil(int cargaUtil) {
		this.cargaUtil = cargaUtil;
	}

	public float getVolumenUtil() {
		return volumenUtil;
	}

	public void setVolumenUtil(float volumenUtil) {
		this.volumenUtil = volumenUtil;
	}

	public int getCargaUtilRetorno() {
		return cargaUtilRetorno;
	}

	public void setCargaUtilRetorno(int cargaUtilRetorno) {
		this.cargaUtilRetorno = cargaUtilRetorno;
	}

	public float getDiametro() {
		return diametro;
	}

	public void setDiametro(float diametro) {
		this.diametro = diametro;
	}

	public int getPotencia() {
		return potencia;
	}

	public void setPotencia(int potencia) {
		this.potencia = potencia;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return "Nave [id=" + id + ", nombre=" + nombre + ", pais=" + pais + ", fabricante=" + fabricante
				+ ", sistemaLanzamiento=" + sistemaLanzamiento + ", longitud=" + longitud + ", masaSeco=" + masaSeco
				+ ", masaLanzamiento=" + masaLanzamiento + ", cargaUtil=" + cargaUtil + ", volumenUtil=" + volumenUtil
				+ ", cargaUtilRetorno=" + cargaUtilRetorno + ", diametro=" + diametro + ", potencia=" + potencia
				+ ", estado=" + estado + "]";
	}

}