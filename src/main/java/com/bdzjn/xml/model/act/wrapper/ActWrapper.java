package com.bdzjn.xml.model.act.wrapper;


import com.bdzjn.xml.model.act.Act;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "wrapper")
public class ActWrapper {

    @XmlElement(required = true)
    private List<Act> acts;

    public ActWrapper() {
    }

    public ActWrapper(List<Act> acts) {
        this.acts = acts;
    }

    public List<Act> getActs() {
        return acts;
    }

    public void setActs(List<Act> acts) {
        this.acts = acts;
    }
}
