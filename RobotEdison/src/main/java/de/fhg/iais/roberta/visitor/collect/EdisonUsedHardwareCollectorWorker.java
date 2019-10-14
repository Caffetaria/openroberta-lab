package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.bean.UsedHardwareBean.Builder;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.visitor.validate.AbstractCollectorVisitor;

public final class EdisonUsedHardwareCollectorWorker extends AbstractUsedHardwareCollectorWorker {
    @Override
    protected AbstractCollectorVisitor getVisitor(Builder builder, Project project) {
        return new EdisonUsedHardwareCollectorVisitor(builder, project.getProgramAst().getTree(), project.getConfigurationAst());
    }
}
