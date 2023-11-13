package com.switzerlandbank.api.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.switzerlandbank.api.entities.enums.Gender;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tb_client")
public class Client implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "cannot be null")
	private String name;
	@NotNull(message = "cannot be null")
	private String cpf;
	@NotNull(message = "cannot be null")
	private String motherName;
	@NotNull(message = "cannot be null")
	private LocalDate dateBirth;
	@NotNull(message = "cannot be null")
	private Integer gender;
	@NotNull(message = "cannot be null")
	private String email;
	@NotNull(message = "cannot be null")
	private String password;
	
	@NotNull(message = "address fields cannot be null")
	@OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
	private Address address;
	
	@JsonIgnore
	@OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
	private Account account;
	
	public Client() {
	}

	public Client(Long id, String name, String cpf, String motherName, LocalDate dateBirth, Gender gender,
			String email, String password) {
		this.id = id;
		this.name = name;
		this.cpf = cpf;
		this.motherName = motherName;
		this.dateBirth = dateBirth;
		this.setGender(gender);
		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public LocalDate getDateBirth() {
		return dateBirth;
	}

	public void setDateBirth(LocalDate dateBirth) {
		this.dateBirth = dateBirth;
	}

	public Gender getGender() {
		return Gender.valueOf(gender);
	}

	public void setGender(Gender gender) {
		this.gender = gender.getCode();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
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
		Client other = (Client) obj;
		return Objects.equals(id, other.id);
	}

}
