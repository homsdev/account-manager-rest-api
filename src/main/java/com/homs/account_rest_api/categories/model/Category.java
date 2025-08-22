package com.homs.account_rest_api.categories.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cli_category")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "category_id", updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "category_name", nullable = false)
    private String name;
}
