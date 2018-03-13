package com.takkaiah.db.dto;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table (name="ItemMaster")
public class Item {
			
	@GenericGenerator(name="generator", strategy="increment")
	@GeneratedValue(generator="generator")
	@Column(name = "ItemID")
	@Id
	private int  itemID;
	
	@Column(name = "EANCode")
    private String eanCode;

	@Column(name = "ItemName")
	private String itemName;

	
	@Column(name = "Alias")
	private String alias;

	@ManyToOne
	@JoinColumn(name="catID")
	private ItemCategory itemCategory;
	
	@Column(name = "units")
	private String units;
	
	@Column(name = "TaxPercent")
	private float taxPercent;
	
	@Column(name = "CaseQty",nullable = true)
	private Integer caseQty;
	
	@Column(name = "hsn_code")
	private String hsnCode;
	
	@OneToMany (mappedBy="item", cascade=CascadeType.ALL, fetch = FetchType.LAZY) 
	private List<ItemMRP> itemPrice;

	
	@OneToMany (mappedBy="custItem")
	private List<CustomerArticleCode> custArticleCode;

	
	@OneToMany (mappedBy="custMapItem")
	private List<Item> custMapItem;

	
	public Item() {
		super();
	}


	public int getItemID() {
		return itemID;
	}


	public void setItemID(int itemID) {
		this.itemID = itemID;
	}


	public String getEanCode() {
		return eanCode;
	}


	public void setEanCode(String eanCode) {
		this.eanCode = eanCode;
	}


	public String getItemName() {
		return itemName;
	}


	public void setItemName(String itemName) {
		this.itemName = itemName;
	}


	public String getAlias() {
		return alias;
	}


	public void setAlias(String alias) {
		this.alias = alias;
	}


	public ItemCategory getItemCategory() {
		return itemCategory;
	}


	public void setItemCategory(ItemCategory itemCategory) {
		this.itemCategory = itemCategory;
	}


	public String getUnits() {
		return units;
	}


	public void setUnits(String units) {
		this.units = units;
	}


	public List<ItemMRP> getItemPrice() {
		return itemPrice;
	}


	public void setItemPrice(List<ItemMRP> itemPrice) {
		this.itemPrice = itemPrice;
	}

	
	

	public float getTaxPercent() {
		return taxPercent;
	}


	public void setTaxPercent(float taxPercent) {
		this.taxPercent = taxPercent;
	}

	
	public Integer getCaseQty() {
		return caseQty;
	}


	public void setCaseQty(Integer caseQty) {
		this.caseQty = caseQty;
	}


	public List<CustomerArticleCode> getCustArticleCode() {
		return custArticleCode;
	}


	public void setCustArticleCode(List<CustomerArticleCode> custArticleCode) {
		this.custArticleCode = custArticleCode;
	}


	public List<Item> getCustMapItem() {
		return custMapItem;
	}


	public void setCustMapItem(List<Item> custMapItem) {
		this.custMapItem = custMapItem;
	}


	public String getHsnCode() {
		return hsnCode;
	}


	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}
}
