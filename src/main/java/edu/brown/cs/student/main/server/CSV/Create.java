package edu.brown.cs.student.main.server.CSV;

import edu.brown.cs.student.main.CreateObject;

import java.util.List;

public class Create implements CreatorFromRow<CreateObject>{
    /**
     * This class implements the CreatorFromRow interface.
     */
    public Create(){}
    /**
     * This method makes CreateObjects.
     */
    @Override
    public CreateObject create(List<String> row) throws FactoryFailureException {
        CreateObject returnObject = new CreateObject();
        return returnObject;
    }
}
