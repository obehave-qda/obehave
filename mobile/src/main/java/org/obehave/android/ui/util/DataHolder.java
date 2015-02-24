package org.obehave.android.ui.util;

import org.obehave.model.Action;
import org.obehave.model.Color;
import org.obehave.model.Node;
import org.obehave.model.Subject;
import org.obehave.model.modifier.ModifierFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataHolder {
    private static DataHolder instance = new DataHolder();
    private List<Subject> subjects;
    private List<Action> actions;
    private Node root;
    private Node rudel1;
    private Node rudel2;
    private Node rudel2_1;

    public static DataHolder getInstance() {
        return instance;
    }

    private DataHolder() {
    }

    private int randomNumber(int min, int max){
        return min + (int)(Math.random()*max);
    }

    private List<Subject> getSubjects(Node node){
        return null;
    }

    public List<Subject> getAllSubjects() {
        if(subjects == null) {
            generateSubjects();
        }

        return subjects;
    }

    private void buildSubjectNode(){
        //this.buildSubjectNode();

        root = new Node(Subject.class);
        root.setTitle("Root");

        rudel1 = new Node(Subject.class);
        rudel2 = new Node(Subject.class);
        rudel2_1 = new Node(Subject.class);
        rudel1.setTitle("Rudel 1");
        rudel2.setTitle("Rudel 2");
        rudel2_1.setTitle("Rudel 21");

        root.addChild(rudel1);
        root.addChild(rudel2);
        rudel2.addChild(rudel2_1);
    }

    /**
     * Only for testing purpose.
     */
    private void generateSubjects(){
        buildSubjectNode();
        //root = new Node();
        subjects = new ArrayList<Subject>();

        String subjectNamens[] = {
                "Cherokee",
                "Amarok",
                "Kia",
                "Kim",
                "Kenai",
                "Kaspar",
                "Aragon",
                "Chitto",
                "Shima",
                "Tala",
                "Nanuk",
                "Una",
                "Geronimo",
                "Wamblee",
                "Yukon",
                "Apache",
                "Kay",
                "Tatonga",
                "Tayanita",
                "Wapi"

        };
        int i = 0;
        for (String name : subjectNamens) {
            i++;
            Subject subject = new Subject(name);
            subject.setAlias(name.substring(0, 2));
            subject.setColor(new Color(randomNumber(0, 255), randomNumber(0, 255), randomNumber(0, 255)));
            subjects.add(subject);

            Node parentNode = root;
            if( (i%4) == 0) {
                parentNode = rudel1;
            }
            else if( (i%3) == 0) {
                parentNode = rudel2_1;
            }
            else if( (i%2) == 0) {
                parentNode = rudel2;
            }

            Node node = new Node(Subject.class);
            node.setData(subject);
            parentNode.addChild(node);
        }
    }

    public List<Action> getAllActions() {
        if(actions == null) {
            generateActions();
        }

        return actions;
    }

    /**
     *  Only for testing purpose.
     */

    private void generateActions(){
        actions = new ArrayList<Action>();
        generateNumberRangeActions();
        generateSubjectModifierFactoryActions();
        generateEnumerationModifierActions();
    }

    private void generateSubjectModifierFactoryActions(){
        String actionNames[] = {
                "spielen",
                "beißen",
                "schlafen",
                "heulen"
        };

        for (String actionName : actionNames) {
            Action action = new Action(actionName);
            action.setType(Action.Type.POINT);
            action.setAlias(actionName.substring(0, 2));
            action.setRecurring(0);
            ModifierFactory subjectModifierFactory = new ModifierFactory(subjects.get(1), subjects.get(2), subjects.get(3));
            subjectModifierFactory.setName("Subject Modifier");
            subjectModifierFactory.setAlias("su");
            action.setModifierFactory(subjectModifierFactory);

            actions.add(action);
        }
    }

    private void generateNumberRangeActions(){
        String actionNames[] = {
                "number 1",
                "number 2",
                "number 3"
        };

        for (String actionName : actionNames) {
            Action action = new Action(actionName);
            action.setType(Action.Type.POINT);
            action.setAlias(actionName.substring(0, 2));
            action.setRecurring(0);
            ModifierFactory decimalRangeModifierFactory = new ModifierFactory(20, 100);
            decimalRangeModifierFactory.setName("Decimal Modifier");
            decimalRangeModifierFactory.setAlias("su");
            action.setModifierFactory(decimalRangeModifierFactory);

            actions.add(action);
        }
    }

    private void generateEnumerationModifierActions(){
        String actionNames[] = {
                "enum 1",
                "enum 2",
                "enum 3"
        };

        for (String actionName : actionNames) {
            Action action = new Action(actionName);
            action.setType(Action.Type.POINT);
            action.setAlias(actionName.substring(0, 2));
            action.setRecurring(0);
            ModifierFactory enumerationModifierFactory = new ModifierFactory("Value1", "Value 2", "Value 3", "Value4");
            enumerationModifierFactory.setName("Enumeration Modifier");
            enumerationModifierFactory.setAlias("enu");
            action.setModifierFactory(enumerationModifierFactory);

            actions.add(action);
        }
    }

    public List<Node> getChildrenOfNode(Node node){
        if(node == null){
            node = root;
        }

        List<Node> nodes = new ArrayList<Node>();

        for(Node currentNode: node.getChildren()){
            if(currentNode.getData() == null){
                nodes.add(currentNode);
            }
        }

        return Collections.unmodifiableList(nodes);
    }

    public List<Subject> getSubjectsByNode(Node node){
        if(node == null){
            node = root;
        }

        List<Subject> subjects = new ArrayList<Subject>();


        for(Node currentNode: node.getChildren()){
            if(currentNode.getData() != null){
                Subject subject  = (Subject) currentNode.getData();
                subjects.add(subject);
            }
        }

        return Collections.unmodifiableList(subjects);

    }
}
