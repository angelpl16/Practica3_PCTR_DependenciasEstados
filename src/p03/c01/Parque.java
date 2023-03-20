package src.p03.c01;

import java.util.Enumeration;
import java.util.Hashtable;

public class Parque implements IParque {

	// TODO
	private int contadorPersonasTotales;
	// El contador se utilizará unicamente para las personas que entran
	private Hashtable<String, Integer> contadoresPersonasPuertaEntrar;
	private Hashtable<String, Integer> contadoresPersonasPuertaSalir;

	private int maxPersonas = 50;
	private boolean entrada = false;
	private boolean salida = false;

	private boolean bloqueoEntrada = false;

	public Parque() {
		contadorPersonasTotales = 0;
		contadoresPersonasPuertaEntrar = new Hashtable<String, Integer>();
	}

	@Override
	public void entrarAlParque(String puerta) { // TODO

		// Si no hay entradas por esa puerta, inicializamos
		if (contadoresPersonasPuertaEntrar.get(puerta) == null) {
			contadoresPersonasPuertaEntrar.put(puerta, 0);
		}

		// TODO Precondiciones

		comprobarAntesDeEntrar();

		if (entrada) {

			// Se bloquea la entrada para que no se modifique la misma información en
			// diferentes hilos
			bloqueoEntrada = true;
			// Aumentamos el contador total y el individual
			contadorPersonasTotales++;
			contadoresPersonasPuertaEntrar.put(puerta, contadoresPersonasPuertaEntrar.get(puerta) + 1);

			// Imprimimos el estado del parque
			imprimirInfo(puerta, "Entrada");

			checkInvariante();

			bloqueoEntrada = false;
			notifyAll();
		}

	}

	//
	// TODO Método salirDelParque
	//

	public void salirDelParque(String puerta) {

	}

	private void imprimirInfo(String puerta, String movimiento) {
		System.out.println(movimiento + " por puerta " + puerta);
		System.out.println("--> Personas en el parque " + contadorPersonasTotales); // + " tiempo medio de estancia: " +
																					// tmedio);

		// Iteramos por todas las puertas e imprimimos sus entradas
		for (String p : contadoresPersonasPuertaEntrar.keySet()) {
			System.out.println("----> Por puerta " + p + " " + contadoresPersonasPuertaEntrar.get(p));
		}
		System.out.println(" ");
	}

	// Se deberán contar los contadores de las puertas de entrada y salida.
	private int sumarContadoresPuerta(Hashtable<String, Integer> contadoresPersonasPuerta) {
		int sumaContadoresPuerta = 0;
		Enumeration<Integer> iterPuertas = contadoresPersonasPuerta.elements();
		while (iterPuertas.hasMoreElements()) {
			sumaContadoresPuerta += iterPuertas.nextElement();
		}
		return sumaContadoresPuerta;
	}

	protected void checkInvariante() {
		assert sumarContadoresPuerta(contadoresPersonasPuertaEntrar)
				- sumarContadoresPuerta(contadoresPersonasPuertaSalir) == contadorPersonasTotales
				: "INV: La suma de contadores de las puertas debe ser igual al valor del contador del parte";
		assert sumarContadoresPuerta(contadoresPersonasPuertaEntrar) >= sumarContadoresPuerta(
				contadoresPersonasPuertaSalir) : "INV: No pueden salir mas personas de las que han entrado";
		assert contadorPersonasTotales <= maxPersonas && contadorPersonasTotales >= 0 : "INV: Aforo incorrecto";

	}

	protected void comprobarAntesDeEntrar() {
		if (contadorPersonasTotales < maxPersonas && !bloqueoEntrada) {
			entrada = true;
		}

		while (contadorPersonasTotales == maxPersonas || bloqueoEntrada) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(e.toString());
			}
		}
	}

	protected void comprobarAntesDeSalir() {
		//
		// TODO
		//
	}

}
