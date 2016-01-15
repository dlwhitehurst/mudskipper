package com.ciwise.mudskipper.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Genacctentry.
 */
@Entity
@Table(name = "genacctentry")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "genacctentry")
public class Genacctentry implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 128)
    @Column(name = "entrytext", length = 128, nullable = false)
    private String entrytext;

    @NotNull
    @Column(name = "entrydate", nullable = false)
    private LocalDate entrydate;

    @Column(name = "debit", precision=10, scale=2)
    private BigDecimal debit;

    @Column(name = "credit", precision=10, scale=2)
    private BigDecimal credit;

    @ManyToOne
    @JoinColumn(name = "genacct_id")
    private Genacct genacct;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntrytext() {
        return entrytext;
    }

    public void setEntrytext(String entrytext) {
        this.entrytext = entrytext;
    }

    public LocalDate getEntrydate() {
        return entrydate;
    }

    public void setEntrydate(LocalDate entrydate) {
        this.entrydate = entrydate;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public Genacct getGenacct() {
        return genacct;
    }

    public void setGenacct(Genacct genacct) {
        this.genacct = genacct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Genacctentry genacctentry = (Genacctentry) o;
        return Objects.equals(id, genacctentry.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Genacctentry{" +
            "id=" + id +
            ", entrytext='" + entrytext + "'" +
            ", entrydate='" + entrydate + "'" +
            ", debit='" + debit + "'" +
            ", credit='" + credit + "'" +
            '}';
    }
}
