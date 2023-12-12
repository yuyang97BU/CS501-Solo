package com.example.rngesus;

import java.util.List;

public class NewsBean {

    /**
     * reference : Acts 21:32
     * verses : [{"book_id":"ACT","book_name":"Acts","chapter":21,"verse":32,"text":"Who immediately took soldiers and centurions, and ran down unto them: and when they saw the chief captain and the soldiers, they left beating of Paul.\n"}]
     * text : Who immediately took soldiers and centurions, and ran down unto them: and when they saw the chief captain and the soldiers, they left beating of Paul.
     * translation_id : kjv
     * translation_name : King James Version
     * translation_note : Public Domain
     */

    private String reference;
    private String text;
    private String translation_id;
    private String translation_name;
    private String translation_note;
    private List<VersesBean> verses;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTranslation_id() {
        return translation_id;
    }

    public void setTranslation_id(String translation_id) {
        this.translation_id = translation_id;
    }

    public String getTranslation_name() {
        return translation_name;
    }

    public void setTranslation_name(String translation_name) {
        this.translation_name = translation_name;
    }

    public String getTranslation_note() {
        return translation_note;
    }

    public void setTranslation_note(String translation_note) {
        this.translation_note = translation_note;
    }

    public List<VersesBean> getVerses() {
        return verses;
    }

    public void setVerses(List<VersesBean> verses) {
        this.verses = verses;
    }

    public static class VersesBean {
        /**
         * book_id : ACT
         * book_name : Acts
         * chapter : 21
         * verse : 32
         * text : Who immediately took soldiers and centurions, and ran down unto them: and when they saw the chief captain and the soldiers, they left beating of Paul.

         */

        private String book_id;
        private String book_name;
        private int chapter;
        private int verse;
        private String text;

        public String getBook_id() {
            return book_id;
        }

        public void setBook_id(String book_id) {
            this.book_id = book_id;
        }

        public String getBook_name() {
            return book_name;
        }

        public void setBook_name(String book_name) {
            this.book_name = book_name;
        }

        public int getChapter() {
            return chapter;
        }

        public void setChapter(int chapter) {
            this.chapter = chapter;
        }

        public int getVerse() {
            return verse;
        }

        public void setVerse(int verse) {
            this.verse = verse;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
