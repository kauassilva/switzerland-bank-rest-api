package com.switzerlandbank.api.entities;

import java.io.Serializable;
import java.util.Objects;

import com.switzerlandbank.api.entities.enums.KeyType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tb_pixkey")
public class PixKey implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String keyValue;
	@NotNull(message = "cannot be null")
	private Integer keyType;
	
	@ManyToOne
	@JoinColumn(name = "account_id")
	@NotNull(message = "cannot be null")
	private Account account;
	
	public PixKey() {
	}

	public PixKey(Long id, String keyValue, KeyType keyType, Account account) {
		this.id = id;
		this.keyValue = keyValue;
		this.setKeyType(keyType);
		this.account = account;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public KeyType getKeyType() {
		return KeyType.valueOf(keyType);
	}

	public void setKeyType(KeyType keyType) {
		this.keyType = keyType.getCode();
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PixKey other = (PixKey) obj;
		return Objects.equals(id, other.id);
	}
	
}
