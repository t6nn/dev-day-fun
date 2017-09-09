package eu.t6nn.demo.codecomp.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class TaskDef {

    private String id;
    private String name;
    private long defaultScore;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDefaultScore() {
        return defaultScore;
    }

    public void setDefaultScore(long defaultScore) {
        this.defaultScore = defaultScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TaskDef taskDef = (TaskDef) o;

        return new EqualsBuilder()
                .append(defaultScore, taskDef.defaultScore)
                .append(id, taskDef.id)
                .append(name, taskDef.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(defaultScore)
                .toHashCode();
    }
}
