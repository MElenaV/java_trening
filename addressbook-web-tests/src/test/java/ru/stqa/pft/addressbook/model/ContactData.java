package ru.stqa.pft.addressbook.model;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@XStreamAlias("contact")
@Entity
@Table(name = "addressbook")
public class ContactData {
  @XStreamOmitField
  @Id
  @Column(name = "id")       // можно было эту привязка к столбцу таблицы не делать, т.к. название столбца таблицы совпадает с названием атрибута
  private int id = Integer.MAX_VALUE;

  @Expose
  @Column(name = "firstname")       // можно было эту привязка к столбцу таблицы не делать, т.к. название столбца таблицы совпадает с названием атрибута
  private String firstname;

  @Expose
  @Column(name = "lastname")        // можно было эту привязка к столбцу таблицы не делать, т.к. название столбца таблицы совпадает с названием атрибута
  private String lastname;

  @Expose
  @Type(type = "text")
  private String address;

  @Expose
  @Column(name = "home")
  @Type(type = "text")
  private String homePhone;

  @Expose
  @Column(name = "mobile")
  @Type(type = "text")
  private String mobilePhone;

  @Expose
  @Column(name = "work")
  @Type(type = "text")
  private String workPhone;

  @Expose
  @Column(name = "email")
  @Type(type = "text")
  private String email1;

  @Expose
  @Column(name = "email2")
  @Type(type = "text")
  private String email2;

  @Expose
  @Column(name = "email3")
  @Type(type = "text")
  private String email3;

  @Transient
  private String allEmail;


  @Transient
  private String allPhones;

  @Column(name = "photo")
  @Type(type = "text")
  private String photo;       // атрибут имеет тип файл, но в БД хранится строка, поэтому преобразуем в String (а getter и setter преобразуем в файл, чтобы ничего не поломать)

  @ManyToMany(fetch = FetchType.EAGER)    // опция fetch по умолчанию имеет значение LAZY (из БД извлекается, как можно меньше информации, меняем её на EAGER, чтобы извлекалось больше инфы за один заход)
  @JoinTable(name = "address_in_groups",
          joinColumns = @JoinColumn(name = "id"), inverseJoinColumns = @JoinColumn(name = "group_id"))    // JoinTable - в качестве связующей таблицы используется; joinColumns - столбец, который указывает на объект текущего класса;  inverseJoinColumns - обратный столбец, который указывает на объект другого типа
  private Set<GroupData> groups = new HashSet<GroupData>(); // согласно документации hibernate (https://docs.jboss.org/hibernate/orm/5.4/userguide) инициализируем свойство, создаём пустое множество

  public File getPhoto() {
    if (photo == null) {
      return null;
    } else {
      return new File(photo);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    ContactData that = (ContactData) o;
    if (this == o || firstname == null || lastname == null || address == null || homePhone == null || mobilePhone == null || workPhone == null || email1 == null || email2 == null || email3 == null) return true;
    return id == that.id && Objects.equals(firstname, that.firstname) && Objects.equals(lastname, that.lastname) && Objects.equals(address, that.address) && Objects.equals(homePhone, that.homePhone) && Objects.equals(mobilePhone, that.mobilePhone) && Objects.equals(workPhone, that.workPhone) && Objects.equals(email1, that.email1) && Objects.equals(email2, that.email2) && Objects.equals(email3, that.email3);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstname, lastname, address, homePhone, mobilePhone, workPhone, email1, email2, email3);
  }

  @Override
  public String toString() {
    return "ContactData{" +
            "id=" + id +
            ", firstname='" + firstname + '\'' +
            ", lastname='" + lastname + '\'' +
            ", address='" + address + '\'' +
            ", homePhone='" + homePhone + '\'' +
            ", mobilePhone='" + mobilePhone + '\'' +
            ", workPhone='" + workPhone + '\'' +
            ", email1='" + email1 + '\'' +
            ", email2='" + email2 + '\'' +
            ", email3='" + email3 + '\'' +
            '}';
  }

  public ContactData withPhoto(File photo) {
    this.photo = photo.getPath();
    return this;
  }

  public ContactData withId(int id) {
    this.id = id;
    return this;
  }

  public ContactData withFirstname(String firstname) {
    this.firstname = firstname;
    return this;
  }

  public ContactData withLastname(String lastname) {
    this.lastname = lastname;
    return this;
  }

  public ContactData withAddress(String address) {
    this.address = address;
    return this;
  }

  public ContactData withHomePhone(String homePhone) {
    this.homePhone = homePhone;
    return this;
  }

  public ContactData withMobilePhone(String mobilePhone) {
    this.mobilePhone = mobilePhone;
    return this;
  }

  public ContactData withWorkPhone(String workPhone) {
    this.workPhone = workPhone;
    return this;
  }

  public ContactData withAllPhones(String allPhones) {
    this.allPhones = allPhones;
    return this;
  }


  public ContactData withEmail1(String email1) {
    this.email1 = email1;
    return this;
  }

  public ContactData withEmail2(String email2) {
    this.email2 = email2;
    return this;
  }

  public ContactData withEmail3(String email3) {
    this.email3 = email3;
    return this;
  }

  public ContactData withAllEmail(String allEmail) {
    this.allEmail = allEmail;
    return this;
  }

  public void withGroup(Set<GroupData> groups) {
    this.groups = groups;
  }

  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public String getAddress() {
    return address;
  }

  public String getHomePhone() { return homePhone; }

  public String getMobilePhone() {
    return mobilePhone;
  }

  public String getWorkPhone() {
    return workPhone;
  }

  public String getAllPhones() { return allPhones; }

  public String getEmail1() {    return email1;  }

  public String getEmail2() { return email2;   }

  public String getEmail3() {  return email3;  }

  public String getAllEmail() {
    return allEmail;
  }

  public Groups getGroups() {    // сгенерили getter и изменили его, чтобы возвращал объекты типа Groups для этого внутри сделали преобразования: множества превратили в объект типа Groups (при этом создается копия)
    return new Groups(groups);
  }

  public int getId() { return id; }

  public ContactData inGroup(GroupData group) {
    groups.add(group);    // помечаем контакт, как добавленный в какую-то группу
  return this;    // возвращаем this, чтобы можно было это вытягивать в цепочку
  }
}
