package com.todolist.todolist.model;

import com.todolist.todolist.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Task {
    @Id
    @GeneratedValue
    private Integer id;

    private String title;

    private String description;

    private Date creationDate;

    private Date dueDate;

    @ManyToOne
    private User owner;

    private Status status;

}
