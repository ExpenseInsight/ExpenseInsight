package com.analytics.expenseinsight.restapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Table(
        name = "filters",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id","filterName"})
        }
)
@Data
public class Filter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int filterId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String filterName;

    private Date startDate;
    private Date endDate;

    @ManyToMany
    @JoinTable(
            name = "filter_include_tags",
            joinColumns = @JoinColumn(name = "filter_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> includeTags;

    @ManyToMany
    @JoinTable(
            name = "filter_exclude_tags",
            joinColumns = @JoinColumn(name = "filter_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> excludeTags;


    private Date createdAt;
    private Date updatedAt;

    private String paymentType;

    private double minAmount;
    private  double maxAmount;
}
