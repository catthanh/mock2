package com.example.mock2.auditable;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@MappedSuperclass // parent class not entity
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<T> {
  @Column(name = "creationDate", updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreatedDate
  protected Date creationDate;

  @Column(name = "lastMod_date")
  @LastModifiedDate
  @Temporal(TemporalType.TIMESTAMP)
  protected Date lastModifiedDate;

  @CreatedBy
  @Column(name = "created_by")
  protected T createdBy;

  @LastModifiedBy
  @Column(name = "modified_by")
  protected T modifiedBy;

  public Auditable(T createdBy, T modifiedBy) {
    this.createdBy = createdBy;
    this.modifiedBy = modifiedBy;
  }


}
