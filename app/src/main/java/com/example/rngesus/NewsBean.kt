package com.example.rngesus

class NewsBean {
    /**
     * reference : Acts 21:32
     * verses : [{"book_id":"ACT","book_name":"Acts","chapter":21,"verse":32,"text":"Who immediately took soldiers and centurions, and ran down unto them: and when they saw the chief captain and the soldiers, they left beating of Paul.\n"}]
     * text : Who immediately took soldiers and centurions, and ran down unto them: and when they saw the chief captain and the soldiers, they left beating of Paul.
     * translation_id : kjv
     * translation_name : King James Version
     * translation_note : Public Domain
     */
    var reference: String? = null
    var text: String? = null
    var translation_id: String? = null
    var translation_name: String? = null
    var translation_note: String? = null
    var verses: List<VersesBean>? = null

    class VersesBean {
        /**
         * book_id : ACT
         * book_name : Acts
         * chapter : 21
         * verse : 32
         * text : Who immediately took soldiers and centurions, and ran down unto them: and when they saw the chief captain and the soldiers, they left beating of Paul.
         *
         */
        var book_id: String? = null
        var book_name: String? = null
        var chapter = 0
        var verse = 0
        var text: String? = null
    }
}
