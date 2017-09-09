package eu.t6nn.demo.codecomp.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

public class GameDef {

    public enum Rules {
        CODEGOLF;
    }

    private String name;
    private boolean enabled = true;
    private Rules rules;
    private List<TaskDef> tasks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }

    public List<TaskDef> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDef> tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        GameDef gameDef = (GameDef) o;

        return new EqualsBuilder()
                .append(enabled, gameDef.enabled)
                .append(name, gameDef.name)
                .append(rules, gameDef.rules)
                .append(tasks, gameDef.tasks)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(enabled)
                .append(rules)
                .append(tasks)
                .toHashCode();
    }
}
