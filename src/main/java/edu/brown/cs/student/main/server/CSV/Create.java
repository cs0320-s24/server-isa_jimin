package edu.brown.cs.student.main.server.CSV;

import edu.brown.cs.student.main.FactoryFailureException;

import java.util.List;

public class Create implements CreatorFromRow<CreateObject> {
    /**
     * This class implements the CreatorFromRow interface.
     */
    public Create(){}
    /**
     * This method makes CreateObjects.
     */
    @Override
    public CreateObject create(List<String> row) throws FactoryFailureException {
        return new CreateObject();
    }
}
