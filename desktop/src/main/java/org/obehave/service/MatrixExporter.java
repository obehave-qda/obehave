package org.obehave.service;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.*;
import org.obehave.model.modifier.Modifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;


public class MatrixExporter implements Exporter {
    private static final Logger log = LoggerFactory.getLogger(MatrixExporter.class);
    private final ExportDataWriter exportDataWriter;
    private final boolean summarize;

    public MatrixExporter(ExportDataWriter exportDataWriter, boolean summarize) {
        this.exportDataWriter = exportDataWriter;
        this.summarize = summarize;
    }

    @Override
    public void export(List<Observation> observations, List<Subject> subjects, List<Node> actionNodes) throws ServiceException {
        List<ExportData> exportDatas = new ArrayList<>();

        if (summarize) {
            for (Map.Entry<String, Table<String, String, Number>> e : aggregate(observations, subjects, actionNodes).entrySet()) {
                exportDatas.add(new ExportData<>(e.getKey(), "Summary", e.getValue()));
            }
        } else {
            for (Node n : actionNodes) {
                for (Map.Entry<String, Table<String, String, Number>> e : aggregate(observations, subjects, Collections.singletonList(n)).entrySet()) {
                    exportDatas.add(new ExportData<>(e.getKey(), n.getDisplayString(), e.getValue()));
                }
            }
        }

        exportDataWriter.write(exportDatas);
    }

    private Map<String, Table<String, String, Number>> aggregate(List<Observation> observations, List<Subject> subjects, List<Node> actions) throws ServiceException {
        final Map<String, Table<String, String, Number>> dataMap = new HashMap<>();
        final List<String> subjectCaptions = subjects.stream().map(Subject::getDisplayString).collect(Collectors.toList());

        final Table<String, String, Number> countTable = ArrayTable.create(subjectCaptions, subjectCaptions);

        final Table<String, String, Number> durationTable = ArrayTable.create(subjectCaptions, subjectCaptions);


        dataMap.put("Count", countTable);
        dataMap.put("Duration", durationTable);

        final List<Action> allValidActions = getAllActions(actions);

        for (Observation o : observations) {
            for (Coding c : o.getCodings()) {
                if (subjects.contains(c.getSubject()) && allValidActions.contains(c.getAction()) && isSubjectModifierInList(c, subjects)) {
                    final String subjectName = c.getSubject().getName();

                    String modifierSubjectName = subjectName;
                    if (c.getModifier() != null) {
                        modifierSubjectName = ((Subject) c.getModifier().get()).getName();
                    }
                    if (countTable.get(subjectName, modifierSubjectName) == null){
                        countTable.put(subjectName, modifierSubjectName, 0L);
                    }
                    final Number countValue = countTable.get(subjectName, modifierSubjectName);
                    countTable.put(subjectName, modifierSubjectName, countValue.longValue() + 1);

                    if (!c.isOpen()) {
                        if (durationTable.get(subjectName, modifierSubjectName) == null){
                            durationTable.put(subjectName, modifierSubjectName, 0d);
                        }
                        final Number durationValue = durationTable.get(subjectName, modifierSubjectName);
                        durationTable.put(subjectName, modifierSubjectName, durationValue.doubleValue() + c.getDuration());
                    }
                }
            }
        }

        return dataMap;
    }

    private List<Action> getAllActions(List<Node> actionNodes) {
        List<Action> actions = new ArrayList<>();

        for (Node actionNode : actionNodes) {
            if (actionNode.getDataType() == Action.class) {
                if (actionNode.getData() != null) {
                    // if actionNode contains an Action, add it to list
                    actions.add((Action) actionNode.getData());
                } else {
                    // if it's an action group, recursively resolve the actions of it's children
                    actions.addAll(getAllActions(actionNode.getChildren()));
                }
            }
        }

        return actions;
    }



    private boolean isSubjectModifierInList(Coding c, List<Subject> subjects) {
        return (c.getModifier() == null || (c.getModifier().getType() == Modifier.Type.SUBJECT_MODIFIER) && subjects.contains(c.getModifier().get()));
    }




}
