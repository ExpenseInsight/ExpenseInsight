package com.analytics.expenseinsight.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "filters")
@Data
public class Filter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int filterId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String filterName;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ElementCollection
    @CollectionTable(name = "filter_include_tags", joinColumns = @JoinColumn(name = "filter_id"))
    @Column(name = "tag_id")
    private List<Integer> includeTags;

    @ElementCollection
    @CollectionTable(name = "filter_exclude_tags", joinColumns = @JoinColumn(name = "filter_id"))
    @Column(name = "tag_id")
    private List<Integer> excludeTags;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
