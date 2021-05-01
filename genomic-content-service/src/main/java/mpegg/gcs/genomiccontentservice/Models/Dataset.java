package mpegg.gcs.genomiccontentservice.Models;

import mpegg.gcs.genomiccontentservice.Models.DatasetGroup;

import javax.persistence.*;

@Entity
@Table(name="dataset")
public class Dataset {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Integer dt_id;

    private String title;

    private String taxon_id;

    private String path;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "datasetgroup_id", nullable = false)
    private DatasetGroup datasetGroup;

    public Dataset() {
    }

    public Dataset(DatasetGroup datasetGroup) {
        this.datasetGroup = datasetGroup;
    }

    public Long getId() {
        return id;
    }

    public DatasetGroup getDatasetGroup() {
        return datasetGroup;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTaxon_id() {
        return taxon_id;
    }

    public void setTaxon_id(String taxon_id) {
        this.taxon_id = taxon_id;
    }

    public Integer getDt_id() {
        return dt_id;
    }

    public void setDt_id(Integer dt_id) {
        this.dt_id = dt_id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}