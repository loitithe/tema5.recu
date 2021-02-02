
public class Student {

	public  int codigo = 0;
	private String nombre;
	private String apellidos;
	private int altura;
	private int aula;
	/**
	 * 
	 * @param codigo
	 * @param nombre
	 * @param apellidos
	 * @param altura
	 * @param aula
	 */
	public Student(int codigo,String nombre, String apellidos, int altura, int aula) {
		super();
		this.codigo=codigo;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.altura = altura;
		this.aula = aula;
	}
	/**
	 * 
	 * @param nombre
	 * @param apellidos
	 * @param altura
	 * @param aula
	 */
	public Student(String nombre, String apellidos, int altura, int aula) {
		// TODO Auto-generated constructor stub
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.altura = altura;
		this.aula = aula;
	}
	/**
	 * 
	 * @return
	 */
	public  int getCodigo() {
		return codigo;
	}
	/**
	 * 
	 * @param codigo
	 */
	public  void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	/**
	 * 
	 * @return
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * 
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * 
	 * @return
	 */
	public String getApellidos() {
		return apellidos;
	}
	/**
	 * 
	 * @param apellidos
	 */
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	/**
	 * 
	 * @return
	 */
	public int getAltura() {
		return altura;
	}
	/**
	 * 
	 * @param altura
	 */
	public void setAltura(short altura) {
		this.altura = altura;
	}
	/**
	 * 
	 * @return
	 */
	public int getAula() {
		return aula;
	}
	/**
	 * 
	 * @param aula
	 */
	public void setAula(short aula) {
		this.aula = aula;
	}
	
	
	

}
