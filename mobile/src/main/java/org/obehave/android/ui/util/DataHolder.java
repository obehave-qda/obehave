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
    private Node subjectRoot;
    private Node subjectNode1;
    private Node subjectNode2;
    private Node subjectNode2_1;

    private Node actionRoot;
    private Node actionNode1;
    private Node actionNode2;
    private Node actionNode2_1;

    private int actionIndex = 0;

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

        subjectRoot = new Node(Subject.class);
        subjectRoot.setTitle("Root");

        subjectNode1 = new Node(Subject.class);
        subjectNode2 = new Node(Subject.class);
        subjectNode2_1 = new Node(Subject.class);
        subjectNode1.setTitle("Rudel 1");
        subjectNode2.setTitle("Rudel 2");
        subjectNode2_1.setTitle("Rudel 21");

        subjectRoot.addChild(subjectNode1);
        subjectRoot.addChild(subjectNode2);
        subjectNode2.addChild(subjectNode2_1);
    }

    private void buildActionNode(){
        //this.buildSubjectNode();

        actionRoot = new Node(Action.class);
        actionRoot.setTitle("Root");

        actionNode1 = new Node(Action.class);
        actionNode2 = new Node(Action.class);
        actionNode2_1 = new Node(Action.class);
        actionNode1.setTitle("ActionNode 1");
        actionNode2.setTitle("ActionNode 2");
        actionNode2_1.setTitle("ActionNode 2_1");

        actionRoot.addChild(actionNode1);
        actionRoot.addChild(actionNode2);
        actionNode2.addChild(actionNode2_1);
    }

    /**
     * Only for testing purpose.
     */
    private void generateSubjects(){
        buildSubjectNode();
        //subjectRoot = new Node();
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

            Node parentNode = subjectRoot;
            if( (i%4) == 0) {
                parentNode = subjectNode1;
            }
            else if( (i%3) == 0) {
                parentNode = subjectNode2_1;
            }
            else if( (i%2) == 0) {
                parentNode = subjectNode2;
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
        actionIndex = 0;
        buildActionNode();
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

            addActionToNode(action);

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
            addActionToNode(action);
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
            addActionToNode(action);
            actions.add(action);
        }
    }

    private void addActionToNode(Action action){
        Node parentNode = actionRoot;
        actionIndex++;
        if( (actionIndex%4) == 0) {
            parentNode = actionNode1;
        }
        else if( (actionIndex%3) == 0) {
            parentNode = actionNode2_1;
        }
        else if( (actionIndex%2) == 0) {
            parentNode = actionNode2;
        }

        Node node = new Node(Action.class);
        node.setData(action);
        parentNode.addChild(node);
    }

    public List<Node> getChildrenOfSubjectNode(Node node){
        if(node == null){
            node = subjectRoot;
        }

        return getChildrenOfNode(node);
    }

    public List<Node> getChildrenOfActionNode(Node node){
        if(node == null){
            node = actionRoot;
        }

        return getChildrenOfNode(node);
    }

    private List<Node> getChildrenOfNode(Node node){
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
            node = subjectRoot;
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

    public List<Action> getActionsByNode(Node node){
        if(node == null){
            node = actionRoot;
        }

        List<Action> actions = new ArrayList<Action>();

        for(Node currentNode: node.getChildren()){
            if(currentNode.getData() != null){
                Action action = (Action) currentNode.getData();
                actions.add(action);
            }
        }

        return Collections.unmodifiableList(actions);
    }
}
