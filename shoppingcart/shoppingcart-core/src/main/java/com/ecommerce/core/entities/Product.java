package com.ecommerce.core.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Siva
 *
 */
@Entity
@Table(name="products")
@Data
public class Product implements Serializable
{
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	@Column(nullable=false, unique=true)
	private String sku;
	@Column(nullable=false)
	private String name;
	private String description;
	@Column(nullable=false)
	private BigDecimal price = new BigDecimal("0.0");
	private String imageUrl;
	private boolean disabled;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_on")
	private Date createdOn = new Date();
	
	@ManyToOne
	@JoinColumn(name="cat_id")
	private Category category;

}
