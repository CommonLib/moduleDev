package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GenerateClass {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.pwc.sdc.recruit.data.model.greendao");
        addNote(schema);
        new DaoGenerator().generateAll(schema, "C:\\Users\\byang059\\AndroidStudioProjects\\pwcrecruit\\app\\src\\main\\java");
    }

    /**
     * @param schema
     */
    private static void addNote(Schema schema) {
        Entity candidate = schema.addEntity("Candidate");
        candidate.addIdProperty();
        candidate.addStringProperty("userName").notNull();
        candidate.addStringProperty("password");
    }
}
