package com.ticksy.model;

import jakarta.persistence.*;

@Entity
@Table(name = "priorities")
public class Priority extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "level", nullable = false)
    private int level;

    @Column(name = "color", length = 7)
    private String color;

    public Priority() {}

    public Priority(String name, int level, String color) {
        this.name = name;
        this.level = level;
        this.color = color;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    @Override
    public String toString() { return name; }
}
