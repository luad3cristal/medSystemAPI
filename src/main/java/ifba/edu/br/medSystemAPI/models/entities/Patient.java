package ifba.edu.br.medSystemAPI.models.entities;

import ifba.edu.br.medSystemAPI.dtos.patient.request.PatientCreateDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "patients")
public class Patient {
  @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String email;
  private String phone;

  @Column(unique = true)
  private String cpf;

  @ManyToOne
  @JoinColumn(name = "address_id")
  private Address address;

  private Boolean status = true;

  public Patient () {}

  public Patient (String name, String email, String phone, String cpf, Address address) {
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.cpf = cpf;
    this.address = address;
  }

  public Patient (PatientCreateDTO patient) {
    this.name = patient.name();
    this.email = patient.email();
    this.phone = patient.phone();
    this.cpf = patient.cpf();
    this.address = new Address(patient.address());
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }

  public String getCPF() { return cpf; }
  public void setCPF(String cpf) { this.cpf = cpf; }

  public Address getAddress() { return address; }
  public void setAddress(Address address) { this.address = address; }

  public Boolean getStatus() { return status; }
  public void setStatus(Boolean status) { this.status = status; }

}
