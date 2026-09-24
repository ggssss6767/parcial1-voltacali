package co.edu.usc.voltacali;

import co.edu.usc.voltacali.CargadorVE.TipoCargador;
import co.edu.usc.voltacali.CargadorVE.TipoConector;
import co.edu.usc.voltacali.CargadorVE.Ubicacion;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Locale;

/** Ejecuta en orden el caso obligatorio del parcial. */
public final class App {
    private static final int N = 28;
    private static final int BASE_DECIMAL = 10;
    private static final int NUMERO_RUTAS = 4;
    private static final int MODULO_CONECTORES = 3;
    private static final int ANIO_BASE = 2015;
    private static final int VOLTAJE_PAR = 220;
    private static final int VOLTAJE_IMPAR = 400;
    private static final int POTENCIA_BASE = 20;
    private static final int INCREMENTO_BASE = 5;
    private static String paso = "P01";

    private App() {
    }

    /** Incluye el codigo tambien en los mensajes producidos por el modelo. */
    private static void etiquetarSalida() {
        final PrintStream consola = System.out;
        System.setOut(new PrintStream(new OutputStream() {
            private boolean inicioLinea = true;

            @Override
            public void write(int valor) {
                if (inicioLinea && valor != '\r' && valor != '\n') {
                    consola.print("[" + paso + "] ");
                    inicioLinea = false;
                }
                consola.write(valor);
                if (valor == '\n') {
                    inicioLinea = true;
                }
            }

            @Override
            public void flush() {
                consola.flush();
            }
        }, true));
    }

    private static void potencia(String id, CargadorVE cargador) {
        System.out.println("Potencia " + id + ": " + cargador.getPotenciaActual() + " kW");
    }

    private static void tiempo(double horas) {
        System.out.printf(Locale.US, "Tiempo estimado: %.2f horas%n", horas);
    }

    private static void cantidades(CargadorVE[] flota) {
        int[] conteo = CargadorVE.contarPorTipo(flota);
        for (TipoCargador tipo : TipoCargador.values()) {
            System.out.println(tipo + ": " + conteo[tipo.ordinal()]);
        }
    }

    private static void fabricantes(String criterio, CargadorVE[] resultado) {
        StringBuilder nombres = new StringBuilder();
        for (CargadorVE cargador : resultado) {
            if (nombres.length() > 0) {
                nombres.append(", ");
            }
            nombres.append(cargador.getFabricante());
        }
        System.out.println(criterio + ": " + resultado.length + " | "
                + (resultado.length == 0 ? "Sin resultados" : nombres));
    }

    private static void mayor(CargadorVE[] flota) {
        CargadorVE cargador = CargadorVE.mayorPotencia(flota);
        System.out.println(cargador == null ? "Sin cargadores" : "Mayor potencia: "
                + cargador.getFabricante() + " | " + cargador.getPotenciaActual() + " kW");
    }

    /** Ejecuta las partes E, la ruta cero y la extension F, en ese orden. */
    public static void main(String[] args) {
        PrintStream salidaOriginal = System.out;
        etiquetarSalida();
        try {
            CargadorVE[] flota = crearFlota();
            sesionC1(flota[0]);
            restoFlota(flota);
            estadisticas(flota);
            rutaIndividual(flota);
            extensionPersonalizada(flota);
        } finally {
            System.out.flush();
            System.setOut(salidaOriginal);
        }
    }

    // Los numeros de esta seccion son datos de entrada exigidos por la Parte E.
    @SuppressWarnings("checkstyle:MagicNumber")
    private static CargadorVE[] crearFlota() {
        CargadorVE c1 = new CargadorVE("ABB", 2023, 400, TipoConector.CCS2,
                TipoCargador.RAPIDO_DC, 2, 2, 60, Ubicacion.UNIVERSIDAD);
        CargadorVE c2 = new CargadorVE("Siemens", 2022, 220, TipoConector.TIPO_2,
                TipoCargador.MURAL, 1, 1, 22, Ubicacion.CENTRO_COMERCIAL);
        CargadorVE c3 = new CargadorVE("Delta", 2024, 800, TipoConector.CCS2,
                TipoCargador.ULTRARRAPIDO, 2, 2, 150, Ubicacion.ESTACION_SERVICIO);
        CargadorVE c4 = new CargadorVE("Wallbox", 2021, 220, TipoConector.TIPO_2,
                TipoCargador.MURAL, 1, 1, 11, Ubicacion.RESIDENCIAL);
        CargadorVE c5 = new CargadorVE("Enel X", 2025, 22);
        return new CargadorVE[] {c1, c2, c3, c4, c5};
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    private static void sesionC1(CargadorVE c1) {

        paso = "P01";
        c1.setPotenciaActual(40);
        potencia("C1", c1);
        paso = "P02";
        c1.aumentarPotencia(15);
        potencia("C1", c1);
        paso = "P03";
        tiempo(c1.tiempoEstimadoCarga(66));
        paso = "P04";
        c1.aumentarPotencia(10);
        potencia("C1", c1);
        paso = "P05";
        c1.reducirPotencia(30);
        potencia("C1", c1);
        paso = "P06";
        tiempo(c1.tiempoEstimadoCarga(50, 2, 15));
        paso = "P07";
        tiempo(c1.tiempoEstimadoCarga(50, 40.0));
        paso = "P08";
        c1.aumentarPotencia();
        potencia("C1", c1);
        paso = "P09";
        c1.aumentarPotencia(5, 3);
        potencia("C1", c1);
        paso = "P10";
        c1.reducirPotencia(50);
        potencia("C1", c1);
        paso = "P11";
        c1.cortarCarga();
        potencia("C1", c1);
        paso = "P12";
        tiempo(c1.tiempoEstimadoCarga(10));

    }

    @SuppressWarnings("checkstyle:MagicNumber")
    private static void restoFlota(CargadorVE[] flota) {
        CargadorVE c2 = flota[1];
        CargadorVE c3 = flota[2];
        CargadorVE c4 = flota[3];
        CargadorVE c5 = flota[4];
        paso = "P13";
        c2.setPotenciaActual(22);
        c3.setPotenciaActual(120);
        c4.aumentarPotencia(7.4);
        c5.aumentarPotencia(30);
        potencia("C2", c2);
        potencia("C3", c3);
        potencia("C4", c4);
        potencia("C5", c5);
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    private static void estadisticas(CargadorVE[] flota) {
        CargadorVE c1 = flota[0];
        CargadorVE c3 = flota[2];
        CargadorVE c5 = flota[4];
        paso = "P14";
        cantidades(flota);
        paso = "P15";
        System.out.printf(Locale.US, "Promedio: %.2f kW%n", CargadorVE.promedioPotencia(flota));
        paso = "P16";
        mayor(flota);
        paso = "P17";
        System.out.println("Excesos validos: " + CargadorVE.excesosDePotenciaContratada(flota));
        paso = "P18";
        fabricantes("TIPO_2", CargadorVE.filtrar(flota, TipoConector.TIPO_2));
        fabricantes("MURAL", CargadorVE.filtrar(flota, TipoCargador.MURAL));
        fabricantes("UNIVERSIDAD", CargadorVE.filtrar(flota, Ubicacion.UNIVERSIDAD));
        paso = "P19";
        c5.mostrar(false);
        paso = "P20";
        CargadorVE copia = new CargadorVE(c3);
        System.out.println("Copia: " + copia.getFabricante() + "; potencia: " + copia.getPotenciaActual()
                + " kW; registros: " + copia.getCantidadRegistrosBitacora()
                + "; total cargadores: " + CargadorVE.getTotalCargadores());
        paso = "P21";
        c1.mostrar(true);
        paso = "P22";
        System.out.println("Contador global de registros: " + CargadorVE.getContadorRegistros());
        paso = "P23";
        System.out.println("Promedio con null: " + CargadorVE.promedioPotencia(new CargadorVE[] {c1, null, c3}));
        System.out.println("Conteo de arreglo null: " + Arrays.toString(CargadorVE.contarPorTipo(null)));

    }

    private static void rutaIndividual(CargadorVE[] flota) {
        paso = "R";
        int ruta = N % NUMERO_RUTAS;
        int conectores = N % MODULO_CONECTORES + 1;
        System.out.println("N = " + N + "; r = " + ruta
                + "; ruta 0: cargadores con " + conectores + " conectores");
        fabricantes("Resultado", CargadorVE.cargadoresPorConectores(flota, conectores));
    }

    private static CargadorVE crearC6(int numero) {
        int d1 = numero / BASE_DECIMAL;
        int d2 = numero % BASE_DECIMAL;
        return new CargadorVE("USC-" + numero, ANIO_BASE + d2,
                numero % 2 == 0 ? VOLTAJE_PAR : VOLTAJE_IMPAR,
                TipoConector.values()[numero % TipoConector.values().length],
                TipoCargador.values()[numero % TipoCargador.values().length],
                d1 % MODULO_CONECTORES + 1, d2 % NUMERO_RUTAS + 1,
                POTENCIA_BASE + numero,
                Ubicacion.values()[numero % Ubicacion.values().length]);
    }

    private static void extensionPersonalizada(CargadorVE[] flota) {
        int d1 = N / BASE_DECIMAL;
        int d2 = N % BASE_DECIMAL;
        CargadorVE c6 = crearC6(N);
        paso = "X01";
        System.out.println("N = " + N + "; d1 = " + d1 + "; d2 = " + d2);
        c6.mostrar(false);
        paso = "X02";
        c6.setPotenciaActual(c6.getPotenciaMaxima() / 2);
        int rechazadosAntes = c6.contarIntentosInvalidos();
        c6.aumentarPotencia(d2 + INCREMENTO_BASE, d1 + 1);
        potencia("C6", c6);
        System.out.println("Algun paso rechazado: "
                + (c6.contarIntentosInvalidos() > rechazadosAntes ? "si" : "no"));
        paso = "X03";
        tiempo(c6.tiempoEstimadoCarga(N + BASE_DECIMAL));
        paso = "X04";
        CargadorVE[] flotaExtendida = new CargadorVE[flota.length + 1];
        for (int i = 0; i < flota.length; i++) {
            flotaExtendida[i] = flota[i];
        }
        flotaExtendida[flota.length] = c6;
        System.out.println("Flota extendida: " + flotaExtendida.length + " cargadores");
        paso = "X05";
        cantidades(flotaExtendida);
        System.out.printf(Locale.US, "Promedio: %.2f kW%n", CargadorVE.promedioPotencia(flotaExtendida));
        mayor(flotaExtendida);
        System.out.println("Excesos validos: " + CargadorVE.excesosDePotenciaContratada(flotaExtendida));
        paso = "X06";
        System.out.println("Total cargadores: " + CargadorVE.getTotalCargadores());
        System.out.println("Contador global de registros: " + CargadorVE.getContadorRegistros());
        c6.mostrar(true);
    }
}
