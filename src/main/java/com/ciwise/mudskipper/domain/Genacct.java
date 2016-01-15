package com.ciwise.mudskipper.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Genacct.
 */
@Entity
@Table(name = "genacct")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "genacct")
public class Genacct implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 128)
    @Column(name = "name", length = 128, nullable = false)
    private String name;

    @NotNull
    @Size(max = 80)
    @Column(name = "type", length = 80, nullable = false)
    private String type;

    @NotNull
    @Column(name = "acctno", nullable = false)
    private Integer acctno;

    @OneToMany(mappedBy = "genacct")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Genacctentry> genacctentrys = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAcctno() {
        return acctno;
    }

    public void setAcctno(Integer acctno) {
        this.acctno = acctno;
    }

    public Set<Genacctentry> getGenacctentrys() {
        return genacctentrys;
    }

    public void setGenacctentrys(Set<Genacctentry> genacctentrys) {
        this.genacctentrys = genacctentrys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Genacct genacct = (Genacct) o;
        return Objects.equals(id, genacct.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Genacct{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", type='" + type + "'" +
            ", acctno='" + acctno + "'" +
            '}';
    }
}
