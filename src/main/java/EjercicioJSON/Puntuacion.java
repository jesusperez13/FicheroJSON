package EjercicioJSON;

public class Puntuacion implements Comparable<Puntuacion> {
    private String jugador;
    private int puntos;
    public Puntuacion(String jugador, int puntos) {
        this.jugador = jugador;
        this.puntos = puntos;
    }
    public String getJugador() {
        return jugador;
    }
    public int getPuntos() {
        return puntos;
    }
    @Override
    public int compareTo(Puntuacion otra) {
        return Integer.compare(otra.puntos, this.puntos);
    }
}