package br.com.futmatch.application.dtos.requests;

import java.time.LocalDateTime;

public class PartidaFiltroRequest {
    private String esporte;
    private String tipoPartida;
    private Double latitude;
    private Double longitude;
    private Double raioKm;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Boolean apenasFuturas;
    private Integer page;
    private Integer size;

    public String getEsporte() { return esporte; }
    public void setEsporte(String esporte) { this.esporte = esporte; }
    public String getTipoPartida() { return tipoPartida; }
    public void setTipoPartida(String tipoPartida) { this.tipoPartida = tipoPartida; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Double getRaioKm() { return raioKm; }
    public void setRaioKm(Double raioKm) { this.raioKm = raioKm; }
    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }
    public LocalDateTime getDataFim() { return dataFim; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }
    public Boolean getApenasFuturas() { return apenasFuturas; }
    public void setApenasFuturas(Boolean apenasFuturas) { this.apenasFuturas = apenasFuturas; }
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
}
