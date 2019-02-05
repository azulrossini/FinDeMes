package model;

import java.util.Date;
import java.util.List;

public class Movimiento {

    private int id;

    private String titulo;

    private Double monto;

    private String descripcion;

    private Categoria categoria;

    private Date fecha;

    private FrecuenciaEnum frecuenciaEnum;

    private Date fechaFinalizacion;

    private List<Date> listaFechas;

}
