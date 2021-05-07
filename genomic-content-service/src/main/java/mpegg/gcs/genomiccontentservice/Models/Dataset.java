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

    private Boolean metadata;

    private Boolean protection;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "datasetgroup_id", nullable = false)
    private DatasetGroup datasetGroup;

    public Dataset() {
    }

    public Dataset(DatasetGroup datasetGroup) {
        this.datasetGroup = datasetGroup;
        this.metadata = false;
        this.protection = false;
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

    public Boolean getMetadata() {
        return metadata;
    }

    public void setMetadata(Boolean metadata) {
        this.metadata = metadata;
    }

    public Boolean getProtection() {
        return protection;
    }

    public void setProtection(Boolean protection) {
        this.protection = protection;
    }
}