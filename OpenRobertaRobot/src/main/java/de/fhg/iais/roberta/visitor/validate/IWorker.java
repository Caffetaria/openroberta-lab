package de.fhg.iais.roberta.visitor.validate;

import java.util.Map;

import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.IVisitor;

public interface IWorker {
    String getName();

    void execute(Project project);

    Map<String, String> getResult();

    Key getResultKey();
}
