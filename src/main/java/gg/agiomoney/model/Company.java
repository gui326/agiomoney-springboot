package gg.agiomoney.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "company")
public class Company implements Serializable{
	
	private static final long serialVersionUID = 4715651237926177144L;
	
	@Id
	@SequenceGenerator(name = "generator", sequenceName = "company_code_seq", allocationSize = 1)
	@GeneratedValue(generator = "generator", strategy = GenerationType.SEQUENCE)
	private Long code;
	private String name;
	private String email;
	private String password;
	private Float tax;
	
	@Override
	public int hashCode() {
		return Objects.hash(code, email, name, password, tax);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Company other = (Company) obj;
		return Objects.equals(code, other.code) && Objects.equals(email, other.email)
				&& Objects.equals(name, other.name) && Objects.equals(password, other.password)
				&& Objects.equals(tax, other.tax);
	}
	public Long getCode() {
		return code;
	}
	public void setCode(Long code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Float getTax() {
		return tax;
	}
	public void setTax(Float tax) {
		this.tax = tax;
	}
	
	
	
	
	
}
