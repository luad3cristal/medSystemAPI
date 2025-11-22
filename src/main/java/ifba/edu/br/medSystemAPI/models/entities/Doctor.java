package ifba.edu.br.medSystemAPI.models.entities;

import ifba.edu.br.medSystemAPI.models.enums.Specialty;

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
  private String email;
  private String phone;
  
  @Column(unique = true)
  private String crm;

  @Enumerated(EnumType.STRING)
  private Specialty specialty;

  @ManyToOne
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

  public static class Builder {
    private String name;
    private String email;
    private String phone;
    private String crm;
    private Specialty specialty;
    private Address address;

    public Builder name(String name) { this.name = name; return this; }
    public Builder email(String email) { this.email = email; return this; }
    public Builder phone(String phone) { this.phone = phone; return this; }
    public Builder crm(String crm) { this.crm = crm; return this; }
    public Builder specialty(Specialty specialty) { this.specialty = specialty; return this; }
    public Builder address(Address address) { this.address = address; return this; }

    public Doctor build() {
        Doctor doctor = new Doctor();
        doctor.setName(this.name);
        doctor.setEmail(this.email);
        doctor.setPhone(this.phone);
        doctor.setCRM(this.crm);
        doctor.setSpecialty(this.specialty);
        doctor.setAddress(this.address);
        return doctor;
    }
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
