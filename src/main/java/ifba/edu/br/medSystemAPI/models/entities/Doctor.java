package ifba.edu.br.medSystemAPI.models.entities;

import ifba.edu.br.medSystemAPI.models.enums.Specialty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "doctors")
public class Doctor {
  @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  @Column(updatable = false)
  private String email;
  private String phone;
  
  @Column(unique = true, updatable = false)
  private String crm;

  @Enumerated(EnumType.STRING)
  @Column(updatable = false)
  private Specialty specialty;

  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "address_id")
  private Address address;

  private Boolean status = true;

  public Doctor () {}

  public Doctor (String name, String email, String phone, String crm, Specialty specialty, Address address) {
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.crm = crm;
    this.specialty = specialty;
    this.address = address;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }

  public String getCRM() { return crm; }
  public void setCRM(String crm) { this.crm = crm; }

  public Specialty getSpecialty() { return specialty; }
  public void setSpecialty(Specialty specialty) { this.specialty = specialty; }

  public Address getAddress() { return address; }
  public void setAddress(Address address) { this.address = address; }

  public Boolean getStatus() { return status; }
  public void setStatus(Boolean status) { this.status = status; }
}
