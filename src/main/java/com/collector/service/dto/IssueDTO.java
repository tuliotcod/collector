package com.collector.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Issue entity.
 */
public class IssueDTO implements Serializable {

    private Long id;

    private Long number;

    private String info;

    @Lob
    private byte[] cover;
    private String coverContentType;

    private Integer month;

    private Integer year;

    private Integer day;

    private Integer pages;

    private Boolean sameFormatAllIssues;

    private BigDecimal coverPrice;

    private Instant creationDate;

    private Instant lastUpdate;

    private Long titleId;

    private Long coverCollectorUserId;

    private Long formatId;

    private Long finishingId;

    private Long currencyId;

    private Long countryId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public byte[] getCover() {
        return cover;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }

    public String getCoverContentType() {
        return coverContentType;
    }

    public void setCoverContentType(String coverContentType) {
        this.coverContentType = coverContentType;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Boolean isSameFormatAllIssues() {
        return sameFormatAllIssues;
    }

    public void setSameFormatAllIssues(Boolean sameFormatAllIssues) {
        this.sameFormatAllIssues = sameFormatAllIssues;
    }

    public BigDecimal getCoverPrice() {
        return coverPrice;
    }

    public void setCoverPrice(BigDecimal coverPrice) {
        this.coverPrice = coverPrice;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getTitleId() {
        return titleId;
    }

    public void setTitleId(Long titleId) {
        this.titleId = titleId;
    }

    public Long getCoverCollectorUserId() {
        return coverCollectorUserId;
    }

    public void setCoverCollectorUserId(Long collectorUserId) {
        this.coverCollectorUserId = collectorUserId;
    }

    public Long getFormatId() {
        return formatId;
    }

    public void setFormatId(Long formatId) {
        this.formatId = formatId;
    }

    public Long getFinishingId() {
        return finishingId;
    }

    public void setFinishingId(Long finishingId) {
        this.finishingId = finishingId;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IssueDTO issueDTO = (IssueDTO) o;
        if (issueDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), issueDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IssueDTO{" +
            "id=" + getId() +
            ", number=" + getNumber() +
            ", info='" + getInfo() + "'" +
            ", cover='" + getCover() + "'" +
            ", month=" + getMonth() +
            ", year=" + getYear() +
            ", day=" + getDay() +
            ", pages=" + getPages() +
            ", sameFormatAllIssues='" + isSameFormatAllIssues() + "'" +
            ", coverPrice=" + getCoverPrice() +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", title=" + getTitleId() +
            ", coverCollectorUser=" + getCoverCollectorUserId() +
            ", format=" + getFormatId() +
            ", finishing=" + getFinishingId() +
            ", currency=" + getCurrencyId() +
            ", country=" + getCountryId() +
            "}";
    }
}
