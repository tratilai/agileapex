package com.agileapex.common;

import java.util.Random;

public class SchemaUtil {

    private Random randomGenerator = new Random();

    // Note: this same method is used in the agile-apex project. This is used when admin user creates a new
    // user in the same project (schema). Internal-schema-id is unique, public-schema-id is not (in the DB). 
    // So, one internal-schema-id can point to multiple public-schema-ids.
    //
    public String createUniquePublicId() {
        String schemaPublicId = "s" + (50000000000L + (randomGenerator.nextLong() % 9999999999L));
        return schemaPublicId;
    }
}
