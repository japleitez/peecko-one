package com.peecko.one.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "help_item")
public class HelpItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "help_item_gen")
    @SequenceGenerator(name = "help_item_gen", sequenceName = "help_item_seq")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "language", nullable = false)
    private String lang;

    @NotNull
    @Column(name = "question")
    private String question;

    @NotNull
    @Column(name = "answer")
    private String answer;

    public HelpItem() {}

    public HelpItem(String lang, String question, String answer) {
        this.lang = lang;
        this.question = question;
        this.answer = answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
