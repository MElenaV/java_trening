package ru.stqa.pft.addressbook.model;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@XStreamAlias("group")
@Entity       // аннотация объявляет класс GroupData привязанным к БД
@Table(name = "group_list")       // сопоставление класса к таблице БД, т.к. названия отличаются
public class GroupData {
  @XStreamOmitField
  @Id       // атрибут id используется как идентификатор, поэтому ему присваивается особоая аннотация - подсказка ID
  @Column(name = "group_id")       // привязка к столбцу таблицы, т.к. название столбца не совпадает с названием атрибута
  private int id = Integer.MAX_VALUE;

  @Expose
  @Column(name = "group_name")       // привязка к столбцу таблицы, т.к. название столбца не совпадает с названием атрибута
  private String name;

  @Expose
  @Column(name = "group_header")
  @Type(type = "text")
  private String header;

  @Expose
  @Column(name = "group_footer")
  @Type(type = "text")        // поля с footer многострочные, поэтому в БД они хранятся иначе; сделали подсказку, т.к. hibernate не смог автоматически сделать преобразование типов
  private String footer;

  @ManyToMany(mappedBy = "groups")    // означает, что groups в парном классе ContactData нужно найти свойство или атрибут groups и оттуда взять описание связи между ними
  private Set<ContactData> contacts = new HashSet<ContactData>();

  public Contacts getContacts() {
    return new Contacts(contacts);
  }

  public int getId() { return id; }

  public GroupData withId(int id) {
    this.id = id;
    return this;
  }

  public GroupData withName(String name) {
    this.name = name;
    return this;
  }

  public GroupData withHeader(String header) {
    this.header = header;
    return this;
  }

  public GroupData withFooter(String footer) {
    this.footer = footer;
    return this;
  }

  public String getName() {
    return name;
  }

  public String getHeader() {
    return header;
  }

  public String getFooter() {
    return footer;
  }

  @Override
  public String toString() {
    return "GroupData{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    GroupData groupData = (GroupData) o;

   /* if (id != groupData.id) return false;
    if (name != null ? !name.equals(groupData.name) : groupData.name != null) return false;
    if (header != null ? !header.equals(groupData.header) : groupData.header != null) return false;
    return  footer != null ? footer.equals(groupData.footer) : groupData.footer == null;*/
    return id == groupData.id && Objects.equals(name, groupData.name) && Objects.equals(header, groupData.header) && Objects.equals(footer, groupData.footer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, header, footer);
  }

}
