package com.ineedserv.ineed;

/**
 * Created by andrade on 05-07-17.
 */

public class Solicitudes {
    String pais;
    String fechaFinSolicitud;
    String fechaSolicitud;
    String fechaAceptacion;
    String fechaConfirmacion;
    String fechaHoraCita;
    String instanciaSolicitante;
    String nombreSolicitante;
    String instanciaOfertante;
    double latSolicitante;
    double lonSolicitante;
    double distancia;
    String servicio;
    String estado;

    public String getNombreSolicitante() {
        return nombreSolicitante;
    }

    public void setNombreSolicitante(String nombreSolicitante) {
        this.nombreSolicitante = nombreSolicitante;
    }

    public String getFechaHoraCita() {
        return fechaHoraCita;
    }

    public void setFechaHoraCita(String fechaHoraCita) {
        this.fechaHoraCita = fechaHoraCita;
    }

    public String getFechaConfirmacion() {
        return fechaConfirmacion;
    }

    public void setFechaConfirmacion(String fechaConfirmacion) {
        this.fechaConfirmacion = fechaConfirmacion;
    }

    public String getFechaAceptacion() {
        return fechaAceptacion;
    }

    public void setFechaAceptacion(String fechaAceptacion) {
        this.fechaAceptacion = fechaAceptacion;
    }

    public String getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(String fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getFechaFinSolicitud() {
        return fechaFinSolicitud;
    }

    public void setFechaFinSolicitud(String fechaFinSolicitud) {
        this.fechaFinSolicitud = fechaFinSolicitud;
    }

    public void setInstanciaSolicitante(String instanciaSolicitante) {
        this.instanciaSolicitante = instanciaSolicitante;
    }

    public String getInstanciaSolicitante() {
        return instanciaSolicitante;
    }

    public void setInstanciaOfertante(String instanciaOfertante) {
        this.instanciaOfertante = instanciaOfertante;
    }

    public String getInstanciaOfertante() {
        return instanciaOfertante;
    }

    public double getLatSolicitante() {
        return latSolicitante;
    }

    public void setLatSolicitante(double latSolicitante) {
        this.latSolicitante = latSolicitante;
    }

    public double getLonSolicitante() {
        return lonSolicitante;
    }

    public void setLonSolicitante(double lonSolicitante) {
        this.lonSolicitante = lonSolicitante;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getServicio() {
        return servicio;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }
}