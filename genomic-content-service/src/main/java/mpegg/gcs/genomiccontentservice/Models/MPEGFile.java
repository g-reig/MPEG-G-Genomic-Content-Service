package mpegg.gcs.genomiccontentservice.Models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="mpegfile")
public class MPEGFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public MPEGFile() {
    }

    @Column(unique = true)
    private String path;

    private String owner;

    public MPEGFile(String owner) {
        this.owner = owner;
    }

    @OneToMany(mappedBy = "mpegfile", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<DatasetGroup> datasetGroups;

    public Long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Set<DatasetGroup> getDatasetGroups() {
        return datasetGroups;
    }

    public void setDatasetGroups(Set<DatasetGroup> datasetGroups) {
        this.datasetGroups = datasetGroups;
    }
}
