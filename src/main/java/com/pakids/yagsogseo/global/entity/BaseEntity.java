package com.pakids.yagsogseo.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column(nullable = false, updatable = false,name = "created_at")
  @CreatedDate
  private LocalDateTime createdAt;
  @Column(nullable = false, name = "updated_at")
  @LastModifiedDate
  private LocalDateTime updatedAt;
  @Column(name = "deleted_at", nullable = true)
  private LocalDateTime deletedAt;

}
