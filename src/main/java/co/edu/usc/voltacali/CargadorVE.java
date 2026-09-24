package co.edu.usc.voltacali;

import java.util.Vector;

/**
 * Representa un cargador para vehiculos electricos.
 */
public class CargadorVE {

    /**
     * Tipos de conectores disponibles.
     */
    public enum TipoConector {
        /** Conector tipo 1. */
        TIPO_1,

        /** Conector tipo 2. */
        TIPO_2,

        /** Conector CCS2. */
        CCS2,

        /** Conector CHAdeMO. */
        CHADEMO,

        /** Conector GBT. */
        GBT
    }

    /**
     * Tipos de cargadores disponibles.
     */
    public enum TipoCargador {
        /** Cargador mural. */
        MURAL,

        /** Cargador de pedestal. */
        PEDESTAL,

        /** Cargador rapido DC. */
        RAPIDO_DC,

        /** Cargador ultrarrapido. */
        ULTRARRAPIDO,

        /** Cargador portatil. */
        PORTATIL,

        /** Cargador bidireccional V2G. */
        BIDIRECCIONAL_V2G
    }

    /**
     * Ubicaciones posibles del cargador.
     */
    public enum Ubicacion {
        /** Centro comercial. */
        CENTRO_COMERCIAL,

        /** Universidad. */
        UNIVERSIDAD,

        /** Estacion de servicio. */
        ESTACION_SERVICIO,

        /** Parqueadero publico. */
        PARQUEADERO_PUBLICO,

        /** Zona residencial. */
        RESIDENCIAL,

        /** Hotel. */
        HOTEL,

        /** Terminal. */
        TERMINAL,

        /** Flota corporativa. */
        FLOTA_CORPORATIVA
    }

    /** Limite contratado de potencia de la red. */
    public static final double LIMITE_RED = 50.0;

    /** Incremento de potencia utilizado por defecto. */
    public static final double INCREMENTO_DEFECTO = 5.0;

    private static final int VOLTAJE_DEFECTO = 220;
    private static final double MINUTOS_POR_HORA = 60.0;

    private static int totalCargadores;
    private static int contadorRegistros;

    private String fabricante;
    private int anioInstalacion;
    private int voltajeNominal;
    private TipoConector tipoConector;
    private TipoCargador tipoCargador;
    private int numeroConectores;
    private int puestosParqueo;
    private double potenciaMaxima;
    private Ubicacion ubicacion;
    private double potenciaActual;

    private Vector<RegistroSesion> bitacora;

    /**
     * Representa un registro de una sesion o cambio de potencia.
     */
    public class RegistroSesion {

        private int numero;
        private String evento;
        private boolean valido;
        private String fabricanteRegistro;
        private int anioInstalacionRegistro;
        private double potenciaRegistro;

        /**
         * Crea un nuevo registro de sesion.
         */
        public RegistroSesion(String evento, boolean valido) {
            contadorRegistros++;

            this.numero = contadorRegistros;
            this.evento = evento;
            this.valido = valido;

            fabricanteRegistro = fabricante;
            anioInstalacionRegistro = anioInstalacion;
            potenciaRegistro = potenciaActual;
        }

        /**
         * Retorna la descripcion completa del registro.
         */
        public String describir() {
            return "Registro #" + numero
                    + " | Fabricante: " + fabricanteRegistro
                    + " | Anio: " + anioInstalacionRegistro
                    + " | Potencia: " + potenciaRegistro + " kW"
                    + " | Evento: " + evento
                    + " | Valido: " + valido;
        }

        /**
         * Indica si el registro corresponde a una operacion valida.
         */
        public boolean isValido() {
            return valido;
        }

        /**
         * Retorna la potencia almacenada en el registro.
         */
        public double getPotenciaRegistro() {
            return potenciaRegistro;
        }
    }

    /**
     * Construye un cargador con todos sus datos tecnicos.
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    public CargadorVE(
            String fabricante,
            int anioInstalacion,
            int voltajeNominal,
            TipoConector tipoConector,
            TipoCargador tipoCargador,
            int numeroConectores,
            int puestosParqueo,
            double potenciaMaxima,
            Ubicacion ubicacion) {

        this.fabricante = fabricante;
        this.anioInstalacion = anioInstalacion;
        this.voltajeNominal = voltajeNominal;
        this.tipoConector = tipoConector;
        this.tipoCargador = tipoCargador;
        this.numeroConectores = numeroConectores;
        this.puestosParqueo = puestosParqueo;
        this.potenciaMaxima = potenciaMaxima;
        this.ubicacion = ubicacion;
        potenciaActual = 0.0;
        bitacora = new Vector<RegistroSesion>();

        totalCargadores++;
    }

    /**
     * Construye un cargador usando los valores por defecto.
     */
    public CargadorVE(
            String fabricante,
            int anioInstalacion,
            double potenciaMaxima) {

        this(
                fabricante,
                anioInstalacion,
                VOLTAJE_DEFECTO,
                TipoConector.TIPO_2,
                TipoCargador.PEDESTAL,
                1,
                1,
                potenciaMaxima,
                Ubicacion.PARQUEADERO_PUBLICO
        );
    }

    /**
     * Construye una copia tecnica de otro cargador.
     */
    public CargadorVE(CargadorVE otro) {
        this(
                otro.fabricante,
                otro.anioInstalacion,
                otro.voltajeNominal,
                otro.tipoConector,
                otro.tipoCargador,
                otro.numeroConectores,
                otro.puestosParqueo,
                otro.potenciaMaxima,
                otro.ubicacion
        );
    }

    /**
     * Retorna el fabricante.
     */
    public String getFabricante() {
        return fabricante;
    }

    /**
     * Cambia el fabricante.
     */
    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    /**
     * Retorna el anio de instalacion.
     */
    public int getAnioInstalacion() {
        return anioInstalacion;
    }

    /**
     * Cambia el anio de instalacion.
     */
    public void setAnioInstalacion(int anioInstalacion) {
        this.anioInstalacion = anioInstalacion;
    }

    /**
     * Retorna el voltaje nominal.
     */
    public int getVoltajeNominal() {
        return voltajeNominal;
    }

    /**
     * Cambia el voltaje nominal.
     */
    public void setVoltajeNominal(int voltajeNominal) {
        this.voltajeNominal = voltajeNominal;
    }

    /**
     * Retorna el tipo de conector.
     */
    public TipoConector getTipoConector() {
        return tipoConector;
    }

    /**
     * Cambia el tipo de conector.
     */
    public void setTipoConector(TipoConector tipoConector) {
        this.tipoConector = tipoConector;
    }

    /**
     * Retorna el tipo de cargador.
     */
    public TipoCargador getTipoCargador() {
        return tipoCargador;
    }

    /**
     * Cambia el tipo de cargador.
     */
    public void setTipoCargador(TipoCargador tipoCargador) {
        this.tipoCargador = tipoCargador;
    }

    /**
     * Retorna la cantidad de conectores.
     */
    public int getNumeroConectores() {
        return numeroConectores;
    }

    /**
     * Cambia la cantidad de conectores.
     */
    public void setNumeroConectores(int numeroConectores) {
        this.numeroConectores = numeroConectores;
    }

    /**
     * Retorna los puestos de parqueo.
     */
    public int getPuestosParqueo() {
        return puestosParqueo;
    }

    /**
     * Cambia los puestos de parqueo.
     */
    public void setPuestosParqueo(int puestosParqueo) {
        this.puestosParqueo = puestosParqueo;
    }

    /**
     * Retorna la potencia maxima.
     */
    public double getPotenciaMaxima() {
        return potenciaMaxima;
    }

    /**
     * Cambia la potencia maxima.
     */
    public void setPotenciaMaxima(double potenciaMaxima) {
        this.potenciaMaxima = potenciaMaxima;
    }

    /**
     * Retorna la ubicacion.
     */
    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    /**
     * Cambia la ubicacion.
     */
    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    /**
     * Retorna la potencia actual.
     */
    public double getPotenciaActual() {
        return potenciaActual;
    }

    /**
     * Cambia la potencia actual si el valor es valido.
     */
    public void setPotenciaActual(double nuevaPotencia) {
        if (nuevaPotencia < 0 || nuevaPotencia > potenciaMaxima) {
            System.out.println(
                    "Potencia invalida. Debe estar entre 0 y "
                            + potenciaMaxima + " kW."
            );

            registrarEvento(
                    "Intento invalido de establecer potencia en "
                            + nuevaPotencia + " kW",
                    false
            );
            return;
        }

        potenciaActual = nuevaPotencia;

        registrarEvento(
                "Potencia establecida en " + nuevaPotencia + " kW",
                true
        );
    }

    /**
     * Registra un evento en la bitacora del cargador.
     */
    private void registrarEvento(String evento, boolean valido) {
        bitacora.add(new RegistroSesion(evento, valido));
    }

    /**
     * Retorna la cantidad de registros de la bitacora.
     */
    public int getCantidadRegistrosBitacora() {
        return bitacora.size();
    }

    /**
     * Aumenta la potencia usando el incremento por defecto.
     */
    public void aumentarPotencia() {
        aumentarPotencia(INCREMENTO_DEFECTO);
    }

    /**
     * Aumenta la potencia una cantidad determinada.
     */
    public void aumentarPotencia(double incremento) {
        double nuevaPotencia = potenciaActual + incremento;

        if (nuevaPotencia > potenciaMaxima || nuevaPotencia < 0) {
            System.out.println(
                    "No se puede aumentar la potencia. "
                            + "El resultado seria "
                            + nuevaPotencia + " kW."
            );

            registrarEvento(
                    "Intento invalido de aumentar potencia en "
                            + incremento + " kW",
                    false
            );
            return;
        }

        potenciaActual = nuevaPotencia;

        registrarEvento(
                "Aumento de potencia en " + incremento + " kW",
                true
        );
    }

    /**
     * Aumenta la potencia varias veces paso a paso.
     */
    public void aumentarPotencia(double incremento, int veces) {
        for (int i = 0; i < veces; i++) {
            double nuevaPotencia = potenciaActual + incremento;

            if (nuevaPotencia > potenciaMaxima
                    || nuevaPotencia < 0) {

                System.out.println(
                        "Paso " + (i + 1)
                                + " rechazado. La potencia resultante seria "
                                + nuevaPotencia + " kW."
                );

                registrarEvento(
                        "Paso " + (i + 1)
                                + " invalido al aumentar "
                                + incremento + " kW",
                        false
                );
                break;
            }

            potenciaActual = nuevaPotencia;

            registrarEvento(
                    "Paso " + (i + 1)
                            + " de aumento en "
                            + incremento + " kW",
                    true
            );
        }
    }

    /**
     * Reduce la potencia del cargador.
     */
    public void reducirPotencia(double reduccion) {
        double nuevaPotencia = potenciaActual - reduccion;

        if (nuevaPotencia < 0
                || nuevaPotencia > potenciaMaxima) {

            System.out.println(
                    "No se puede reducir la potencia. "
                            + "El resultado seria "
                            + nuevaPotencia + " kW."
            );

            registrarEvento(
                    "Intento invalido de reducir potencia en "
                            + reduccion + " kW",
                    false
            );
            return;
        }

        potenciaActual = nuevaPotencia;

        registrarEvento(
                "Reduccion de potencia en "
                        + reduccion + " kW",
                true
        );
    }

    /**
     * Detiene completamente la carga.
     */
    public void cortarCarga() {
        potenciaActual = 0.0;

        registrarEvento(
                "Carga cortada. Potencia establecida en 0 kW",
                true
        );
    }

    /**
     * Calcula el tiempo de carga usando la potencia actual.
     */
    public double tiempoEstimadoCarga(double energiaKWh) {
        if (potenciaActual == 0) {
            System.out.println(
                    "No se puede calcular el tiempo: "
                            + "la potencia actual es 0."
            );
            return -1;
        }

        return energiaKWh / potenciaActual;
    }

    /**
     * Calcula el tiempo usando una potencia programada.
     */
    public double tiempoEstimadoCarga(
            double energiaKWh,
            double potenciaProgramada) {

        if (potenciaProgramada <= 0) {
            System.out.println(
                    "La potencia programada debe ser mayor que 0."
            );
            return -1;
        }

        return energiaKWh / potenciaProgramada;
    }

    /**
     * Calcula el tiempo de carga incluyendo pausas.
     */
    public double tiempoEstimadoCarga(
            double energiaKWh,
            int pausas,
            double minutosPorPausa) {

        double tiempoBase = tiempoEstimadoCarga(energiaKWh);

        if (tiempoBase == -1) {
            return -1;
        }

        double tiempoPausas =
                pausas * minutosPorPausa / MINUTOS_POR_HORA;

        return tiempoBase + tiempoPausas;
    }

    /**
     * Muestra todos los atributos del cargador.
     */
    public void mostrar() {
        mostrar(false);
    }

    /**
     * Muestra los atributos y opcionalmente la bitacora.
     */
    public void mostrar(boolean detallado) {
        System.out.println("Fabricante: " + fabricante);
        System.out.println(
                "Anio instalacion: " + anioInstalacion
        );
        System.out.println(
                "Voltaje nominal: " + voltajeNominal + " V"
        );
        System.out.println(
                "Tipo conector: " + tipoConector
        );
        System.out.println(
                "Tipo cargador: " + tipoCargador
        );
        System.out.println(
                "Numero conectores: " + numeroConectores
        );
        System.out.println(
                "Puestos parqueo: " + puestosParqueo
        );
        System.out.println(
                "Potencia maxima: " + potenciaMaxima + " kW"
        );
        System.out.println(
                "Ubicacion: " + ubicacion
        );
        System.out.println(
                "Potencia actual: " + potenciaActual + " kW"
        );

        if (detallado) {
            System.out.println("----- BITACORA -----");

            if (bitacora.isEmpty()) {
                System.out.println("Bitacora vacia.");
            } else {
                for (RegistroSesion registro : bitacora) {
                    System.out.println(registro.describir());
                }
            }
        }
    }

    /**
     * Retorna el total de cargadores creados.
     */
    public static int getTotalCargadores() {
        return totalCargadores;
    }

    /**
     * Retorna el contador global de registros.
     */
    public static int getContadorRegistros() {
        return contadorRegistros;
    }

    /**
     * Cuenta los cargadores existentes para cada tipo.
     */
    public static int[] contarPorTipo(CargadorVE[] cargadores) {
        int[] cantidades =
                new int[TipoCargador.values().length];

        if (cargadores == null) {
            return cantidades;
        }

        for (CargadorVE cargador : cargadores) {
            if (cargador != null
                    && cargador.tipoCargador != null) {

                cantidades[cargador.tipoCargador.ordinal()]++;
            }
        }

        return cantidades;
    }

    /**
     * Retorna el cargador que posee mayor potencia actual.
     */
    public static CargadorVE mayorPotencia(
            CargadorVE[] cargadores) {

        if (cargadores == null) {
            return null;
        }

        CargadorVE mayor = null;

        for (CargadorVE cargador : cargadores) {
            if (cargador == null) {
                continue;
            }

            if (mayor == null
                    || cargador.potenciaActual
                    > mayor.potenciaActual) {

                mayor = cargador;
            }
        }

        return mayor;
    }

    /**
     * Calcula la potencia promedio de los cargadores.
     */
    public static double promedioPotencia(
            CargadorVE[] cargadores) {

        if (cargadores == null) {
            return 0.0;
        }

        double suma = 0.0;
        int cantidad = 0;

        for (CargadorVE cargador : cargadores) {
            if (cargador != null) {
                suma += cargador.potenciaActual;
                cantidad++;
            }
        }

        if (cantidad == 0) {
            return 0.0;
        }

        return suma / cantidad;
    }

    /**
     * Cuenta registros validos que superan el limite de red.
     */
    public static int excesosDePotenciaContratada(
            CargadorVE[] cargadores) {

        if (cargadores == null) {
            return 0;
        }

        int cantidad = 0;

        for (CargadorVE cargador : cargadores) {
            if (cargador == null) {
                continue;
            }

            for (RegistroSesion registro
                    : cargador.bitacora) {

                if (registro.valido
                        && registro.potenciaRegistro
                        > LIMITE_RED) {

                    cantidad++;
                }
            }
        }

        return cantidad;
    }

    /**
     * Filtra cargadores por tipo de conector.
     */
    public static CargadorVE[] filtrar(
            CargadorVE[] cargadores,
            TipoConector conector) {

        if (cargadores == null) {
            return new CargadorVE[0];
        }

        int cantidad = 0;

        for (CargadorVE cargador : cargadores) {
            if (cargador != null
                    && cargador.tipoConector == conector) {

                cantidad++;
            }
        }

        CargadorVE[] resultado =
                new CargadorVE[cantidad];

        int posicion = 0;

        for (CargadorVE cargador : cargadores) {
            if (cargador != null
                    && cargador.tipoConector == conector) {

                resultado[posicion] = cargador;
                posicion++;
            }
        }

        return resultado;
    }

    /**
     * Filtra cargadores por tipo de cargador.
     */
    public static CargadorVE[] filtrar(
            CargadorVE[] cargadores,
            TipoCargador tipo) {

        if (cargadores == null) {
            return new CargadorVE[0];
        }

        int cantidad = 0;

        for (CargadorVE cargador : cargadores) {
            if (cargador != null
                    && cargador.tipoCargador == tipo) {

                cantidad++;
            }
        }

        CargadorVE[] resultado =
                new CargadorVE[cantidad];

        int posicion = 0;

        for (CargadorVE cargador : cargadores) {
            if (cargador != null
                    && cargador.tipoCargador == tipo) {

                resultado[posicion] = cargador;
                posicion++;
            }
        }

        return resultado;
    }

    /**
     * Filtra cargadores por ubicacion.
     */
    public static CargadorVE[] filtrar(
            CargadorVE[] cargadores,
            Ubicacion ubicacionBuscada) {

        if (cargadores == null) {
            return new CargadorVE[0];
        }

        int cantidad = 0;

        for (CargadorVE cargador : cargadores) {
            if (cargador != null
                    && cargador.ubicacion == ubicacionBuscada) {

                cantidad++;
            }
        }

        CargadorVE[] resultado =
                new CargadorVE[cantidad];

        int posicion = 0;

        for (CargadorVE cargador : cargadores) {
            if (cargador != null
                    && cargador.ubicacion == ubicacionBuscada) {

                resultado[posicion] = cargador;
                posicion++;
            }
        }

        return resultado;
    }

    /**
     * Filtra los cargadores por cantidad de conectores.
     */
    public static CargadorVE[] cargadoresPorConectores(
            CargadorVE[] flota,
            int conectores) {

        if (flota == null) {
            return new CargadorVE[0];
        }

        int cantidad = 0;

        for (CargadorVE cargador : flota) {
            if (cargador != null
                    && cargador.numeroConectores == conectores) {

                cantidad++;
            }
        }

        CargadorVE[] resultado =
                new CargadorVE[cantidad];

        int posicion = 0;

        for (CargadorVE cargador : flota) {
            if (cargador != null
                    && cargador.numeroConectores == conectores) {

                resultado[posicion] = cargador;
                posicion++;
            }
        }

        return resultado;
    }

    /**
     * Calcula el voltaje promedio para un tipo de conector.
     */
    public static double promedioVoltajePorConector(
            CargadorVE[] flota,
            TipoConector conector) {

        if (flota == null) {
            return -1;
        }

        double suma = 0.0;
        int cantidad = 0;

        for (CargadorVE cargador : flota) {
            if (cargador != null
                    && cargador.tipoConector == conector) {

                suma += cargador.voltajeNominal;
                cantidad++;
            }
        }

        if (cantidad == 0) {
            return -1;
        }

        return suma / cantidad;
    }

    /**
     * Retorna todos los tipos de cargador mas frecuentes.
     */
    public static TipoCargador[] tipoMasFrecuente(
            CargadorVE[] flota) {

        int[] cantidades = contarPorTipo(flota);
        int maximo = 0;

        for (int cantidad : cantidades) {
            if (cantidad > maximo) {
                maximo = cantidad;
            }
        }

        if (maximo == 0) {
            return new TipoCargador[0];
        }

        int empatados = 0;

        for (int cantidad : cantidades) {
            if (cantidad == maximo) {
                empatados++;
            }
        }

        TipoCargador[] resultado =
                new TipoCargador[empatados];

        int posicion = 0;

        for (int i = 0; i < cantidades.length; i++) {
            if (cantidades[i] == maximo) {
                resultado[posicion] =
                        TipoCargador.values()[i];
                posicion++;
            }
        }

        return resultado;
    }

    /**
     * Muestra y cuenta las sesiones validas sobre el limite de red.
     */
    public static int sesionesSobreLimiteRed(
            CargadorVE[] flota) {

        if (flota == null) {
            return 0;
        }

        int cantidad = 0;

        for (CargadorVE cargador : flota) {
            if (cargador == null) {
                continue;
            }

            for (RegistroSesion registro
                    : cargador.bitacora) {

                if (registro.valido
                        && registro.potenciaRegistro
                        > LIMITE_RED) {

                    System.out.println(
                            registro.describir()
                    );
                    cantidad++;
                }
            }
        }

        return cantidad;
    }
}
